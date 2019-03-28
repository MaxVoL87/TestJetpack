package com.example.testjetpack.dataflow

import androidx.annotation.MainThread
import androidx.paging.PagedList
import com.example.testjetpack.MainApplication
import com.example.testjetpack.dataflow.network.IGitApi
import com.example.testjetpack.models.git.db.GitRepositoryView
import com.example.testjetpack.models.git.network.request.GitPage
import com.example.testjetpack.models.git.network.response.*
import com.example.testjetpack.utils.fromJson
import com.example.testjetpack.utils.paging.PagingRequestHelper
import com.example.testjetpack.utils.paging.createStatusLiveData
import com.example.testjetpack.utils.reset
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

/**
 * This boundary callback gets notified when user reaches to the edges of the list such that the
 * database cannot provide any more data.
 * <p>
 * The boundary callback might be called multiple times for the same direction so it does its own
 * rate limiting using the PagingRequestHelper class.
 */
class SearchGitReposPListBoundaryCallback(
    private val curPage: AtomicReference<GitPage>,
    private val webservice: IGitApi,
    private val handleResponse: (GitPage, GitResponse<*>?) -> Unit,
    private val ioExecutor: Executor,
    private val skipIfFail: Boolean = false
) : PagedList.BoundaryCallback<GitRepositoryView>() {

    val helper = PagingRequestHelper(ioExecutor)
    val networkState = helper.createStatusLiveData()

    val lastSuccessPageNum = AtomicInteger(-1)

    @Inject
    lateinit var gson: Gson

    init {
        MainApplication.component.inject(this)
    }

    fun reset() {
        curPage.set(curPage.get().reset())
        lastSuccessPageNum.set(-1)
    }

    @Synchronized
    fun getPageNum(): Int {
        return if (lastSuccessPageNum.get() < curPage.get().page && !skipIfFail) {
            lastSuccessPageNum.get()
        } else {
            curPage.get().page
        }
    }

    @Synchronized
    fun setPageNum(num: Int) {
        curPage.set(curPage.get().copy(page = num))
    }

    /**
     * Database returned 0 items. We should query the backend for more items.
     */
    @MainThread
    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            webservice
                .searchRepos(curPage.get().q, getPageNum(), curPage.get().perPage)
                .enqueue(createWebserviceCallback(it))
        }
    }

    /**
     * User reached to the end of the list.
     */
    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: GitRepositoryView) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            setPageNum(getPageNum() + 1)
            webservice
                .searchRepos(curPage.get().q, curPage.get().page, curPage.get().perPage)
                .enqueue(createWebserviceCallback(it))
        }
    }

    @MainThread
    override fun onItemAtFrontLoaded(itemAtFront: GitRepositoryView) {
        // ignored, since we only ever append to what's in the DB
    }

    // Generic just because of less ot write, must be GitRepository
    private fun <T> createWebserviceCallback(it: PagingRequestHelper.Request.Callback): Callback<PagedGitResponse<T>> {

        return object : Callback<PagedGitResponse<T>> {

            override fun onFailure(call: Call<PagedGitResponse<T>>, t: Throwable) {
                it.recordFailure(t)
            }

            /**
             * every time it gets new items, boundary callback simply inserts them into the database and
             * paging library takes care of refreshing the list if necessary.
             */
            override fun onResponse(call: Call<PagedGitResponse<T>>, response: Response<PagedGitResponse<T>>) {
                val resp: GitResponse<*>?
                val func: () -> Unit
                // case error in response
                if (response.body() == null && response.errorBody() != null) {
                    val error = gson
                        .fromJson<ErrorGitListingWithDoc<GitFieldError>>(response.errorBody()?.charStream())

                    resp = error
                    func = { onFailure(call, Exception(error?.message)) }
                } else {
                    lastSuccessPageNum.set(curPage.get().page)

                    resp = response.body()
                    func = it::recordSuccess
                }

                ioExecutor.execute {
                    handleResponse(curPage.get(), resp)
                    func.invoke()
                }
            }
        }

    }
}