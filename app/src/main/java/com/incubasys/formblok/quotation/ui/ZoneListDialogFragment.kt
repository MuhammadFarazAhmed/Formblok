package com.incubasys.formblok.quotation.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.incubasys.formblok.R
import com.incubasys.formblok.projects.data.model.Zone
import com.incubasys.formblok.quotation.adapter.ZoneHorizontalAdapter
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.fragment_zone_list_dialog.*
import android.content.Intent
import android.net.Uri


const val ARG_ITEM_COUNT = "item_count"

class ZoneListDialogFragment : BottomSheetDialogFragment() {
    private var mListener: Listener? = null
    private lateinit var zoneList: ArrayList<Zone>
    private var position: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            zoneList = it.getParcelableArrayList<Zone>("list") as ArrayList<Zone>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_zone_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = ZoneHorizontalAdapter()
        list.adapter = adapter
        adapter.submitList(zoneList)
        list.setOffscreenItems(1)
        list.setItemTransformer(
            ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER)
                .setPivotY(Pivot.Y.CENTER)
                .build()
        )
        list.addOnItemChangedListener { viewHolder, adapterPosition ->
            position = adapterPosition
            if (adapterPosition == list.currentItem) {
                (viewHolder as ZoneHorizontalAdapter.ItemViewholder).binding.itemBackground.alpha = 1f
            }
        }

        bFindOutMore.setOnClickListener {
            position?.let { it1 ->
                zoneList[it1].info_url?.let { url ->
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(browserIntent)
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        /* val parent = parentFragment
         if (parent != null) {
             mListener = parent as Listener
         } else {
             mListener = context as Listener
         }*/
    }

    override fun onDetach() {
        mListener = null
        super.onDetach()
    }

    interface Listener {
        fun onZoneClicked(position: Int)
    }

    companion object {

        fun newInstance(zoneList: ArrayList<Zone>): ZoneListDialogFragment =
            ZoneListDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList("list", zoneList)
                }
            }

    }
}
