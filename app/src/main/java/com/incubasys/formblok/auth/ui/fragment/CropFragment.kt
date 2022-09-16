package com.incubasys.formblok.auth.ui.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.incubasys.formblok.R
import com.incubasys.formblok.auth.callback.CropFragmentCallback
import com.incubasys.formblok.auth.ui.activity.AuthActivity
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.databinding.FragmentCropBinding
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_crop.*


private const val IMG_PATH = "image_path"

class CropFragment : BaseFragment() {

    private var imagePath: String? = null
    private lateinit var callback: CropFragmentCallback
    private lateinit var binding: FragmentCropBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imagePath = "file:///" + it.getString(IMG_PATH)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_crop, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CropFragmentCallback) {
            callback = context
        } else {
            throw RuntimeException("$context must implement CropFragmentCallback")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.vm = (activity as AuthActivity).viewModel

        initCropper()

        bCropConfirm.setOnClickListener {
            (activity as AuthActivity).viewModel.croppedImage.value = cropImageView.croppedImage
            callback.onConfirmButtonClicked()
        }

        bCropCancel.setOnClickListener {
            callback.onCancelButtonClicked()
        }

    }

    private fun initCropper() {
        cropImageView.cropShape = CropImageView.CropShape.OVAL
        cropImageView.guidelines = CropImageView.Guidelines.OFF
        cropImageView.setAspectRatio(1, 1)
        cropImageView.setFixedAspectRatio(true)
        cropImageView.cropShape = CropImageView.CropShape.OVAL
        cropImageView.scaleType = CropImageView.ScaleType.FIT_CENTER
        cropImageView.isAutoZoomEnabled = true
        cropImageView.isShowProgressBar = true
        cropImageView.setImageUriAsync(Uri.parse(imagePath))
    }


    companion object {

        @JvmStatic
        fun newInstance(imagePath: String) =
            CropFragment().apply {
                arguments = Bundle().apply {
                    putString(IMG_PATH, imagePath)
                }
            }
    }
}
