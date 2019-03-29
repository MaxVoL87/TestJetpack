package com.example.testjetpack.dataflow.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.toLiveData
import com.example.testjetpack.dataflow.SearchGitReposPListBoundaryCallback
import com.example.testjetpack.dataflow.local.AppDatabase
import com.example.testjetpack.dataflow.network.IGitApi
import com.example.testjetpack.models.Listing
import com.example.testjetpack.models.NetworkState
import com.example.testjetpack.models.git.License
import com.example.testjetpack.models.git.User
import com.example.testjetpack.models.git.db.GitRepositoryView
import com.example.testjetpack.models.git.network.request.GitPage
import com.example.testjetpack.models.git.network.response.GitRepository
import com.example.testjetpack.models.git.network.response.GitResponse
import com.example.testjetpack.models.git.network.response.PagedGitResponse
import com.example.testjetpack.utils.getPartOfOrCurrent
import com.example.testjetpack.utils.toDBEntity
import com.example.testjetpack.utils.withNotNullOrEmpty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

class GitDataRepository @Inject constructor(
    private val gitApi: IGitApi,
    private val appDatabase: AppDatabase
) : IGitDataRepository {

    /**
     * Inserts the response into the database while also assigning position indices to items.
     */
    private fun insertGitResultIntoDb(resp: GitResponse<*>?) {
        if (resp is PagedGitResponse<*>) {
            val rRepos = resp.items.mapNotNull { it as? GitRepository }
            val licenses = mutableListOf<License>()
            val owners = mutableListOf<User>()

            appDatabase.runInTransaction {
                withNotNullOrEmpty(rRepos) {
                    val start = appDatabase.getGitRepositoryDao().getNextIndex()
                    val repos = mapIndexed { index, child ->
                        child.license?.let { license -> licenses.add(license) }
                        owners.add(child.owner)
                        return@mapIndexed child.toDBEntity(child.license, child.owner, start + index)
                    }
                    appDatabase.getGitRepositoryDao().insert(*repos.toTypedArray())
                }
                withNotNullOrEmpty(licenses) {
                    appDatabase.getGitLicenseDao().insert(*toTypedArray())
                }
                withNotNullOrEmpty(owners) {
                    appDatabase.getGitUserDao().insert(*toTypedArray())
                }
            }
        }
    }

    /**
     * Returns a Listing for the given page.
     */
    override fun searchGitRepositories(page: GitPage): Listing<GitRepositoryView> {

        appDatabase.runInTransaction {
            appDatabase.clearAllGitData()
        }

        val mPage = AtomicReference(page)

        // create a boundary callback which will observe when the user reaches to the edges of
        // the list and update the database with extra data.
        val boundaryCallback = SearchGitReposPListBoundaryCallback(
            curPage = mPage,
            webservice = gitApi,
            handleResponse = { curPage: GitPage, response -> insertGitResultIntoDb(response) },
            ioExecutor = fiveTPoolFixedExecutor,
            skipIfFail = false
        )

        val refreshState = MutableLiveData(NetworkState.LOADED)

        // we are using a mutable live data to trigger refresh requests which eventually calls
        // refresh method and gets a new live data. Each refresh request by the user becomes a newly
        // dispatched data in refreshTrigger
        val refreshTrigger = {
            refreshState.value = NetworkState.LOADING

            GlobalScope.launch(Dispatchers.IO) {
                boundaryCallback.reset()
                appDatabase.runInTransaction {
                    appDatabase.clearAllGitData()
                }
            }
            refreshState.value = NetworkState.LOADED // for hide refresh layout because of on recycler implemented
        }

        // We use toLiveData Kotlin extension function here, you could also use LivePagedListBuilder
        val livePagedList = appDatabase.getGitDao().getAllGitRepositoriesSortedByRespIndex().toLiveData(
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
                refreshTrigger.invoke()
            },
            refreshState = refreshState
        )
    }

    companion object {
        private val fiveTPoolFixedExecutor = Executors.newFixedThreadPool(5)
    }
}