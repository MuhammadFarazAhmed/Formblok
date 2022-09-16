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
import com.incubasys.formblok.settings.callback.ChangeNewPasswordFragmentCallback
import kotlinx.android.synthetic.main.fragment_change_new_password.*

class ChangeNewPasswordFragment : BaseFragment() {


    private lateinit var binding: com.incubasys.formblok.databinding.FragmentChangeNewPasswordBinding
    private lateinit var callback: ChangeNewPasswordFragmentCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ChangeNewPasswordFragmentCallback) {
            callback = context
        } else {
            throw RuntimeException("$context must implement ChangeNewPasswordFragmentCallback")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_new_password, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvNewPasswordFragmentHeading.text =
            spannedHeadingReverse(
                (activity as SettingsActivity),
                getString(R.string.change_new_heading),
                getString(R.string.change_new_password_sub_heading)
            ,"Change password")
        binding.vm = (activity as SettingsActivity).viewModel
        (activity as SettingsActivity).viewModel.checkNewPasswordValidation()

        ivNewPasswordBackArrow.setOnClickListener {
            callback.onFragmentBackPressed()
        }

        ivNewPasswordNext.setOnClickListener {
            callback.onChangeNewPasswordNextClicked()
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            ChangeNewPasswordFragment()
    }
}
