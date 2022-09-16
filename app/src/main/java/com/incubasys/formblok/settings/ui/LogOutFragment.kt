package com.incubasys.formblok.settings.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.incubasys.formblok.R
import com.incubasys.formblok.settings.callback.LogOutFragmentCallback
import kotlinx.android.synthetic.main.fragment_log_out_dialog.*


class LogOutFragment : BottomSheetDialogFragment() {
    private var mListener: LogOutFragmentCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_log_out_dialog, container, false)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        mListener = if (parent != null) {
            parent as LogOutFragmentCallback
        } else {
            context as LogOutFragmentCallback
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bLogoutAccept.setOnClickListener {
            mListener?.onLogoutButtonClicked()
        }
        bLogoutCancel.setOnClickListener {
            mListener?.onCancelButtonClicked()
        }
    }

    override fun onDetach() {
        mListener = null
        super.onDetach()
    }

    companion object {

        fun newInstance(): LogOutFragment =
            LogOutFragment().apply {
                arguments = Bundle().apply {

                }
            }

    }
}
