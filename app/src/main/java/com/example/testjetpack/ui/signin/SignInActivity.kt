package com.example.testjetpack.ui.signin

import android.content.Intent
import android.os.Bundle
import androidx.navigation.ui.NavigationUI
import com.example.testjetpack.R
import com.example.testjetpack.databinding.ActivitySignInBinding
import com.example.testjetpack.ui.base.BaseActivity
import com.example.testjetpack.ui.base.EventStateChange
import com.example.testjetpack.ui.main.MainActivity
import com.example.testjetpack.ui.signin.passwordrecovery.IPasswordRecoveryFragmentCallback
import com.example.testjetpack.ui.signin.signin.ISignInFragmentCallback
import com.example.testjetpack.ui.signin.signup.ISignUpFragmentCallback
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlin.reflect.KClass

class SignInActivity : BaseActivity<ActivitySignInBinding, SignInActivityVM>(),
    ISignInFragmentCallback,
    IPasswordRecoveryFragmentCallback,
    ISignUpFragmentCallback {

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
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return true
    }

    override fun openMain() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        })
        finish()
    }

    override fun openSignIn() {
        navController.navigate(R.id.action_popupTo_signInFragment)
    }

    override fun openPasswordRecovery() {
        navController.navigate(R.id.action_signInFragment_to_passwordRecoveryFragment)
    }

    override fun openRegistration() {
        navController.navigate(R.id.action_signInFragment_to_signUpFragment)
    }

    // region VM events renderer

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(

    )
    // endregion VM events renderer
}
