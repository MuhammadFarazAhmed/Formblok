package com.incubasys.formblok.quotation.ui


import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.incubasys.formblok.R
import com.incubasys.formblok.common.data.model.MaterialType
import com.incubasys.formblok.common.data.model.MaterialTypeProperty
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.quotation.adapter.MaterialAdapter
import com.incubasys.formblok.quotation.data.model.Material
import kotlinx.android.synthetic.main.fragment_material_item.*
import java.util.*


private const val DATA = "data"
private const val ARG_PARAM2 = "param2"


class MaterialItemFragment : BaseFragment() {
    private var data: List<MaterialType>? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            data = it.getParcelableArrayList(DATA)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_material_item, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val materialAdapter = MaterialAdapter()

        rvMaterials.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = materialAdapter
        }

        val list = mutableListOf<Material>()
        data?.forEach {
            list.add(Material(type = MaterialAdapter.TYPE_HEADER, heading = it.name))
            list.add(Material(type = MaterialAdapter.TYPE_NORMAL, list = it.materials))
        }

        materialAdapter.submitList(list)

    }

    companion object {
        @JvmStatic
        fun newInstance(data: ArrayList<MaterialType>?) =
            MaterialItemFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(DATA, data as ArrayList<out Parcelable>?)
                    // putString(ARG_PARAM2, param2)
                }
            }
    }
}
