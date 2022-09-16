package com.incubasys.formblok.auth.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.incubasys.formblok.R
import com.incubasys.formblok.auth.callback.NameFragmentCallback
import com.incubasys.formblok.auth.ui.activity.AuthActivity
import com.incubasys.formblok.auth.ui.activity.GotoFragment
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.databinding.FragmentNameBinding
import kotlinx.android.synthetic.main.fragment_name.*

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NameFragment : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var callback: NameFragmentCallback
    private lateinit var binding: FragmentNameBinding

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_name, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is NameFragmentCallback) {
            callback = context
        } else {
            throw RuntimeException("$context must implement OnNameFragmentCallback")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callback.onNameFragmentReady()
        binding.vm = (activity as AuthActivity).viewModel
        ivNameNext.setOnClickListener { callback.onNextButtonClicked(GotoFragment.PHOTO) }

    }


    companion object {
        @JvmStatic
        fun newInstance() =
            NameFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
