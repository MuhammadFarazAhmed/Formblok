package com.incubasys.formblok.onboard.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionManager
import com.incubasys.formblok.R
import com.incubasys.formblok.databinding.FragmentOnboardingItemBinding
import com.incubasys.formblok.onboard.callback.OnboardingItemCallback
import com.incubasys.formblok.onboard.data.Onboarding
import com.incubasys.formblok.onboard.viewmodel.OnboardingItemViewModel
import kotlinx.android.synthetic.main.fragment_onboarding_item.*

class OnboardingItemFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingItemBinding
    private lateinit var viewmodel: OnboardingItemViewModel
    private lateinit var fragmentCallback: OnboardingItemCallback

    companion object {
        private const val ARG_ONBOARDING = "onBoarding"
        private const val ARG_INDEX = "INDEX"
        private const val ARG_SIZE = "SIZE"
        fun newInstance(onboarding: Onboarding, index: Int, size: Int): Fragment {
            val fragment = OnboardingItemFragment()
            val args = Bundle()
            args.putParcelable(ARG_ONBOARDING, onboarding)
            args.putInt(ARG_INDEX, index)
            args.putInt(ARG_SIZE, size)
            fragment.arguments = args
            return fragment
        }
    }

    private fun onAttachToParentFragment(parentFragment: Fragment?) {
        if (parentFragment is OnboardingItemCallback) {
            fragmentCallback = parentFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onAttachToParentFragment(parentFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_onboarding_item, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewmodel = ViewModelProviders.of(this).get(OnboardingItemViewModel::class.java)
        arguments.let {
            viewmodel.onboarding = it?.get(ARG_ONBOARDING) as Onboarding?
        }
        binding.vm = viewmodel
        bNextButton.setOnClickListener { fragmentCallback.onNextButtonClicked(arguments!!.getInt(ARG_INDEX)) }
        if (arguments!!.getInt(ARG_INDEX) == arguments!!.getInt(ARG_SIZE) - 1) {
            bNextButton.text = getString(R.string.get_started)
            bNextButton.setOnClickListener { fragmentCallback.onGetStartedButtonClicked() }
        }
    }


}
