package com.incubasys.formblok.quotation.adapter

import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.incubasys.formblok.common.data.model.MaterialData
import com.incubasys.formblok.common.data.model.MaterialType
import com.incubasys.formblok.common.data.model.MaterialTypeProperty
import com.incubasys.formblok.quotation.ui.MaterialItemFragment
import java.util.*

class MaterialPagerAdapter internal constructor(
    fm: FragmentManager
) : FragmentStatePagerAdapter(fm) {

    private lateinit var categoryList: List<MaterialData>
    private var pos: Int? = null

    override fun getItem(position: Int): Fragment {
        pos = position
        return MaterialItemFragment.newInstance(categoryList[position].types as ArrayList<MaterialType>?)
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return categoryList[position].name
    }

    override fun getCount(): Int {
        return categoryList.size
    }

    fun setList(categoryList: List<MaterialData>) {
        this.categoryList = categoryList
    }

    fun getMaterialData(position:Int): MaterialData {
        return categoryList[position]
    }

    fun getMaterialTypeWithData(position:Int): MutableList<MaterialType>? {
        return categoryList[position].types
    }
}