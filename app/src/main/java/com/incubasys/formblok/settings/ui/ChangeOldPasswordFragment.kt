package com.incubasys.formblok.settings.ui


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.incubasys.formblok.R
import com.incubasys.formblok.common.extenstions.spannedHeading
import com.incubasys.formblok.common.extenstions.spannedHeadingReverse
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.databinding.FragmentChangeOldPasswordBinding
import com.incubasys.formblok.settings.callback.ChangeOldPasswordFragmentCallback
import kotlinx.android.synthetic.main.fragment_change_old_password.*

class ChangeOldPasswordFragment : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentChangeOldPasswordBinding
    private lateinit var callback: ChangeOldPasswordFragmentCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ChangeOldPasswordFragmentCallback) {
            callback = context
        } else {
            throw RuntimeException("$context must implement ChangeOldPasswordFragmentCallback")
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_old_password, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvOldPasswordFragmentHeading.text =
            spannedHeadingReverse(
                (activity as SettingsActivity),
                getString(R.string.change_old_heading),
                getString(R.string.change_old_password_sub_heading),
                "Change password"
            )
        binding.vm = (activity as SettingsActivity).viewModel
        (activity as SettingsActivity).viewModel.checkPasswordValidation()

        ivEditPasswordBackArrow.setOnClickListener {
            callback.onFragmentBackPressed()
        }

        ivEditPasswordNext.setOnClickListener {
            callback.onChangeOldPasswordNextClicked()
        }
    }


    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        @JvmStatic
        fun newInstance() =
            ChangeOldPasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
