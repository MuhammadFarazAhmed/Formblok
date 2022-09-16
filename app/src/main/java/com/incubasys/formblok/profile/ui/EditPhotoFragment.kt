package com.incubasys.formblok.profile.ui


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.incubasys.formblok.R
import com.incubasys.formblok.auth.callback.EditPhotoFragmentCallback
import com.incubasys.formblok.common.extenstions.spannedHeading
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.common.ui.loadImageDrawable
import com.incubasys.formblok.databinding.FragmentEditPhotoBinding
import kotlinx.android.synthetic.main.fragment_edit_photo.*

class EditPhotoFragment : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentEditPhotoBinding
    private lateinit var callback: EditPhotoFragmentCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is EditPhotoFragmentCallback) {
            callback = context
        } else {
            throw RuntimeException("$context must implement EditPhotoFragmentCallback")
        }
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_photo, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.vm = (activity as EditProfileActivity).viewModel

        ivEditPhotoBackArrow.setOnClickListener {
            callback.onEditPhotoBackPress()
        }

        tvEditPhotoFragmentHeading.text =
            spannedHeading(
                activity as EditProfileActivity,
                getString(R.string.what_do_you_look_like),
                getString(R.string.like)
            )

        (activity as EditProfileActivity).viewModel.croppedImageBitmap.observe(this, Observer {
            loadImageDrawable(ivEditPhotoChoosePhoto, it, null, null, null, true)
            (activity as EditProfileActivity).viewModel.encodeToBase64(it)
        }
        )

        bEditPhotoUsePhoto.setOnClickListener {
            callback.onFragmentBackPressed()
        }

        bEditPhotoChangePhoto.setOnClickListener {
            callback.onEditPhotoChangePhotoClicked()
        }
    }


    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        @JvmStatic
        fun newInstance() =
            EditPhotoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
