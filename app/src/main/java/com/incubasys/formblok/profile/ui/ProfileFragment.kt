package com.incubasys.formblok.profile.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.incubasys.formblok.R
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.databinding.ProfileFragmentBinding
import com.incubasys.formblok.home.adaptor.ProfileAdaptor
import com.incubasys.formblok.profile.callback.ProfileFragmentCallback
import com.incubasys.formblok.profile.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.profile_fragment.*
import javax.inject.Inject

class ProfileFragment : BaseFragment() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: ProfileViewModel by lazy {
        ViewModelProviders.of(this, factory).get(ProfileViewModel::class.java)
    }

    private lateinit var callback: ProfileFragmentCallback
    private lateinit var binding: ProfileFragmentBinding

    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ProfileFragmentCallback) callback = context
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_fragment, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.vm = viewModel
        val adaptor = ProfileAdaptor()
        rvProfile.layoutManager = LinearLayoutManager(activity)
        rvProfile.adapter = adaptor
        rvProfile.isNestedScrollingEnabled = false
        adaptor.submitList(viewModel.list)

        tvEditProfileLabel.setOnClickListener { callback.onEditProfileTextClicked() }
        ivProfileSettings.setOnClickListener { callback.onSettingsImageClicked() }
    }

}
