package com.incubasys.formblok.auth.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.incubasys.formblok.auth.callback.DobFragmentCallback
import com.incubasys.formblok.auth.ui.activity.AuthActivity
import com.incubasys.formblok.auth.ui.activity.GotoFragment
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.databinding.FragmentDobBinding
import kotlinx.android.synthetic.main.fragment_dob.*
import org.joda.time.LocalDate
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DobFragment : BaseFragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var callback: DobFragmentCallback
    private lateinit var binding: FragmentDobBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DobFragmentCallback) {
            callback = context
        } else {
            throw RuntimeException("$context must implement DobFragmentCallback")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, com.incubasys.formblok.R.layout.fragment_dob, container, false)
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.vm = (activity as AuthActivity).viewModel
        callback.onDobFragmentReady()

        initDatePicker()

        ivDobNext.setOnClickListener {
            (activity as AuthActivity).viewModel.signUpInput.dob =
                jodaDate?.toString("dd MMMM yyyy") ?: (activity as AuthActivity).viewModel.signUpInput.dob
            callback.onNextButtonClicked(GotoFragment.GENDER)
        }
    }

    private fun initDatePicker() {
        auth_single_day_picker.selectDate(Calendar.getInstance())
        auth_single_day_picker.maxDate = Calendar.getInstance().time
        auth_single_day_picker.addOnDateChangedListener { _, date ->
            jodaDate = LocalDate(date)
        }
    }


    companion object {
        private var jodaDate: LocalDate? = null
        @JvmStatic
        fun newInstance() =
            DobFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
