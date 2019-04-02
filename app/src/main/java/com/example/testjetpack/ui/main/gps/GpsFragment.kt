package com.example.testjetpack.ui.main.gps


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testjetpack.R
import com.example.testjetpack.databinding.FragmentGpsBinding
import com.example.testjetpack.ui.base.BaseFragment
import com.example.testjetpack.ui.base.EventStateChange
import kotlin.reflect.KClass

class GpsFragment : BaseFragment<FragmentGpsBinding, GpsFragmentVM>() {
    override val name: String = "GPS"
    override val viewModelClass: Class<GpsFragmentVM> = GpsFragmentVM::class.java
    override val layoutId: Int = R.layout.fragment_gps
    override val observeLiveData: GpsFragmentVM.() -> Unit
        get() = {
        }

    private var callback: IGpsFragmentCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = bindInterfaceOrThrow<IGpsFragmentCallback>(parentFragment, context)
    }

    override fun onDetach() {
        callback = null
        super.onDetach()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = viewModel
        return view
    }

    // region VM events renderer

    override val RENDERERS: Map<KClass<out EventStateChange>, Function1<Any, Unit>> = mapOf(
    )
    // endregion VM events renderer

    companion object {
        @JvmStatic
        fun newInstance() = GpsFragment()
    }
}
