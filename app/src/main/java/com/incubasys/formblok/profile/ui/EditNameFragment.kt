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
import com.incubasys.formblok.profile.data.model.Profile
import com.incubasys.formblok.profile.callback.EditNameFragmentCallback
import kotlinx.android.synthetic.main.fragment_edit_name.*


class EditNameFragment : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: com.incubasys.formblok.databinding.FragmentEditNameBinding

    private lateinit var callback: EditNameFragmentCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is EditNameFragmentCallback) {
            callback = context
        } else {
            throw RuntimeException("$context must implement EditNameFragmentCallback")
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_name, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvEditNameFragmentHeading.text =
            spannedHeading(
                (activity as EditProfileActivity),
                getString(R.string.what_is_your_name),
                getString(R.string.name)
            )
        binding.vm = (activity as EditProfileActivity).viewModel
        (activity as EditProfileActivity).viewModel.checkEditNameValidation()

        ivEditNameBackArrow.setOnClickListener {
            callback.onFragmentBackPressed()
        }

        ivEditNameNext.setOnClickListener {
            (activity as EditProfileActivity).viewModel.list[0] = Profile(
                id = 1,
                text = (activity as EditProfileActivity).viewModel.profileInput.name
            )
            callback.onNextIconClicked()
        }
    }


    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        @JvmStatic
        fun newInstance() =
            EditNameFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
