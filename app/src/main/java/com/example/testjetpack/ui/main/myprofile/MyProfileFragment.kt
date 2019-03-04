package com.example.testjetpack.ui.main.myprofile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testjetpack.R
import com.example.testjetpack.databinding.MyProfileFragmentBinding
import com.example.testjetpack.ui.base.BaseFragment

class MyProfileFragment : BaseFragment<MyProfileFragmentBinding, MyProfileFragmentVM>() {
    override val name: String = "My Profile"
    override val viewModelClass: Class<MyProfileFragmentVM> = MyProfileFragmentVM::class.java
    override val layoutId: Int = R.layout.my_profile_fragment
    override val observeLiveData: MyProfileFragmentVM.() -> Unit
        get() = {

        }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = viewModel
        return view
    }

    override fun onStart() {
        super.onStart()
        viewModel.getProfile()
    }


    companion object {
        @JvmStatic
        fun newInstance() = MyProfileFragment()
    }
}
