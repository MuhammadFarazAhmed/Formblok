package com.incubasys.formblok.profile.ui


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.incubasys.formblok.R
import com.incubasys.formblok.common.extenstions.loadDrawable
import com.incubasys.formblok.common.extenstions.spannedHeading
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.profile.data.model.Profile
import com.incubasys.formblok.profile.callback.EditGenderFragmentCallback
import kotlinx.android.synthetic.main.fragment_edit_gender.*


class EditGenderFragment : BaseFragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: com.incubasys.formblok.databinding.FragmentEditGenderBinding
    private lateinit var callback: EditGenderFragmentCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is EditGenderFragmentCallback) {
            callback = context
        } else {
            throw RuntimeException("$context must implement EditGenderFragmentCallback")
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_gender, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvEditGenderFragmentHeading.text =
            spannedHeading(
                (activity as EditProfileActivity),
                getString(R.string.what_is_your_gender),
                getString(R.string.gender)
            )

        binding.vm = (activity as EditProfileActivity).viewModel

        ivEditFemaleIcon.setOnClickListener {
            ivEditFemaleIcon.loadDrawable(
                ContextCompat.getDrawable(
                    activity as EditProfileActivity,
                    R.drawable.female_selected
                )!!
            )
            ivEditMaleIcon.loadDrawable(
                ContextCompat.getDrawable(
                    activity as EditProfileActivity,
                    R.drawable.male_unselected
                )!!
            )
            (activity as EditProfileActivity).viewModel.profileInput.gender = 0
        }

        ivEditMaleIcon.setOnClickListener {
            ivEditFemaleIcon.loadDrawable(
                ContextCompat.getDrawable(
                    activity as EditProfileActivity,
                    R.drawable.female_unselected
                )!!
            )
            ivEditMaleIcon.loadDrawable(ContextCompat.getDrawable(activity as EditProfileActivity, R.drawable.male_selected)!!)
            (activity as EditProfileActivity).viewModel.profileInput.gender = 1
        }


        ivEditGenderBackArrow.setOnClickListener {
            callback.onFragmentBackPressed()
        }

        ivEditGenderNext.setOnClickListener {
            (activity as EditProfileActivity).viewModel.list[2] = Profile(
                id = 3,
                text = if ((activity as EditProfileActivity).viewModel.profileInput.gender == 1) "Male" else "Female"
            )
            callback.onNextIconClicked()
        }
    }


    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        @JvmStatic
        fun newInstance() =
            EditGenderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
