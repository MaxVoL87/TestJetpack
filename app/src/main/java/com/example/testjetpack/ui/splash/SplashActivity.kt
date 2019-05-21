package com.example.testjetpack.ui.splash

import android.content.Intent
import android.os.Bundle
import com.example.testjetpack.MainApplicationContract.DEFAULT_UI_DELAY
import com.example.testjetpack.R
import com.example.testjetpack.databinding.ActivitySplashBinding
import com.example.testjetpack.ui.base.BaseActivity
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.ui.main.MainActivity
import com.example.testjetpack.ui.signin.SignInActivity
import kotlinx.android.synthetic.main.activity_splash.*
import kotlin.reflect.KClass


class SplashActivity : BaseActivity<ActivitySplashBinding, SplashActivityVM>() {
    override val viewModelClass: KClass<SplashActivityVM> = SplashActivityVM::class
    override val layoutId: Int = R.layout.activity_splash
    override val navControllerId: Int = 0
    override val observeLiveData: SplashActivityVM.() -> Unit
        get() = {
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
    }

    override fun onStart() {
        super.onStart()
        viewModel.checkSingInState()
        animateDots()
    }

    private fun animateDots() {
        with(motionLayout) {
            transitionToEnd()
            postDelayed(
                {
                    transitionToStart()
                    postDelayed(::animateDots, transitionTimeMs + DEFAULT_UI_DELAY)
                },
                transitionTimeMs + DEFAULT_UI_DELAY
            )
        }
    }

    private fun openSingIn() {
        startActivity(Intent(this, SignInActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        })
        finish()
    }

    private fun openMain() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        })
        finish()
    }

    // region VM events renderer
    private val openSingInRenderer: (Any) -> Unit = { event ->
        event as SplashActivityVMEventStateChange.OpenSingIn
        openSingIn()
    }

    private val openMainRenderer: (Any) -> Unit = { event ->
        event as SplashActivityVMEventStateChange.OpenMain
        openMain()
    }

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
        SplashActivityVMEventStateChange.OpenSingIn::class to openSingInRenderer,
        SplashActivityVMEventStateChange.OpenMain::class to openMainRenderer
    )
    // endregion VM events renderer
}
