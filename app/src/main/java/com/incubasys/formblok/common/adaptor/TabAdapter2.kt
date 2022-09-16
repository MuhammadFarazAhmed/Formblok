package com.incubasys.formblok.common.adaptor

import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.incubasys.formblok.projects.data.model.PropertyOutput
import com.incubasys.formblok.projects.ui.AreaFragment
import com.incubasys.formblok.projects.ui.DetailFragment
import com.incubasys.formblok.projects.ui.InfoFragment
import com.incubasys.formblok.projects.ui.QuoteFragment


class TabAdapter2 internal constructor(fm: FragmentManager, private val propertyOutput: PropertyOutput) :
    FragmentStatePagerAdapter(fm) {

    val list = listOf("Info", "Detail", "Area", "Quote")
    var fragmentList = mutableListOf<Fragment>()

    override fun getItem(position: Int): Fragment {
       return fragmentList[position]
    }

    fun setUpList() {
        fragmentList.add(0, InfoFragment.newInstance(propertyOutput))
        fragmentList.add(1, DetailFragment.newInstance(propertyOutput))
        fragmentList.add(2, AreaFragment.newInstance(propertyOutput))
    }

    fun addQuoteFragment() {
       // fragmentList.add(3, QuoteFragment.newInstance(propertyOutput))
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return list[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }
}