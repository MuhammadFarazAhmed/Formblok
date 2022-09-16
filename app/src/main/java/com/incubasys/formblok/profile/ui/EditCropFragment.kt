package com.incubasys.formblok.profile.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.incubasys.formblok.R
import com.incubasys.formblok.auth.callback.CropFragmentCallback
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.databinding.FragmentEditCropPhotoBinding
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_edit_crop_photo.*




class EditCropFragment : BaseFragment() {
    private var imagePath: String? = null
    private lateinit var callback: CropFragmentCallback
    private lateinit var binding: FragmentEditCropPhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imagePath = "file:///" + it.getString(IMAGE_PATH)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CropFragmentCallback) {
            callback = context
        } else {
            throw RuntimeException("$context must implement CropFragmentCallback")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_crop_photo, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.vm = (activity as EditProfileActivity).viewModel

        initCropper()

        bEditCropConfirm.setOnClickListener {
            (activity as EditProfileActivity).viewModel.croppedImageBitmap.value = editCropImageView.croppedImage
            callback.onConfirmButtonClicked()

        }

        bEditCropCancel.setOnClickListener {
            callback.onCancelButtonClicked()
        }

    }

    private fun initCropper() {
        editCropImageView.cropShape = CropImageView.CropShape.OVAL
        editCropImageView.guidelines = CropImageView.Guidelines.OFF
        editCropImageView.setAspectRatio(1, 1)
        editCropImageView.setFixedAspectRatio(true)
        editCropImageView.cropShape = CropImageView.CropShape.OVAL
        editCropImageView.scaleType = CropImageView.ScaleType.FIT_CENTER
        editCropImageView.isAutoZoomEnabled = true
        editCropImageView.isShowProgressBar = true
        editCropImageView.setImageUriAsync(Uri.parse(imagePath))
    }

    companion object {
        private const val IMAGE_PATH = "image_path"
        @JvmStatic
        fun newInstance(imagePath: String) =
            EditCropFragment().apply {
                arguments = Bundle().apply {
                    putString(IMAGE_PATH, imagePath)
                }
            }
    }
}
