package com.incubasys.formblok.quotation.callback

import com.incubasys.formblok.quotation.ui.CreateQuotationActivity

interface CommonFragmentCallback {

    fun onNextClicked(action: CreateQuotationActivity.ACTION)
}