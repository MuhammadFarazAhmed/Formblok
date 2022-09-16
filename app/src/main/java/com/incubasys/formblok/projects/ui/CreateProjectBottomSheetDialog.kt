package com.incubasys.formblok.projects.ui


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.incubasys.formblok.R
import com.incubasys.formblok.projects.callback.CreateProjectBottomSheetCallback
import com.incubasys.formblok.settings.callback.LogOutFragmentCallback
import kotlinx.android.synthetic.main.fragment_create_project_bottom_sheet_dialog.*


class CreateProjectBottomSheetDialog : BottomSheetDialogFragment() {

    private var callback: CreateProjectBottomSheetCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        callback = if (parent != null) {
            parent as CreateProjectBottomSheetCallback
        } else {
            context as CreateProjectBottomSheetCallback
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_project_bottom_sheet_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bProjectConfirm.setOnClickListener {
            callback?.onConfirmButtonClicked()
        }
        bProjectCancel.setOnClickListener {
            dismiss()
            callback?.onCancelButtonClicked()
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            CreateProjectBottomSheetDialog().apply {

            }
    }
}
