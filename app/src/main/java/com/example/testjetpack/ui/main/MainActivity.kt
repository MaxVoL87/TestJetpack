package com.example.testjetpack.ui.main

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.example.testjetpack.R
import com.example.testjetpack.ui.base.BaseActivity
import com.example.testjetpack.ui.main.myprofile.MyProfileFragment

class MainActivity : BaseActivity<MainActivityVM>(),
    FragmentManager.OnBackStackChangedListener {
    override val viewModelClass: Class<MainActivityVM> = MainActivityVM::class.java
    override val layoutId: Int = R.layout.activity_main
    override val containerId: Int = R.id.container
    override val observeLiveData: MainActivityVM.() -> Unit
        get() = {

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Listen for changes in the back stack
        supportFragmentManager.addOnBackStackChangedListener(this)
        //Handle when activity is recreated like on orientation Change
        shouldDisplayHomeUp()

        openMyProfile()
    }

    override fun onBackStackChanged() {
        shouldDisplayHomeUp()
    }

    private fun shouldDisplayHomeUp() {
        //Enable Up button only  if there are entries in the back stack
        supportActionBar?.setDisplayHomeAsUpEnabled(supportFragmentManager.backStackEntryCount > 0)
    }

    private fun openMyProfile() {
        replaceFragment(MyProfileFragment.newInstance(), false)
    }
}
