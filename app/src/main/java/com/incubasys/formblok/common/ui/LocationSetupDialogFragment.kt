package com.incubasys.formblok.common.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.incubasys.formblok.R
import com.incubasys.formblok.common.callback.PermissionDialogCallback
import com.incubasys.formblok.common.viewmodel.PermissionViewModel
import com.incubasys.formblok.databinding.LocationSetupDialogFragmentBinding

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 * PermissionDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
</pre> *
 *
 * You activity (or fragment) needs to implement [LocationSetupDialogFragment.Listener].
 */
class LocationSetupDialogFragment : BottomSheetDialogFragment(), PermissionDialogCallback {
    private var messageType = 0

    private var mListener: Listener? = null
    private lateinit var binding: LocationSetupDialogFragmentBinding
    private var viewModel: PermissionViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            messageType = arguments!!.getInt(ARG_MESSAGE_TYPE, 0)
        }
        viewModel = ViewModelProviders.of(this).get(PermissionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.location_setup_dialog_fragment, container, false)
        binding.callback = this
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.setMessageType(messageType)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        return dialog

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        if (parent is Listener) {
            mListener = parent
        } else if (context is Listener) {
            mListener = context
        }
    }

    override fun onDetach() {
        mListener = null
        super.onDetach()
    }

    override fun onNegativeButtonClicked() {
        dismiss()
    }

    override fun onPositiveButtonClicked() {
        mListener!!.onPermissionDialogButtonClicked(messageType)
        dismiss()
    }

    interface Listener {
        fun onPermissionDialogButtonClicked(messageType: Int)
    }

    companion object {

        const val TYPE_ALLOW_LOCATION_PERMISSION_DIALOG = 1
        const val TYPE_ALLOW_LOCATION_PERMISSION_SETTING = 2
        const val TYPE_LOCATION_SETTINGS_ON = 3

        private const val ARG_MESSAGE_TYPE = "messageType"

        fun newInstance(messageType: Int): LocationSetupDialogFragment {
            val fragment = LocationSetupDialogFragment()
            val args = Bundle()
            args.putInt(ARG_MESSAGE_TYPE, messageType)
            fragment.arguments = args
            return fragment
        }
    }
}
