package com.example.testjetpack.ui.main

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.collection.LruCache
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.ui.NavigationUI.*
import com.example.testjetpack.R
import com.example.testjetpack.databinding.ActivityMainBinding
import com.example.testjetpack.databinding.ItemNavHeaderMainBinding
import com.example.testjetpack.models.git.db.GitRepositoryView
import com.example.testjetpack.models.own.Notification
import com.example.testjetpack.ui.base.BaseActivity
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.ui.main.gitrepodetails.IGitRepoDetailsFragmentCallback
import com.example.testjetpack.ui.main.gitreposearch.GitRepoSearchFragmentDirections
import com.example.testjetpack.ui.main.gitreposearch.IGitRepoSearchFragmentCallback
import com.example.testjetpack.ui.main.gps.IGpsFragmentCallback
import com.example.testjetpack.ui.main.gps.GpsFragment
import com.example.testjetpack.ui.main.myprofile.IMyProfileFragmentCallback
import com.example.testjetpack.ui.main.myprofile.MyProfileFragment
import com.example.testjetpack.ui.main.mystatus.IMyStatusFragmentCallback
import com.example.testjetpack.ui.main.mystatus.MyStatusFragment
import com.example.testjetpack.ui.main.mytrip.IMyTripFragmentCallback
import com.example.testjetpack.ui.main.mytrip.MyTripFragment
import com.example.testjetpack.ui.main.notifications.INotificationsFragmentCallback
import com.example.testjetpack.ui.main.notifications.NotificationsFragment
import com.example.testjetpack.utils.browseWithoutCurrentApp
import com.example.testjetpack.utils.showNotImplemented
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.toolbar_main.*
import org.koin.android.ext.android.inject
import kotlin.reflect.KClass


