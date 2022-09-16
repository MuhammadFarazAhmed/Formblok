package com.incubasys.formblok.profile.ui


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.incubasys.formblok.R
import com.incubasys.formblok.common.extenstions.spannedHeading
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.databinding.FragmentEditEmailBinding
import com.incubasys.formblok.profile.data.model.Profile
import com.incubasys.formblok.profile.callback.EditEmailFragmentCallback
import kotlinx.android.synthetic.main.fragment_edit_email.*


class EditEmailFragment : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentEditEmailBinding
    private lateinit var callback: EditEmailFragmentCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is EditEmailFragmentCallback) {
            callback = context
        } else {
            throw RuntimeException("$context must implement EditEmailFragmentCallback")
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_email, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvEditEmailFragmentHeading.text =
            spannedHeading(
                (activity as EditProfileActivity),
                getString(R.string.what_is_your_email),
                getString(R.string.email_address)
            )
        binding.vm = (activity as EditProfileActivity).viewModel
        (activity as EditProfileActivity).viewModel.checkEditEmailValidation()

        ivEditEmailBackArrow.setOnClickListener {
            callback.onFragmentBackPressed()
        }

        ivEditEmailNext.setOnClickListener {
            (activity as EditProfileActivity).viewModel.list[1] =
                Profile(
                    id = 2,
                    text = (activity as EditProfileActivity).viewModel.profileInput.email
                )
            callback.onNextIconClicked()
        }
    }


    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance() =
            EditEmailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
