package com.incubasys.formblok.onboard.adaptor

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.incubasys.formblok.onboard.ui.fragment.OnboardingItemFragment
import com.incubasys.formblok.onboard.viewmodel.OnboardingViewModel

class PagerAdapter(fm: FragmentManager, private val viewModel: OnboardingViewModel) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return OnboardingItemFragment.newInstance(viewModel.list[position], position, viewModel.list.size)
    }

    override fun getCount(): Int {
        return viewModel.list.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)

    }


}