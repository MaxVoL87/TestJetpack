package com.example.testjetpack.dataflow.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import com.example.testjetpack.dataflow.local.AppDatabase
import com.example.testjetpack.dataflow.network.IGitApi
import com.example.testjetpack.models.Notification
import com.example.testjetpack.models.Profile
import com.example.testjetpack.models.git.GitRepository
import com.example.testjetpack.models.git.network.*
import com.example.testjetpack.utils.getPartOfOrCurrent
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val gitApi: IGitApi,
    private val appDatabase: AppDatabase
) : IDataRepository {

    override fun getProfileAsync(): Deferred<Profile> {
        //todo: change with api
        return GlobalScope.async {
            Profile(
                "https://picsum.photos/200/200/?random",
                "Benjamin",
                "Bankog",
                "11.07.1975",
                "MORGA753116SM9IJ",
                "MQQ346789C"
            )
        }
    }

    override fun getNotificationsAsync(): Deferred<List<Notification>> {
        //todo: change with api
        return GlobalScope.async {
            listOf(
                Notification(
                    "00001",
                    "Welcome!",
                    "Snooze these notifications for: an hour, eight hours, a day, three days, or the next week. Or, turn email notifications off. For more detailed preferences, see your account page.",
                    "02.02.2019",
                    "02.02.2019"
                ),
                Notification(
                    "00002",
                    "Your email has been verified",
                    "Thank you for verifying your email address. Your new Upclick account has been activated and you can now login to the Merchant Area. All your account details...",
                    "20.02.2019",
                    null
                ),
                Notification(
                    "00003",
                    "Start your travel",
                    "This article is part of our Travel Business Startup Guide—a curated list of articles to help you plan, start, and grow your travel business!",
                    "20.04.2019",
                    null
                ),
                Notification(
                    "00004",
                    "Hello",
                    "Hello from the outside \nAt least I can say that I've tried \nTo tell you I'm sorry \nFor breaking your heart…",
                    "02.20.2019",
                    null
                )
            )
        }
    }


//region Git


    /**
     * Inserts the response into the database while also assigning position indices to items.
     */
    private fun insertResultIntoDb(resp: ServerResponse?) {
        if (resp is SearchRepositoriesResponse) {
            resp.items.let { repos ->
                appDatabase.runInTransaction {
                    val start = appDatabase.gitRepositoryDao().getNextIndex()
                    val items = repos.mapIndexed { index, child ->
                        child.indexInResponse = start + index
                        return@mapIndexed child
                    }
                    appDatabase.gitRepositoryDao().insert(*items.toTypedArray())
                }
            }
        }
    }

    /**
     * When refresh is called, we simply run a fresh network request and when it arrives, clear
     * the database table and insert all new items in a transaction.
     * <p>
     * Since the PagedList already uses a database bound data source, it will automatically be
     * updated after the database transaction is finished.
     */
    private fun refresh(page: GitPage): LiveData<NetworkState> {
        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.LOADING
        gitApi.searchRepos(page.q, page.number.get(), page.perPage).enqueue(
            object : Callback<SearchRepositoriesResponse> {
                override fun onFailure(call: Call<SearchRepositoriesResponse>, t: Throwable) {
                    // retrofit calls this on main thread so safe to call set value
                    networkState.value = NetworkState.error(t.message)
                }

                override fun onResponse(
                    call: Call<SearchRepositoriesResponse>,
                    response: Response<SearchRepositoriesResponse>
                ) {
                    GlobalScope.launch(Dispatchers.IO) {
                        appDatabase.runInTransaction {
                            appDatabase.gitRepositoryDao().clearAll()
                            insertResultIntoDb(response.body())
                        }
                        // since we are in bg thread now, post the result.
                        networkState.postValue(NetworkState.LOADED)
                    }
                }
            }
        )
        return networkState
    }

    /**
     * Returns a Listing for the given page.
     */
    override fun getGitRepositories(page: GitPage): Listing<GitRepository> {

        appDatabase.runInTransaction {
            appDatabase.gitRepositoryDao().clearAll()
        }

        // create a boundary callback which will observe when the user reaches to the edges of
        // the list and update the database with extra data.
        val boundaryCallback = GitSearchRepositoriesBoundaryCallback(
            curPage = page,
            webservice = gitApi,
            handleResponse = { p: GitPage, r: ServerResponse? -> this.insertResultIntoDb(r) },
            ioExecutor = fiveTPoolFixedExecutor
        )
        // we are using a mutable live data to trigger refresh requests which eventually calls
        // refresh method and gets a new live data. Each refresh request by the user becomes a newly
        // dispatched data in refreshTrigger
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) { refresh(page) }

        // We use toLiveData Kotlin extension function here, you could also use LivePagedListBuilder
        val livePagedList = appDatabase.gitRepositoryDao().getAllSortedByRespIndex().toLiveData(
            pageSize = page.perPage.getPartOfOrCurrent(0.6),
            boundaryCallback = boundaryCallback
        )

        return Listing(
            pagedList = livePagedList,
            networkState = boundaryCallback.networkState,
            retry = {
                boundaryCallback.helper.retryAllFailed()
            },
            refresh = {
                refreshTrigger.value = null
            },
            refreshState = refreshState
        )
    }

    //endregion Git


    companion object {
        private val fiveTPoolFixedExecutor = Executors.newFixedThreadPool(5)
    }
}