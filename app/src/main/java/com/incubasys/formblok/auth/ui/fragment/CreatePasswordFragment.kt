package com.incubasys.formblok.auth.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.incubasys.formblok.R
import com.incubasys.formblok.auth.callback.CreatePasswordFragmentCallback
import com.incubasys.formblok.auth.ui.activity.AuthActivity
import com.incubasys.formblok.auth.ui.activity.GotoFragment
import com.incubasys.formblok.common.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_create_password.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CreatePasswordFragment : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var callback: CreatePasswordFragmentCallback
    private lateinit var binding: com.incubasys.formblok.databinding.FragmentCreatePasswordBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CreatePasswordFragmentCallback) {
            callback = context
        } else {
            throw RuntimeException("$context must implement CreatePasswordFragmentCallback")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_password, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callback.onCreatePasswordFragmentReady()
        binding.vm = (activity as AuthActivity).viewModel

        ivCreatePasswordNext.setOnClickListener { callback.onNextButtonClicked(GotoFragment.USER_TYPE) }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreatePasswordFragment()
    }
}
