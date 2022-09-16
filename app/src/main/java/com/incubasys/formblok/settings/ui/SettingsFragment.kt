package com.incubasys.formblok.settings.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.incubasys.formblok.R
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.databinding.SettingsFragmentBinding
import com.incubasys.formblok.profile.callback.SettingsFragmentCallback
import com.incubasys.formblok.settings.adaptor.SettingsAdapter
import com.incubasys.formblok.settings.callback.SettingsAdaptorCallback
import kotlinx.android.synthetic.main.settings_fragment.*

class SettingsFragment : BaseFragment(), SettingsAdaptorCallback {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var binding: SettingsFragmentBinding
    private lateinit var callback: SettingsFragmentCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SettingsFragmentCallback) callback = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.settings_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.vm = (activity as SettingsActivity).viewModel
        initRecyclerView()
        ivSettingsBackArrow.setOnClickListener {
            callback.onbackPress()
        }

        bSettingsLogout.setOnClickListener {
            callback.onLogout()
        }
    }

    private fun initRecyclerView() {
        val adaptor = SettingsAdapter()
        adaptor.setAdaptorCallback(this)
        rvSettings.layoutManager = LinearLayoutManager(activity)
        rvSettings.adapter = adaptor
        rvSettings.isNestedScrollingEnabled = false
        adaptor.submitList((activity as SettingsActivity).viewModel.list)
    }

    override fun onListItemClicked(position: Int) {
        val id = (activity as SettingsActivity).viewModel.list[position].id
        when (id) {
            1 -> callback.onChangePasswordClicked()
            2 -> callback.onPrivacyPolicyClicked()
            3 -> callback.onTermsAndConditionClicked()
            4 -> callback.onTutorialClicked()
            5 -> callback.onContactAdmin()
        }
    }
}
