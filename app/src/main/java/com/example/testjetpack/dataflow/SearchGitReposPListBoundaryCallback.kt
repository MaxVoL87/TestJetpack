package com.example.testjetpack.dataflow

import androidx.annotation.MainThread
import androidx.paging.PagedList
import com.example.testjetpack.dataflow.network.IGitApi
import com.example.testjetpack.models.git.db.GitRepositoryView
import com.example.testjetpack.models.git.network.request.GitPage
import com.example.testjetpack.models.git.network.response.*
import com.example.testjetpack.utils.fromJson
import com.example.testjetpack.utils.paging.PagingRequestHelper
import com.example.testjetpack.utils.paging.createStatusLiveData
import com.example.testjetpack.utils.reset
import com.example.testjetpack.utils.withNotNull
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

/**
 * This boundary callback gets notified when user reaches to the edges of the list such that the
 * database cannot provide any more data.
 * <p>
 * The boundary callback might be called multiple times for the same direction so it does its own
 * rate limiting using the PagingRequestHelper class.
 */
class SearchGitReposPListBoundaryCallback(
    private val _curPage: AtomicReference<GitPage>,
    private val _webservice: IGitApi,
    private val _handleResponseAsync: (GitResponse<*>?) -> Unit,
    private val _skipIfFail: Boolean = false
) : PagedList.BoundaryCallback<GitRepositoryView>(), KoinComponent {

    private val _initialPageNum = AtomicInteger(_curPage.get().page)
    private val _lastSuccessPageNum = AtomicInteger(-1)

    private val _helper = PagingRequestHelper()
    val networkState = _helper.createStatusLiveData()

    private val _gson: Gson by inject()

    fun retry() {
        _helper.retryAllFailed()
    }

    fun reset() {
        _curPage.set(_curPage.get().reset(_initialPageNum.get()))
        _lastSuccessPageNum.set(-1)
    }

    @Synchronized
    fun reinitPageNum(increment: Int) {
        var num: Int

        if (_lastSuccessPageNum.get() < _curPage.get().page && !_skipIfFail) {
            val ls = _lastSuccessPageNum.get()
            num = if (ls < 0) _initialPageNum.get() else ls
        } else {
            num = 0
            num += _curPage.get().page
        }

        num += increment
        if (num >= 0) {
            _curPage.set(_curPage.get().reset(num))
        } else throw Exception("num < 0")
    }

    /**
     * Database returned 0 items. We should query the backend for more items.
     */
    @MainThread
    override fun onZeroItemsLoaded() {
        _helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL, object : PagingRequestHelper.Request {
            override fun run(callback: PagingRequestHelper.Request.Callback) {
                reinitPageNum(0)
                _webservice
                    .searchRepos(_curPage.get().q, _curPage.get().page, _curPage.get().perPage)
                    .enqueue(createWebserviceCallback(callback))
            }
        })
    }

    /**
     * User reached to the end of the list.
     */
    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: GitRepositoryView) {
        _helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER, object : PagingRequestHelper.Request {
            override fun run(callback: PagingRequestHelper.Request.Callback) {

                // last page is current
                if (_lastSuccessPageNum.get() == _curPage.get().page && _curPage.get().isLast == true) {
                    callback.recordSuccess()
                    return
                }

                reinitPageNum(1)
                _webservice
                    .searchRepos(_curPage.get().q, _curPage.get().page, _curPage.get().perPage)
                    .enqueue(createWebserviceCallback(callback))
            }
        })
    }

    @MainThread
    override fun onItemAtFrontLoaded(itemAtFront: GitRepositoryView) {
        // ignored, since we only ever append to what's in the DB
    }

    // Generic just because of less ot write, must be GitRepository
    private fun <T> createWebserviceCallback(it: PagingRequestHelper.Request.Callback): Callback<PagedGitResponse<T>> {

        return object : Callback<PagedGitResponse<T>> {

            override fun onFailure(call: Call<PagedGitResponse<T>>, t: Throwable) {
                GlobalScope.launch(Dispatchers.IO) {
                    it.recordFailure(t)
                }
            }

            /**
             * every time it gets new items, boundary callback simply inserts them into the database and
             * paging library takes care of refreshing the list if necessary.
             */
            override fun onResponse(call: Call<PagedGitResponse<T>>, response: Response<PagedGitResponse<T>>) {
                GlobalScope.launch(Dispatchers.IO) {
                    val func: () -> Unit
                    val resp: GitResponse<*>?

                    // case error in response
                    if (response.body() == null && response.errorBody() != null) {
                        val error = _gson
                            .fromJson<ErrorGitListingWithDoc<GitFieldError>>(response.errorBody()?.charStream())

                        resp = error
                        func = { onFailure(call, Exception(error?.message)) }
                    } else {
                        resp = response.body()?.apply { initLinks(response) }

                        withNotNull(resp?.getPage()) {
                            _curPage.set(this)
                        }
                        _lastSuccessPageNum.set(_curPage.get().page)

                        func = it::recordSuccess
                    }

                    _handleResponseAsync(resp)
                    func.invoke()
                }
            }
        }

    }
}