package com.incubasys.formblok.projects.ui


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.incubasys.formblok.R
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.projects.callback.NoAddressFragmentCallback
import kotlinx.android.synthetic.main.fragment_no_address.*
import kotlinx.android.synthetic.main.toolbar.*

class NoAddressFragment : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var callback: NoAddressFragmentCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is NoAddressFragmentCallback) callback = context
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
        return inflater.inflate(R.layout.fragment_no_address, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        ivToolbarBackArrow.setOnClickListener {
            callback.onNoAddressBackPressed()
        }

        bSearchAddress.setOnClickListener {
            callback.onSearchAddressButtonClicked()
        }



    }


    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance() =
            NoAddressFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
