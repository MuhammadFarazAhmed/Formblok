package com.incubasys.formblok.profile.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.databinding.EditProfileFragmentBinding
import com.incubasys.formblok.profile.adaptor.EditProfileAdaptor
import com.incubasys.formblok.profile.callback.EditProfileAdaptorCallback
import com.incubasys.formblok.profile.callback.EditProfileFragmentCallback
import kotlinx.android.synthetic.main.edit_profile_fragment.*
import kotlinx.android.synthetic.main.settings_list_item.*

class EditProfileFragment : BaseFragment(), EditProfileAdaptorCallback {


    companion object {
        val adaptor = EditProfileAdaptor()
        fun newInstance() = EditProfileFragment()
        fun notifyAdaptor() = adaptor.notifyDataSetChanged()
    }


    private lateinit var binding: EditProfileFragmentBinding
    private lateinit var callback: EditProfileFragmentCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is EditProfileFragmentCallback) callback = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EditProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.vm = (activity as EditProfileActivity).viewModel
        initRecyclerView()
        setClickListeners()
    }

    private fun initRecyclerView() {
        adaptor.setAdaptorCallback(this)
        rvEditProfile.layoutManager = LinearLayoutManager(activity)
        rvEditProfile.adapter = adaptor
        rvEditProfile.isNestedScrollingEnabled = false
        adaptor.submitList((activity as EditProfileActivity).viewModel.list)
    }


    private fun setClickListeners() {
        ivEditProfileChangePhotoLabel.setOnClickListener { callback.onChangePhotoClicked() }
        ivEditProfileBackArrow.setOnClickListener { callback.onBackPress() }
        bEditProfileSave.setOnClickListener { callback.onSaveButtonClicked() }

    }

    override fun onListItemClicked(position: Int) {
        when ((activity as EditProfileActivity).viewModel.list[position].id) {
            1 -> callback.onNameItemClicked()
            2 -> callback.onEmailItemClicked()
            3 -> callback.onGenderItemClicked()
            4 -> callback.onDobItemClicked()
        }
    }

}
