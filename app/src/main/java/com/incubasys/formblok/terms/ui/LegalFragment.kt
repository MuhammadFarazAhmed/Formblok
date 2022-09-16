package com.incubasys.formblok.terms.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.incubasys.formblok.R
import com.incubasys.formblok.common.adaptor.TabAdapter
import com.incubasys.formblok.common.data.remote.ApiStatus
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.terms.callback.LegalFragmentCallback
import kotlinx.android.synthetic.main.fragment_legal.*

private const val TYPE = "type"

class LegalFragment : BaseFragment() {
    private var type: Int = -1

    private lateinit var binding: com.incubasys.formblok.databinding.FragmentLegalBinding
    private lateinit var callback: LegalFragmentCallback


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LegalFragmentCallback) callback = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getInt(TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_legal, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter = TabAdapter(childFragmentManager)
        (activity as PrivacyPolicyActivity).viewModel.supportContent.observe(this, Observer {
            when (it.status) {
                ApiStatus.LOADING -> TODO()
                ApiStatus.SUCCESS -> {
                    adapter.addFragment(
                        LegalViewPagerFragment.newInstance(it.data?.privacyPolicy!!),
                        getString(R.string.privacy_policy)
                    )
                    adapter.addFragment(
                        LegalViewPagerFragment.newInstance(it.data?.terms!!),
                        getString(R.string.terms_amp_conditions)
                    )
                    adapter.notifyDataSetChanged()
                    when (type) {
                        1 -> vpLegalPager.currentItem = 0
                        2 -> vpLegalPager.currentItem = 1
                    }
                }
                ApiStatus.EMPTY -> TODO()
                ApiStatus.ERROR -> Toast.makeText(context, it.error?.message, Toast.LENGTH_SHORT).show()
                ApiStatus.COMPLETED -> TODO()
            }
        })


        vpLegalPager.adapter = adapter

        tlLegalTabLayout.setupWithViewPager(vpLegalPager)


        ivLegalBackArrow.setOnClickListener {
            callback.onBackButtonClicked()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(type: Int) =
            LegalFragment().apply {
                arguments = Bundle().apply {
                    putInt(TYPE, type)
                }
            }
    }
}
