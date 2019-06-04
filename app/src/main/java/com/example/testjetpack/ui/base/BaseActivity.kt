package com.example.testjetpack.ui.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.example.testjetpack.utils.livedata.EventObserver
import retrofit2.HttpException
import timber.log.Timber
import kotlin.reflect.KClass
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.testjetpack.R
import com.example.testjetpack.ui.dialog.message.IMessageDialogFragmentCallback
import com.example.testjetpack.ui.dialog.message.MessageDialogFragment
import com.example.testjetpack.ui.dialog.progress.IProgressDialogFragmentCallback
import com.example.testjetpack.ui.dialog.progress.ProgressDialogFragment
import com.example.testjetpack.utils.autoCleared
import com.example.testjetpack.utils.withNotNull
import org.koin.android.viewmodel.ext.android.getViewModel


abstract class BaseActivity<B : ViewDataBinding, M : BaseViewModel<out EventStateChange>> : AppCompatActivity(),
    IMessageDialogFragmentCallback,
    IProgressDialogFragmentCallback,
    ICallback {
    protected abstract val layoutId: Int
    protected abstract val navControllerId: Int
    protected abstract val viewModelClass: KClass<M>
    protected var binding: B by autoCleared()
    protected val navController: NavController
            by lazy(LazyThreadSafetyMode.NONE) { Navigation.findNavController(this, navControllerId) }
    protected val viewModel: M by lazy(LazyThreadSafetyMode.NONE) { getViewModel(viewModelClass) }
    protected abstract val observeLiveData: M.() -> Unit

    private var _progressDialog: ProgressDialogFragment? = null
    private var _progressDialogsCount: Int = 0
        get() {
            if (field < 0) field = 0
            return field
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this

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

    override fun onDestroy() {
        _progressDialogsCount = 0
        hideProgress()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if ((getCurFragment(navControllerId) as? IOnBackPressed)?.onBackPressed() != true) {
            super.onBackPressed()
        }
    }

    private fun parseError(it: Throwable) {
        //todo:
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
            showProgress?.let { if (it) this@BaseActivity.showProgress(getString(R.string.loadingWithThreeDots)) else this@BaseActivity.hideProgress() }
        })
        alertMessageLiveData.observe(this@BaseActivity, Observer { alertMessage ->
            alertMessage?.let {
                this@BaseActivity.parseError(it)
                alertMessageLiveData.value = null
            }
        })

        events.observe(this@BaseActivity, EventObserver(this@BaseActivity::render))

        observeLiveData()
    }

    override fun showProgress(text: String?) {
        _progressDialogsCount++
        val inShow = _progressDialog != null
        if (!inShow) _progressDialog = ProgressDialogFragment()
        withNotNull(_progressDialog) {
            setText(text)
            setPBVisible(true)
            setIsCancelable(false)
            if (!isAdded && !inShow) show(supportFragmentManager, ProgressDialogFragment::class.java.name)
        }
    }

    override fun hideProgress() {
        _progressDialogsCount--
        withNotNull(_progressDialog) {
            if (_progressDialogsCount == 0) {
                if (isAdded) dismiss()
                _progressDialog = null
            }
        }
    }

    fun showAlert(text: String) {
        Toast.makeText(this@BaseActivity, text, Toast.LENGTH_SHORT).show()
    }

    fun showMessage(text: String, header: String? = null, isCancelable: Boolean = true) {
        withNotNull(MessageDialogFragment()) {
            setText(text)
            setHeader(header)
            setIsCancelable(isCancelable)
            show(supportFragmentManager, MessageDialogFragment::class.java.name)
        }
    }

    fun getCurFragment(@IdRes containerId: Int) = supportFragmentManager.findFragmentById(containerId)?.let { getCurFragment(it) }

    fun getCurFragment(navHostFragment: Fragment): Fragment = navHostFragment.childFragmentManager.fragments[0]

    private fun render(stateChangeEvent: EventStateChange) {
        RENDERERS[stateChangeEvent::class]?.invoke(stateChangeEvent)
    }

    protected abstract val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>>

    //    protected fun tryToOpenUri(uri: String?) {
//        try {
//            withNotNull(uri?.toUri()) { inAppCallBack?.checkAndOpenUri(this) }
//        } catch (exception: Exception) {
//            Timber.e("parse_uri $exception")
//        }
//    }
}