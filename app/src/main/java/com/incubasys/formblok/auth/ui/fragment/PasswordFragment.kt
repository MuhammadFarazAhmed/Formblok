package com.incubasys.formblok.auth.ui.fragment


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.incubasys.formblok.R
import com.incubasys.formblok.auth.callback.PasswordFragmentCallback
import com.incubasys.formblok.auth.ui.activity.AuthActivity
import com.incubasys.formblok.auth.ui.activity.GotoFragment
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.databinding.FragmentPasswordBinding
import kotlinx.android.synthetic.main.fragment_password.*


class PasswordFragment : BaseFragment() {

    private lateinit var callback: PasswordFragmentCallback
    private lateinit var binding: FragmentPasswordBinding

    companion object {
        @JvmStatic
        fun newInstance() =
            PasswordFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is PasswordFragmentCallback) callback = context
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_password, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callback.onPasswordFragmentReady()
        binding.vm = (activity as AuthActivity).viewModel


        ivPasswordNext.setOnClickListener { callback.onNextButtonClicked(GotoFragment.LOGIN) }

        tvForgotPasswordLabel.setOnClickListener { callback.onNextButtonClicked(GotoFragment.FORGOT) }

    }

}
