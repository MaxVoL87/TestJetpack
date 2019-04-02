package com.example.testjetpack.ui.main

import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import com.example.testjetpack.R
import com.example.testjetpack.databinding.ActivityMainBinding
import com.example.testjetpack.databinding.NavHeaderMainBinding
import com.example.testjetpack.models.git.db.GitRepositoryView
import com.example.testjetpack.models.own.Notification
import com.example.testjetpack.ui.base.BaseActivity
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.ui.main.gitreposearch.GitRepoSearchFragment
import com.example.testjetpack.ui.main.gitreposearch.IGitRepoSearchFragmentCallback
import com.example.testjetpack.ui.main.gps.IGpsFragmentCallback
import com.example.testjetpack.ui.main.gps.GpsFragment
import com.example.testjetpack.ui.main.myprofile.IMyProfileFragmentCallback
import com.example.testjetpack.ui.main.myprofile.MyProfileFragment
import com.example.testjetpack.ui.main.notifications.INotificationFragmentCallback
import com.example.testjetpack.ui.main.notifications.NotificationFragment
import com.example.testjetpack.utils.browseWithoutCurrentApp
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import javax.inject.Inject
import kotlin.reflect.KClass


class MainActivity : BaseActivity<ActivityMainBinding, MainActivityVM>(),
    FragmentManager.OnBackStackChangedListener,
    IGitRepoSearchFragmentCallback,
    IMyProfileFragmentCallback,
    INotificationFragmentCallback,
    IGpsFragmentCallback {

    override val viewModelClass: Class<MainActivityVM> = MainActivityVM::class.java
    override val layoutId: Int = R.layout.activity_main
    override val containerId: Int = R.id.container
    override val observeLiveData: MainActivityVM.() -> Unit
        get() = {
        }

    @Inject
    lateinit var picasso: Picasso

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val headerBinding: NavHeaderMainBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0))
        headerBinding.lifecycleOwner = this

        binding.viewModel = viewModel
        headerBinding.profile = viewModel.profile
        headerBinding.picasso = picasso

        setSupportActionBar(toolbar)

        //Listen for changes in the back stack
        supportFragmentManager.addOnBackStackChangedListener(this)
        //Handle when activity is recreated like on orientation Change
        shouldDisplayHomeUp()

        openGitRepoSearch(false)
    }

    override fun onStart() {
        super.onStart()
        viewModel.getProfile()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onBackStackChanged() {
        shouldDisplayHomeUp()
    }

    private fun shouldDisplayHomeUp() {
        //Enable Up button only  if there are entries in the back stack
        supportActionBar?.setDisplayHomeAsUpEnabled(supportFragmentManager.backStackEntryCount > 0)
    }

    private fun openGitRepoSearch(addToBackStack: Boolean) {
        replaceFragment(GitRepoSearchFragment.newInstance(), addToBackStack)
    }

    private fun openMyProfile() {
        replaceFragment(MyProfileFragment.newInstance(), true)
    }

    private fun openGps() {
        replaceFragment(GpsFragment.newInstance(), true)
    }

    private fun openNotifications() {
        replaceFragment(NotificationFragment.newInstance(), true)
    }

    override fun openGitRepository(repo: GitRepositoryView) {
        this.browseWithoutCurrentApp(repo.htmlUrl ?: repo.url)
    }

    override fun openNotificationDetails(notification: Notification) {
        showAlert("Not Implemented")
    }

    override fun openCreditCardDetails() {
        showAlert("Not Implemented")
    }

    // region VM events renderer

    private val closeDrawerRenderer: (Any) -> Unit = { event ->
        event as MainActivityVMEventStateChange.CloseDrawer
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    private val openProfileRenderer: (Any) -> Unit = { event ->
        event as MainActivityVMEventStateChange.OpenProfile
        if (getCurFragment(R.id.container) !is MyProfileFragment) {
            openMyProfile()
        }
    }

    private val openNotificationsRenderer: (Any) -> Unit = { event ->
        event as MainActivityVMEventStateChange.OpenNotifications
        if (getCurFragment(R.id.container) !is NotificationFragment) {
            openNotifications()
        }
    }

    private val openGpsRenderer: (Any) -> Unit = { event ->
        event as MainActivityVMEventStateChange.OpenGps
        if (getCurFragment(R.id.container) !is GpsFragment) {
            openGps()
        }
    }

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
        MainActivityVMEventStateChange.OpenProfile::class to openProfileRenderer,
        MainActivityVMEventStateChange.OpenGps::class to openGpsRenderer,
        MainActivityVMEventStateChange.OpenNotifications::class to openNotificationsRenderer,
        MainActivityVMEventStateChange.CloseDrawer::class to closeDrawerRenderer

    )
    // endregion VM events renderer
}
