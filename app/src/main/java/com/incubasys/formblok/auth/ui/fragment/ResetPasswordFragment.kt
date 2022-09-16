package com.incubasys.formblok.auth.ui.fragment


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.incubasys.formblok.R
import com.incubasys.formblok.auth.callback.ResetPasswordCallback
import com.incubasys.formblok.auth.ui.activity.AuthActivity
import com.incubasys.formblok.common.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_reset_password.*

class ResetPasswordFragment : BaseFragment() {

    private lateinit var callback: ResetPasswordCallback
    private lateinit var binding: com.incubasys.formblok.databinding.FragmentResetPasswordBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ResetPasswordCallback) callback = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reset_password, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callback.onResetPasswordFragmentCallback()
        binding.vm = (activity as AuthActivity).viewModel
        ivResetPasswordNext.setOnClickListener {
            (activity as AuthActivity).viewModel.resetPassword()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ResetPasswordFragment()

    }
}
