package com.example.testjetpack.ui.signin

import android.content.Intent
import android.os.Bundle
import com.example.testjetpack.R
import com.example.testjetpack.databinding.ActivitySignInBinding
import com.example.testjetpack.ui.base.BaseActivity
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.ui.main.MainActivity
import com.example.testjetpack.ui.signin.signin.ISignInFragmentCallback
import com.example.testjetpack.utils.showNotImplemented
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlin.reflect.KClass

class SignInActivity : BaseActivity<ActivitySignInBinding, SignInActivityVM>(),
    ISignInFragmentCallback {

    override val viewModelClass: KClass<SignInActivityVM> = SignInActivityVM::class
    override val layoutId: Int = R.layout.activity_sign_in
    override val navControllerId: Int = R.id.nav_host_fragment
    override val observeLiveData: SignInActivityVM.() -> Unit
        get() = {
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
    }

    override fun openMain() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        })
        finish()
    }

    override fun openPasswordRecovery() {
        showNotImplemented()
    }

    override fun openRegistration() {
        showNotImplemented()
    }

    // region VM events renderer

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(

    )
    // endregion VM events renderer
}
