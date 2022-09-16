package com.incubasys.formblok.onboard.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.incubasys.formblok.R
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.onboard.adaptor.PagerAdapter
import com.incubasys.formblok.onboard.callback.OnboardingCallback
import com.incubasys.formblok.onboard.callback.OnboardingItemCallback
import com.incubasys.formblok.onboard.viewmodel.OnboardingViewModel
import kotlinx.android.synthetic.main.onboarding_fragment.*
import javax.inject.Inject


class OnboardingFragment : BaseFragment(), OnboardingItemCallback {

    override fun onGetStartedButtonClicked() {
        viewModel.setIsOnboardingShown()
        callback.onGetStartedButtonClicked(viewModel.isShownFromSettings())
    }

    override fun onNextButtonClicked(currentIndex: Int) {
        if (currentIndex < vpPager.childCount)
            vpPager.currentItem = vpPager.currentItem + 1
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var callback: OnboardingCallback
    private val viewModel: OnboardingViewModel by lazy {
        ViewModelProviders.of(this, factory).get(OnboardingViewModel::class.java)
    }

    companion object {
        fun newInstance() = OnboardingFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnboardingCallback) callback = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.onboarding_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.setUpList()
        vpPager.adapter = PagerAdapter(childFragmentManager, viewModel)
        cpiIndicator.setViewPager(vpPager)
        callback.onFragmentReady()
    }

    fun setShownFromSettings(shownFromSettings: Boolean) {
        viewModel.setShownFromSettings(shownFromSettings)
    }

}
