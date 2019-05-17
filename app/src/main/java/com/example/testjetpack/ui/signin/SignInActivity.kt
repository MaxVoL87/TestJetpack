package com.example.testjetpack.ui.signin

import android.os.Bundle
import com.example.testjetpack.R
import com.example.testjetpack.databinding.ActivitySignInBinding
import com.example.testjetpack.ui.base.BaseActivity
import com.example.testjetpack.ui.base.EventStateChange
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlin.reflect.KClass

class SignInActivity : BaseActivity<ActivitySignInBinding, SignInActivityVM>() {
    override val viewModelClass: KClass<SignInActivityVM> = SignInActivityVM::class
    override val layoutId: Int = R.layout.activity_sign_in
    override val navControllerId: Int = R.id.nav_host_fragment
    override val observeLiveData: SignInActivityVM.() -> Unit
        get() = {
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        setSupportActionBar(cToolbar)
    }

    // region VM events renderer

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(

    )
    // endregion VM events renderer
}
