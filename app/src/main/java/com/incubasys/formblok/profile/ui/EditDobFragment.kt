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
import com.incubasys.formblok.databinding.FragmentEditDobBinding
import com.incubasys.formblok.profile.callback.EditDobFragmentCallback
import com.incubasys.formblok.profile.data.model.Profile
import kotlinx.android.synthetic.main.fragment_edit_dob.*
import org.joda.time.LocalDate
import java.util.*


class EditDobFragment : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentEditDobBinding
    private lateinit var callback: EditDobFragmentCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is EditDobFragmentCallback) {
            callback = context
        } else {
            throw RuntimeException("$context must implement EditDobFragmentCallback")
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
        binding = DataBindingUtil.inflate(inflater, com.incubasys.formblok.R.layout.fragment_edit_dob, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tvEditDobFragmentHeading.text =
            spannedHeading(
                (activity as EditProfileActivity),
                getString(R.string.when_were_you_born),
                getString(R.string.born)
            )


        initDatePicker()

        ivEditDobBackArrow.setOnClickListener {
            callback.onFragmentBackPressed()
        }

        ivEditDobNext.setOnClickListener {
            (activity as EditProfileActivity).viewModel.profileInput.dob =
                jodaDate?.toString("dd MMMM yyyy") ?: (activity as EditProfileActivity).viewModel.profileInput.dob
            (activity as EditProfileActivity).viewModel.list[3] =
                Profile(
                    id = 4,
                    text = (activity as EditProfileActivity).viewModel.profileInput.dob
                )
            callback.onNextIconClicked()
        }

    }

    private fun initDatePicker() {
        val cal = Calendar.getInstance()
        cal.time = (activity as EditProfileActivity).viewModel.user.dob?.toDate() ?: cal.time
        single_day_picker.selectDate(cal)
        single_day_picker.maxDate = Calendar.getInstance().time
        single_day_picker.addOnDateChangedListener { _, date ->
            jodaDate = LocalDate(date)
        }
    }


    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        private var jodaDate: LocalDate? = null
        @JvmStatic
        fun newInstance() =
            EditDobFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
