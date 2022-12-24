package com.app.demo.ui.fragments.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.app.demo.ApplicationClass
import com.app.demo.viewModel.OwnerViewModel


open class BaseFragment<T : ViewDataBinding> : Fragment() {

    var hasInitializedRootView = false
    private var rootView: View? = null
    lateinit var binding: T

    val ownerViewModel: OwnerViewModel by viewModels {
        OwnerViewModel.OwnerViewModelFactory(ApplicationClass.getContext().ownerRepository)
    }

    fun getPersistentView(inflater: LayoutInflater, container: ViewGroup?, layout: Int): View? {
        if (rootView == null) {
            binding = DataBindingUtil.inflate(inflater, layout, container, false)
            rootView = binding.root
        } else {
            (rootView?.parent as? ViewGroup)?.removeView(rootView)
        }

        return rootView
    }

    fun showMessage(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }
}