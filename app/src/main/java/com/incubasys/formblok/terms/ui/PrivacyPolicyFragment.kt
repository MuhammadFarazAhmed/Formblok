package com.incubasys.formblok.terms.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.incubasys.formblok.R
import com.incubasys.formblok.terms.callback.PrivacyPolicyFragmentCallback
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.common.extenstions.spannedPrivacyText
import kotlinx.android.synthetic.main.fragment_privacy_policy.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PrivacyPolicyFragment : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var callback: PrivacyPolicyFragmentCallback


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is PrivacyPolicyFragmentCallback) {
            callback = context
        } else {
            throw RuntimeException("$context must implement PrivacyPolicyFragmentCallback")
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
        return inflater.inflate(R.layout.fragment_privacy_policy, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        context?.let {
            tvPrivacySubHeading.text = spannedPrivacyText(
                it,
                getString(R.string.privacy_sub_heading),
                getString(R.string.privacy_policy),
                getString(R.string.terms_amp_conditions)
            )
        }

        tvPrivacyLabel.setOnClickListener {
            callback.onPrivacyPolicyClicked(1)
        }

        tvPrivacyTermslabel.setOnClickListener {
            callback.onTermsConditionClicked(2)
        }

        bPrivacyAccept.setOnClickListener {
            callback.onAcceptButtonClicked()
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            PrivacyPolicyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
