package com.incubasys.formblok.auth.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.incubasys.formblok.R
import com.incubasys.formblok.auth.callback.GenderFragmentCallback
import com.incubasys.formblok.auth.extentions.enable
import com.incubasys.formblok.auth.ui.activity.AuthActivity
import com.incubasys.formblok.auth.ui.activity.GotoFragment
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.databinding.FragmentGenderBinding
import com.incubasys.formblok.common.extenstions.loadDrawable
import kotlinx.android.synthetic.main.fragment_gender.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GenderFragment : BaseFragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var callback: GenderFragmentCallback
    private lateinit var binding: FragmentGenderBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is GenderFragmentCallback) {
            callback = context
        } else {
            throw RuntimeException("$context must implement GenderFragmentCallback")
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gender, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callback.onGenderFragmentReady()
        binding.vm = (activity as AuthActivity).viewModel

        ivFemaleIcon.setOnClickListener {
            ivFemaleIcon.loadDrawable(ContextCompat.getDrawable(activity as AuthActivity, R.drawable.female_selected)!!)
            ivMaleIcon.loadDrawable(ContextCompat.getDrawable(activity as AuthActivity, R.drawable.male_unselected)!!)
            (activity as AuthActivity).viewModel.signUpInput.gender  = 0
            callback.onFemaleClicked()
        }

        ivMaleIcon.setOnClickListener {
            ivFemaleIcon.loadDrawable(
                ContextCompat.getDrawable(
                    activity as AuthActivity,
                    R.drawable.female_unselected
                )!!
            )
            ivMaleIcon.loadDrawable(ContextCompat.getDrawable(activity as AuthActivity, R.drawable.male_selected)!!)
            (activity as AuthActivity).viewModel.signUpInput.gender  = 1
            callback.onMaleClicked()
        }


        ivGenderTypeNext.setOnClickListener {
            if((activity as AuthActivity).viewModel.signUpInput.gender == null){
                (activity as AuthActivity).viewModel.signUpInput.gender = 2
            }
            callback.onNextButtonClicked(GotoFragment.PRIVACY_POLICY) }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            GenderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
