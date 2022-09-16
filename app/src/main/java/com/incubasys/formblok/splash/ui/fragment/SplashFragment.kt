package com.incubasys.formblok.splash.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.incubasys.formblok.R
import com.incubasys.formblok.common.data.model.Message
import com.incubasys.formblok.common.data.remote.ApiStatus
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.databinding.SplashFragmentBinding
import com.incubasys.formblok.splash.callback.SplashFragmentCallback
import com.incubasys.formblok.splash.viewmodel.SplashViewModel
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.splash_fragment.*
import javax.inject.Inject

class SplashFragment : BaseFragment() {


    private lateinit var splashCallback: SplashFragmentCallback
    private lateinit var binding: SplashFragmentBinding

    companion object {
        fun newInstance() = SplashFragment()
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: SplashViewModel by lazy {
        ViewModelProviders.of(this, factory).get(SplashViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SplashFragmentCallback) splashCallback = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.splash_fragment, container, false)
        return binding.root
    }

    private fun showError(message: Message) {
        if (view != null) {
            Snackbar.make(flSplashFragmentContainer, message.message, Snackbar.LENGTH_INDEFINITE)
                .setAction(
                    R.string.retry
                ) { viewModel.validateSession() }.show()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (viewModel.isLoggedIn()) {
            viewModel.validateSession()
            viewModel.sessionOutput.observe(this, Observer {
                when (it.status) {
                    ApiStatus.SUCCESS -> sendCallback()
                    ApiStatus.ERROR -> {
                        if (it.error?.code == 401) {
                            viewModel.removeUserLoginInfo()
                            splashCallback.startAuthActivity()
                        }
                        it.error?.let { it1 -> showError(it1) }
                    }
                    else -> {

                    }
                }
            })
        } else {
            Handler().postDelayed({
                sendCallback()
            }, 2000)
        }

    }

    private fun sendCallback() {
        splashCallback.startHomeActivity(viewModel.isOnboardingShown(), viewModel.isLoggedIn())
    }

}
