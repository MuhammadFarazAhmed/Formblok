package com.incubasys.formblok.terms.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.incubasys.formblok.R
import com.incubasys.formblok.common.extenstions.spannedPrivacyText
import com.incubasys.formblok.terms.callback.PrivacyPolicyFragmentCallback
import kotlinx.android.synthetic.main.fragment_privacy_policy.*


class PrivacyPolicyDialogFragment : BottomSheetDialogFragment() {
    private var callback: PrivacyPolicyFragmentCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_privacy_policy_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        callback = if (parent != null) {
            parent as PrivacyPolicyFragmentCallback
        } else {
            context as PrivacyPolicyFragmentCallback
        }
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
            callback?.onPrivacyPolicyClicked(1)
        }

        tvPrivacyTermslabel.setOnClickListener {
            callback?.onTermsConditionClicked(2)
        }

        bPrivacyAccept.setOnClickListener {
            callback?.onAcceptButtonClicked()
        }
    }

    override fun onDetach() {
        super.onDetach()
        activity?.finish()
    }

    companion object {
        fun newInstance(): PrivacyPolicyDialogFragment =
            PrivacyPolicyDialogFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
