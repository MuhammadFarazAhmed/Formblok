package com.incubasys.formblok.quotation.callback

import android.view.View
import com.incubasys.formblok.common.data.model.RoomOutput

interface RoomAdapterCallback {

    fun onItemCheckChanged(room:RoomOutput,position: Int)

    fun onItemAddClicked(position: Int,room: RoomOutput)

    fun onItemClicked(position: Int)
}