package com.incubasys.formblok.auth.ui.fragment


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.incubasys.formblok.R
import com.incubasys.formblok.auth.callback.EmailFragmentCallback
import com.incubasys.formblok.auth.ui.activity.AuthActivity
import com.incubasys.formblok.auth.ui.activity.GotoFragment
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.databinding.FragmentEmailBinding
import kotlinx.android.synthetic.main.fragment_email.*


class EmailFragment : BaseFragment() {

    private lateinit var callback: EmailFragmentCallback
    private lateinit var binding: FragmentEmailBinding

    companion object {
        fun newInstance(): EmailFragment =
            EmailFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is EmailFragmentCallback) callback = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_email, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callback.onEmailFragmentReady()
        binding.vm = (activity as AuthActivity).viewModel
        (activity as AuthActivity).viewModel.message.observe(this, Observer {
            when {
                it.code == 403 -> //Email Exists
                    ivEmailNext.setOnClickListener { callback.onNextButtonClicked(GotoFragment.PASSWORD) }
                it.code == 200
                -> //Start sign Up Process
                    ivEmailNext.setOnClickListener { callback.onNextButtonClicked(GotoFragment.CREATE_PASSWORD) }
                else -> {

                }
            }
        })

    }

}
