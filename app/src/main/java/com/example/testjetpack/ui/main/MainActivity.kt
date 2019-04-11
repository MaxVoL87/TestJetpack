package com.example.testjetpack.ui.main

import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.NavigationUI.*
import com.example.testjetpack.R
import com.example.testjetpack.databinding.ActivityMainBinding
import com.example.testjetpack.databinding.NavHeaderMainBinding
import com.example.testjetpack.models.git.db.GitRepositoryView
import com.example.testjetpack.models.own.Notification
import com.example.testjetpack.ui.base.BaseActivity
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.ui.main.gitreposearch.IGitRepoSearchFragmentCallback
import com.example.testjetpack.ui.main.gps.IGpsFragmentCallback
import com.example.testjetpack.ui.main.gps.GpsFragment
import com.example.testjetpack.ui.main.myprofile.IMyProfileFragmentCallback
import com.example.testjetpack.ui.main.myprofile.MyProfileFragment
import com.example.testjetpack.ui.main.notifications.INotificationsFragmentCallback
import com.example.testjetpack.ui.main.notifications.NotificationsFragment
import com.example.testjetpack.utils.browseWithoutCurrentApp
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import org.koin.android.ext.android.inject
import kotlin.reflect.KClass


class MainActivity : BaseActivity<ActivityMainBinding, MainActivityVM>(),
    IGitRepoSearchFragmentCallback,
    IMyProfileFragmentCallback,
    INotificationsFragmentCallback,
    IGpsFragmentCallback {

    override val viewModelClass: KClass<MainActivityVM> = MainActivityVM::class
    override val layoutId: Int = R.layout.activity_main
    override val navControllerId: Int = R.id.nav_host_fragment
    override val observeLiveData: MainActivityVM.() -> Unit
        get() = {
        }

    private val picasso: Picasso by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val headerBinding: NavHeaderMainBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0))
        headerBinding.lifecycleOwner = this

        binding.viewModel = viewModel
        headerBinding.profile = viewModel.profile
        headerBinding.picasso = picasso

        setSupportActionBar(toolbar)

        //Listen for changes in the back stack
        navController.addOnDestinationChangedListener { cont, dest, bundle ->
            // hide drawer if no parent fragment
            drawer_layout.setDrawerLockMode(
                if (dest.id == cont.graph.startDestination) DrawerLayout.LOCK_MODE_UNLOCKED
                else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
            )
        }

        // Update action bar to reflect navigation
        setupActionBarWithNavController(this, navController, drawer_layout)
        // Tie nav graph to items in nav drawer
        setupWithNavController(nav_view, navController)
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

    override fun onSupportNavigateUp(): Boolean {
        return navigateUp(navController, drawer_layout)
    }

    private fun openMyProfile() {
        navController.navigate(R.id.action_global_myProfileFragment)
    }

    private fun openGps() {
        navController.navigate(R.id.action_global_gpsFragment)
    }

    private fun openNotifications() {
        navController.navigate(R.id.action_global_notificationsFragment)
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
        if (getCurFragment(nav_host_fragment) !is MyProfileFragment) {
            openMyProfile()
        }
    }

    private val openGpsRenderer: (Any) -> Unit = { event ->
        event as MainActivityVMEventStateChange.OpenGps
        if (getCurFragment(nav_host_fragment) !is GpsFragment) {
            openGps()
        }
    }

    private val openNotificationsRenderer: (Any) -> Unit = { event ->
        event as MainActivityVMEventStateChange.OpenNotifications
        if (getCurFragment(nav_host_fragment) !is NotificationsFragment) {
            openNotifications()
        }
    }

    private val logOutRenderer: (Any) -> Unit = { event ->
        event as MainActivityVMEventStateChange.LogOut
        finishAndRemoveTask()
    }

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
        MainActivityVMEventStateChange.OpenProfile::class to openProfileRenderer,
        MainActivityVMEventStateChange.OpenGps::class to openGpsRenderer,
        MainActivityVMEventStateChange.OpenNotifications::class to openNotificationsRenderer,
        MainActivityVMEventStateChange.LogOut::class to logOutRenderer,
        MainActivityVMEventStateChange.CloseDrawer::class to closeDrawerRenderer

    )
    // endregion VM events renderer
}