class MainActivity : BaseActivity<ActivityMainBinding, MainActivityVM>(),
    IGitRepoSearchFragmentCallback,
    IGitRepoDetailsFragmentCallback,
    IMyProfileFragmentCallback,
    IMyTripFragmentCallback,
    IGpsFragmentCallback,
    IMyStatusFragmentCallback,
    INotificationsFragmentCallback {

    override val viewModelClass: KClass<MainActivityVM> = MainActivityVM::class
    override val layoutId: Int = R.layout.activity_main
    override val navControllerId: Int = R.id.nav_host_fragment
    override val observeLiveData: MainActivityVM.() -> Unit
        get() = {
        }

    private val _picasso: Picasso by inject()
    private val _cache: LruCache<String, Any> by inject()
    private val _onDestinationChangedListener =
        NavController.OnDestinationChangedListener { controller, destination, _ ->
            // hide drawer if no parent fragment
            drawer_layout.setDrawerLockMode(
                if (destination.id == controller.graph.startDestination) DrawerLayout.LOCK_MODE_UNLOCKED
                else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
            )
        }
    private val _drawerListener = object : DrawerLayout.DrawerListener {
        override fun onDrawerStateChanged(newState: Int) {
            if (newState == DrawerLayout.STATE_IDLE && !drawer_layout.isDrawerOpen(GravityCompat.START)) {
                _drawerTask.invoke()
            }
        }

        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        }

        override fun onDrawerClosed(drawerView: View) {
        }

        override fun onDrawerOpened(drawerView: View) {
        }
    }
    private var _drawerTask: () -> Unit = {}
        get() {
            val task = field
            field = {} // reset
            return task
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val headerBinding = ItemNavHeaderMainBinding.bind(binding.navView.getHeaderView(0))
        headerBinding.lifecycleOwner = this

        binding.viewModel = viewModel
        headerBinding.profile = viewModel.profile
        headerBinding.picasso = _picasso

        setSupportActionBar(cToolbar)

        // Update action bar to reflect navigation
        setupActionBarWithNavController(this, navController, drawer_layout)
        // Tie nav graph to items in nav drawer
        setupWithNavController(nav_view, navController)

        //Listen for changes in the back stack
        navController.addOnDestinationChangedListener(_onDestinationChangedListener)

        drawer_layout.addDrawerListener(_drawerListener)
    }

    override fun onStart() {
        super.onStart()
        viewModel.getProfile()
    }

    override fun onDestroy() {
        super.onDestroy()
        //Listen for changes in the back stack
        navController.removeOnDestinationChangedListener(_onDestinationChangedListener)

        drawer_layout.removeDrawerListener(_drawerListener)
        _drawerTask = {}
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigateUp(navController, drawer_layout)
    }

    private fun openMyProfile() {
        openFragmentDrawerTask<MyProfileFragment>(R.id.action_global_myProfileFragment)
    }

    private fun openTrip() {
        openFragmentDrawerTask<MyTripFragment>(R.id.action_global_myTripFragment)
    }

    private fun openGps() {
        openFragmentDrawerTask<GpsFragment>(R.id.action_global_gpsFragment)
    }

    private fun openStatus() {
        openFragmentDrawerTask<MyStatusFragment>(R.id.action_global_myStatusFragment)
    }

    private fun openNotifications() {
        openFragmentDrawerTask<NotificationsFragment>(R.id.action_global_notificationsFragment)
    }

    private fun openFeedback() {
        closeDrawer()
        showNotImplemented()
    }

    private inline fun <reified T> openFragmentDrawerTask(globalTaskId: Int) {
        closeDrawer()
        if (getCurFragment(nav_host_fragment) !is T) {
            _drawerTask = { navController.navigate(globalTaskId) }
        }
    }

    override fun openGitRepository(repoUrl: String) {
        this.browseWithoutCurrentApp(repoUrl)
    }

    override fun showGitRepository(repo: GitRepositoryView, fragmentNavigatorExtras: FragmentNavigator.Extras?) {
        val sElementImageViewMapEntry = fragmentNavigatorExtras!!.sharedElements.entries.first()
        val actionDetails = GitRepoSearchFragmentDirections.actionGitRepoSearchFragmentToGitRepoDetailsFragment(
            repo,
            sElementImageViewMapEntry.value
        )
        _cache.put(repo.avatarUrl!!, (sElementImageViewMapEntry.key as ImageView).drawable)
        navController.navigate(actionDetails, fragmentNavigatorExtras)
    }

    override fun openNotificationDetails(notification: Notification) {
        showNotImplemented()
    }

    override fun openCreditCardDetails() {
        showNotImplemented()
    }

    private fun closeDrawer() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }
    // region VM events renderer

    private val openProfileRenderer: (Any) -> Unit = { event ->
        event as MainActivityVMEventStateChange.OpenProfile
        openMyProfile()
    }

    private val openTripRenderer: (Any) -> Unit = { event ->
        event as MainActivityVMEventStateChange.OpenTrip
        openTrip()
    }

    private val openGpsRenderer: (Any) -> Unit = { event ->
        event as MainActivityVMEventStateChange.OpenGps
        openGps()
    }

    private val openStatusRenderer: (Any) -> Unit = { event ->
        event as MainActivityVMEventStateChange.OpenStatus
        openStatus()
    }

    private val openNotificationsRenderer: (Any) -> Unit = { event ->
        event as MainActivityVMEventStateChange.OpenNotifications
        openNotifications()
    }

    private val openFeedbackRenderer: (Any) -> Unit = { event ->
        event as MainActivityVMEventStateChange.OpenFeedback
        openFeedback()
    }

    private val logOutRenderer: (Any) -> Unit = { event ->
        event as MainActivityVMEventStateChange.LogOut
        finishAndRemoveTask()
    }

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
        MainActivityVMEventStateChange.OpenProfile::class to openProfileRenderer,
        MainActivityVMEventStateChange.OpenTrip::class to openTripRenderer,
        MainActivityVMEventStateChange.OpenGps::class to openGpsRenderer,
        MainActivityVMEventStateChange.OpenStatus::class to openStatusRenderer,
        MainActivityVMEventStateChange.OpenNotifications::class to openNotificationsRenderer,
        MainActivityVMEventStateChange.OpenFeedback::class to openFeedbackRenderer,
        MainActivityVMEventStateChange.LogOut::class to logOutRenderer
    )
    // endregion VM events renderer
}
