package com.example.testjetpack.ui.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.testjetpack.utils.UiUtils.hideKeyboard
import dagger.android.support.DaggerAppCompatActivity
import retrofit2.HttpException
import timber.log.Timber

abstract class BaseActivity<T : BaseViewModel> : DaggerAppCompatActivity() {
    abstract val viewModelClass: Class<T>
    protected val viewModel: T by lazy(LazyThreadSafetyMode.NONE) { ViewModelProviders.of(this).get(viewModelClass) }
    protected abstract val layoutId: Int
    protected abstract val containerId: Int
    protected abstract val observeLiveData: T.() -> Unit


    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        observeBaseLiveData()
    }

    override fun onResume() {
        super.onResume()
        with(viewModel.alertMessageLiveData) {
            value?.let {
                this@BaseActivity.parseError(it)
                value = null
            }
        }
    }

    fun parseError(it: Throwable) {
        var message = it.localizedMessage
        if (it is HttpException) {
            when ((it).code()) {
//                HttpURLConnection.HTTP_BAD_REQUEST -> toast("Error: ${it.message()}")
//                HttpURLConnection.HTTP_UNAUTHORIZED -> startActivity(Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
//                HttpURLConnection.HTTP_FORBIDDEN -> startActivity(Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
//                HttpURLConnection.HTTP_NOT_FOUND -> ServerErrorDialog().showDialog(this)
//                HttpURLConnection.HTTP_INTERNAL_ERROR -> ServerErrorDialog().showDialog(this)
//                else -> ServerErrorDialog().showDialog(this)
            }
        } else {
            Timber.e("Error: ${it.localizedMessage}")
//            ServerErrorDialog().showDialog(this)
        }

        showAlert(message)
    }

    private fun observeBaseLiveData() = with(viewModel) {
        showProgressLiveData.observe(this@BaseActivity, Observer { showProgress ->
            showProgress?.let { if (it) showProgress() else hideProgress() }
        })
        alertMessageLiveData.observe(this@BaseActivity, Observer { alertMessage ->
            alertMessage?.let {
                parseError(it)
                alertMessageLiveData.value = null
            }
        })

        observeLiveData()
    }

    /**
     * Replace fragment with tag equals to it's class name {@link Class#getName()}
     *
     * @param fragment             Instance of {@link Fragment}
     * @param needToAddToBackStack boolean value representing necessity for adding fragment to backstack.
     *                             If true fragment will be added to backstack with tag equals
     *                             to it's class name }
     */
    protected fun <T : Fragment> replaceFragment(fragment: T, needToAddToBackStack: Boolean = true): T {
        currentFocus?.let { hideKeyboard(it) }
        val name = fragment.javaClass.name
        with(supportFragmentManager.beginTransaction()) {
            replace(containerId, fragment, name)
            if (needToAddToBackStack) {
                addToBackStack(name)
            }
            commit()
        }
        supportFragmentManager.executePendingTransactions()
        return fragment
    }
    fun showProgress() {
        // nothing
    }

    fun hideProgress() {
        // nothing
    }

    fun showAlert(text: String) {
        Toast.makeText(this@BaseActivity, text, Toast.LENGTH_SHORT).show()
    }
}