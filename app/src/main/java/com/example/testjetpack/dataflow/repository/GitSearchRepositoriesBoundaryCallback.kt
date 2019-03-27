package com.example.testjetpack.dataflow.repository

import androidx.annotation.MainThread
import androidx.paging.PagedList
import com.example.testjetpack.MainApplication
import com.example.testjetpack.dataflow.network.IGitApi
import com.example.testjetpack.models.GitRepositoryView
import com.example.testjetpack.models.git.network.ErrorInvalidFieldsWithDocsResponse
import com.example.testjetpack.models.git.network.GitPage
import com.example.testjetpack.models.git.network.SearchRepositoriesResponse
import com.example.testjetpack.models.git.network.ServerResponse
import com.example.testjetpack.utils.paging.PagingRequestHelper
import com.example.testjetpack.utils.paging.createStatusLiveData
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

/**
 * This boundary callback gets notified when user reaches to the edges of the list such that the
 * database cannot provide any more data.
 * <p>
 * The boundary callback might be called multiple times for the same direction so it does its own
 * rate limiting using the PagingRequestHelper class.
 */
class GitSearchRepositoriesBoundaryCallback(
    private val curPage: GitPage,
    private val webservice: IGitApi,
    private val handleResponse: (GitPage, ServerResponse?) -> Unit,
    private val ioExecutor: Executor,
    private val skipIfFail: Boolean
) : PagedList.BoundaryCallback<GitRepositoryView>() {

    @Inject
    lateinit var gson: Gson

    val helper = PagingRequestHelper(ioExecutor)
    val networkState = helper.createStatusLiveData()

    val lastSuccessPageNum = AtomicInteger(-1)

    init {
        MainApplication.component.inject(this)
    }

    @Synchronized
    fun getPageNum(): Int {
        return if (lastSuccessPageNum.get() != curPage.number.get() && !skipIfFail) {
            lastSuccessPageNum.get()
        } else {
            curPage.number.get()
        }
    }

    /**
     * Database returned 0 items. We should query the backend for more items.
     */
    @MainThread
    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            webservice
                .searchRepos(curPage.q, getPageNum(), curPage.perPage)
                .enqueue(createWebserviceCallback(it))
        }
    }

    /**
     * User reached to the end of the list.
     */
    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: GitRepositoryView) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            curPage.number.set(getPageNum() + 1)
            webservice.searchRepos(curPage.q, curPage.number.get(), curPage.perPage)
                .enqueue(createWebserviceCallback(it))
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: GitRepositoryView) {
        // ignored, since we only ever append to what's in the DB
    }

    private fun createWebserviceCallback(it: PagingRequestHelper.Request.Callback): Callback<SearchRepositoriesResponse> {

        return object : Callback<SearchRepositoriesResponse> {
            override fun onFailure(call: Call<SearchRepositoriesResponse>, t: Throwable) {
                it.recordFailure(t)
            }

            /**
             * every time it gets new items, boundary callback simply inserts them into the database and
             * paging library takes care of refreshing the list if necessary.
             */
            override fun onResponse(
                call: Call<SearchRepositoriesResponse>,
                response: Response<SearchRepositoriesResponse>
            ) {
                // case error in response
                if (response.body() == null && response.errorBody() != null) {
                    val error = gson.fromJson<ErrorInvalidFieldsWithDocsResponse>(
                        response.errorBody()?.charStream(),
                        ErrorInvalidFieldsWithDocsResponse::class.java
                    )
                    onFailure(call, Exception(error.message))
                    return
                }

                lastSuccessPageNum.set(curPage.number.get())
                ioExecutor.execute {
                    handleResponse(curPage, response.body())
                    it.recordSuccess()
                }
            }
        }

    }
}