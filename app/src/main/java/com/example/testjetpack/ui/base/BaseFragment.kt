package com.example.testjetpack.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListPopupWindow
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.testjetpack.MainApplicationContract
import com.example.testjetpack.R
import com.example.testjetpack.utils.livedata.EventObserver
import kotlinx.coroutines.*
import org.koin.android.viewmodel.ext.android.getViewModel
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

abstract class BaseFragment<B : ViewDataBinding, M : BaseViewModel<out EventStateChange>> : Fragment(), CoroutineScope {
    companion object {
        private const val COROUTINE_DELAY = 1000L
    }

    protected abstract val layoutId: Int
    protected abstract val viewModelClass: KClass<M>
    protected lateinit var binding: B
    protected val viewModel: M by lazy(LazyThreadSafetyMode.NONE) { getViewModel(viewModelClass) }

    var popupWindow: ListPopupWindow? = null

    private var canBeClicked = true

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var job: Job


    protected open fun observeBaseLiveData() = with(viewModel) {

        alertMessageLiveData.observe(this@BaseFragment, Observer {
            it?.let { throwable ->
                //todo: Main.parseError(throwable)
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

    protected abstract val observeLiveData: M.() -> Unit

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
        job = Job()
        setHasOptionsMenu(true)
        canBeClicked = true
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        with(viewModel.alertMessageLiveData) {
            value?.let { throwable ->
                //todo: Main.parseError(throwable)
                throwable.message?.let { this@BaseFragment.showAlert(it) }
                value = null
            }
        }
    }

    open fun showAlert(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    open fun showProgress(text: String?) {
        //todo
    }

    open fun hideProgress() {
        //todo
    }

    protected fun processActionWithDelay(
        delay: Long = MainApplicationContract.DEFAULT_UI_DELAY,
        action: () -> Unit
    ) {
        async {
            withContext(Dispatchers.IO) {
                delay(delay)
            }
            if (isActive) {
                action.invoke()
            }
        }
    }

    protected fun invokeIfCanAccepted(withDebounce: Boolean = false, invoke: () -> Unit) {
        if (canBeClicked) {
            if (withDebounce) debounceClick()
            invoke()
        }
    }

    // This is something like debounce in rxBinding, but better :)
    private fun debounceClick() {
        async {
            canBeClicked = false
            withContext(Dispatchers.Default) {
                delay(COROUTINE_DELAY)
            }
            if (isActive) {
                canBeClicked = true
            }
        }
    }

    protected fun invalidateOptionsMenu() = activity?.invalidateOptionsMenu()

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