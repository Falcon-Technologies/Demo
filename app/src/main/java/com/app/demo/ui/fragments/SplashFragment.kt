package com.app.demo.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.app.demo.R
import com.app.demo.databinding.FragmentSplashBinding
import com.app.demo.ui.fragments.base.BaseFragment


class SplashFragment : BaseFragment<FragmentSplashBinding>() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {


        return getPersistentView(inflater, container, R.layout.fragment_splash)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val splashAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_splash)
        binding.tvAppName.animation = splashAnimation

        splashAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // "Add the code that you want to execute when animation starts")
            }

            override fun onAnimationEnd(animation: Animation?) {
                // "Add the code that you want to execute when animation ends")

                Handler(Looper.getMainLooper()).postDelayed({
                   findNavController().navigate(R.id.action_splashFragment_to_profileFragment)
                }, 1000)
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // "Add the code that you want to execute when animation repeats")
            }
        })

    }
}