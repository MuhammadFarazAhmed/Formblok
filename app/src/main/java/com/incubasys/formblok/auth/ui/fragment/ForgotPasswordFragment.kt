package com.incubasys.formblok.auth.ui.fragment


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.incubasys.formblok.auth.callback.ForgotPasswordCallback
import com.incubasys.formblok.auth.ui.activity.AuthActivity
import com.incubasys.formblok.auth.ui.activity.GotoFragment
import com.incubasys.formblok.common.data.remote.ApiStatus
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.databinding.FragmentForgotBinding
import kotlinx.android.synthetic.main.fragment_forgot.*


class ForgotFragment : BaseFragment() {

    private lateinit var callback: ForgotPasswordCallback
    private lateinit var binding: FragmentForgotBinding

    companion object {
        @JvmStatic
        fun newInstance() =
            ForgotFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ForgotPasswordCallback) callback = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.vm = (activity as AuthActivity).viewModel
        callback.onForgotPasswordFragmentReady()

        ivForgotNext.setOnClickListener {
            (activity as AuthActivity).viewModel.requestForgotPassword()
        }

    }

}
