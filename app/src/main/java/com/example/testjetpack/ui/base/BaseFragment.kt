package com.example.testjetpack.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ListPopupWindow
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.testjetpack.MainApplicationContract
import com.example.testjetpack.R
import com.example.testjetpack.utils.livedata.EventObserver
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.*
import retrofit2.HttpException
import timber.log.Timber
import kotlin.reflect.KClass

abstract class BaseFragment<B : ViewDataBinding, T : BaseViewModel<out EventStateChange>> : DaggerFragment() {
    companion object {
        private const val COROUTINE_DELAY = 1000L
    }

    var popupWindow: ListPopupWindow? = null

    abstract val name: String
    protected lateinit var binding: B
    abstract val viewModelClass: Class<T>
    protected val viewModel: T by lazy(LazyThreadSafetyMode.NONE) { ViewModelProviders.of(this).get(viewModelClass) }

    private var canBeClicked = true
    private val coroutines = mutableListOf<Deferred<*>>()

    @Suppress("MemberVisibilityCanPrivate")
    protected fun addCoroutine(coroutine: Deferred<*>) {
        coroutines.add(coroutine)
    }

    protected abstract val layoutId: Int

    protected open fun observeBaseLiveData() = with(viewModel) {

        alertMessageLiveData.observe(this@BaseFragment, Observer {
            it?.let { throwable ->
                parseError(throwable)
                throwable.message?.let { message -> this@BaseFragment.showAlert(message) }
                alertMessageLiveData.value = null
            }
        })

        showProgressLiveData.observe(this@BaseFragment, Observer {
            it?.let { showProgress ->
                if (showProgress) this@BaseFragment.showProgress(getString(R.string.loadingWithThreeDots))
                else this@BaseFragment.hideProgress()
            }
        })

        events.observe(this@BaseFragment, EventObserver(this@BaseFragment::render))

        observeLiveData()
    }

    fun parseError(it: Throwable) {
        if (it is HttpException) {
            when ((it).code()) {
//                HttpURLConnection.HTTP_BAD_REQUEST -> context?.toast("Error: ${it.message()}")
//                HttpURLConnection.HTTP_UNAUTHORIZED -> startActivity(Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
//                HttpURLConnection.HTTP_FORBIDDEN -> startActivity(Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
//                HttpURLConnection.HTTP_NOT_FOUND -> ServerErrorDialog().showDialog(this)
//                HttpURLConnection.HTTP_INTERNAL_ERROR -> ServerErrorDialog().showDialog(this)
//                else -> ServerErrorDialog().showDialog(this)
            }
        } else {
            Timber.e("Error: $it")
//            ServerErrorDialog().showDialog(this)
        }
    }

    protected abstract val observeLiveData: T.() -> Unit

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeBaseLiveData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        canBeClicked = true
    }

    override fun onDestroy() {
        coroutines.forEach { it.cancel() }
        coroutines.clear()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        getActionBar()?.let { it.title = name }
        with(viewModel.alertMessageLiveData) {
            value?.let { throwable ->
                parseError(throwable)
                throwable.message?.let { this@BaseFragment.showAlert(it) }
                value = null
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showAlert(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun showProgress(text: String?) {
        //todo
    }

    fun hideProgress() {
        //todo
    }

    protected fun processActionWithDelay(
        delay: Long = MainApplicationContract.DEFAULT_UI_DELAY,
        action: () -> Unit
    ) {
        addCoroutine(GlobalScope.async(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                delay(delay)
            }
            if (isActive) {
                action.invoke()
            }
        })
    }

    protected inline fun <reified T> bindInterfaceOrThrow(vararg objects: Any?): T =
        objects.find { it is T }?.let { it as T }
            ?: throw NotImplementedInterfaceException(T::class.java)


    protected fun invokeIfCanAccepted(withDebounce: Boolean = false, invoke: () -> Unit) {
        if (canBeClicked) {
            if (withDebounce) debounceClick()
            invoke()
        }
    }

    // This is something like debounce in rxBinding, but better :)
    private fun debounceClick() {
        addCoroutine(GlobalScope.async(Dispatchers.Main) {
            canBeClicked = false
            withContext(Dispatchers.Default) {
                delay(COROUTINE_DELAY)
            }
            if (isActive) {
                canBeClicked = true
            }
        })
    }

    protected fun getActionBar() = (activity as? BaseActivity<*, *>)?.supportActionBar

    protected fun onBackPressed() = invokeIfCanAccepted { activity?.onBackPressed() }

    open fun onViewPagerSelect() {
        // override it if you want use BaseViewPagerAdapter
    }

    private fun render(stateChangeEvent: EventStateChange) {
        RENDERERS[stateChangeEvent::class]?.invoke(stateChangeEvent)
    }

    protected abstract val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>>
}