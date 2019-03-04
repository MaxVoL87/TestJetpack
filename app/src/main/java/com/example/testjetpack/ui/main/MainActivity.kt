package com.example.testjetpack.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import com.example.testjetpack.R
import com.example.testjetpack.ui.base.BaseActivity
import com.example.testjetpack.ui.main.myprofile.MyProfileFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_main.*

class MainActivity : BaseActivity<MainActivityVM>(),
    FragmentManager.OnBackStackChangedListener,
    NavigationView.OnNavigationItemSelectedListener {
    override val viewModelClass: Class<MainActivityVM> = MainActivityVM::class.java
    override val layoutId: Int = R.layout.activity_main
    override val containerId: Int = R.id.container
    override val observeLiveData: MainActivityVM.() -> Unit
        get() = {

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        //Listen for changes in the back stack
        supportFragmentManager.addOnBackStackChangedListener(this)
        //Handle when activity is recreated like on orientation Change
        shouldDisplayHomeUp()

        nav_view.setNavigationItemSelectedListener(this)

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

    // region OnNavigationItemSelectedListener impl
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_my_profile -> {
                openMyProfile()
            }
            R.id.nav_my_history -> {

            }
            R.id.nav_manage -> {

            }

            R.id.nav_tutorials -> {

            }
            R.id.nav_provide_feedback -> {

            }

            R.id.nav_log_out -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    // endregion OnNavigationItemSelectedListener impl

    private fun shouldDisplayHomeUp() {
        //Enable Up button only  if there are entries in the back stack
        supportActionBar?.setDisplayHomeAsUpEnabled(supportFragmentManager.backStackEntryCount > 0)
    }

    private fun openMyProfile() {
        replaceFragment(MyProfileFragment.newInstance(), false)
    }
}
