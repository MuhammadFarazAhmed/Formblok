package com.incubasys.formblok.auth.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.incubasys.formblok.R
import com.incubasys.formblok.auth.callback.UserTypeFragmentCallback
import com.incubasys.formblok.auth.ui.activity.AuthActivity
import com.incubasys.formblok.auth.ui.activity.GotoFragment
import com.incubasys.formblok.common.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_user_type.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class UserTypeFragment : BaseFragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var callback: UserTypeFragmentCallback
    private lateinit var binding: com.incubasys.formblok.databinding.FragmentUserTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is UserTypeFragmentCallback) {
            callback = context
        } else {
            throw RuntimeException("$context must implement UserTypeFragmentCallback")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_type, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callback.onUserTypeFragmentReady()
        binding.vm = (activity as AuthActivity).viewModel


        ivBuyerIcon.setOnClickListener {
            if (it.isSelected) {
                it.isSelected = false
                (activity as AuthActivity).viewModel.signUpInput.isBuyer = false
            } else {
                it.isSelected = true
                (activity as AuthActivity).viewModel.signUpInput.isBuyer = true
            }
        }

        ivAgentIcon.setOnClickListener {
            if (it.isSelected) {
                it.isSelected = false
                (activity as AuthActivity).viewModel.signUpInput.isAgent = false
            } else {
                it.isSelected = true
                (activity as AuthActivity).viewModel.signUpInput.isAgent = true
            }
        }


        ivUserTypeNext.setOnClickListener { callback.onNextButtonClicked(GotoFragment.NAME) }

    }


    companion object {
        @JvmStatic
        fun newInstance() =
            UserTypeFragment()
    }
}
