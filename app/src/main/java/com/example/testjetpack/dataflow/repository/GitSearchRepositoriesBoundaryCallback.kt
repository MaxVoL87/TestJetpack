package com.example.testjetpack.dataflow.repository

import androidx.annotation.MainThread
import androidx.paging.PagedList
import com.example.testjetpack.dataflow.network.IGitApi
import com.example.testjetpack.models.git.GitRepository
import com.example.testjetpack.models.git.network.GitPage
import com.example.testjetpack.models.git.network.SearchRepositoriesResponse
import com.example.testjetpack.models.git.network.ServerResponse
import com.example.testjetpack.utils.paging.PagingRequestHelper
import com.example.testjetpack.utils.paging.createStatusLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

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
    private val ioExecutor: Executor
) : PagedList.BoundaryCallback<GitRepository>() {

    val helper = PagingRequestHelper(ioExecutor)
    val networkState = helper.createStatusLiveData()

    /**
     * Database returned 0 items. We should query the backend for more items.
     */
    @MainThread
    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            webservice.searchRepos(curPage.q, curPage.number.get(),  curPage.perPage).enqueue(createWebserviceCallback(it))
        }
    }

    /**
     * User reached to the end of the list.
     */
    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: GitRepository) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            webservice.searchRepos(curPage.q, curPage.number.incrementAndGet(),  curPage.perPage).enqueue(createWebserviceCallback(it))
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: GitRepository) {
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
            override fun onResponse(call: Call<SearchRepositoriesResponse>, response: Response<SearchRepositoriesResponse>) {
                ioExecutor.execute {
                    handleResponse(curPage, response.body())
                    it.recordSuccess()
                }
            }
        }

    }
}