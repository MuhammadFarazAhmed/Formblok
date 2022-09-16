package com.incubasys.formblok.auth.ui.fragment

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.incubasys.formblok.R
import com.incubasys.formblok.auth.callback.PhotoFragmentCallback
import com.incubasys.formblok.auth.ui.activity.AuthActivity
import com.incubasys.formblok.auth.ui.activity.GotoFragment
import com.incubasys.formblok.common.extenstions.hideKeyboard
import com.incubasys.formblok.common.extenstions.makeGone
import com.incubasys.formblok.common.extenstions.makeVisible
import com.incubasys.formblok.common.ui.BaseFragment
import com.incubasys.formblok.common.ui.loadImageDrawable
import com.incubasys.formblok.databinding.FragmentPhotoBinding
import com.incubasys.formblok.util.Compressor
import kotlinx.android.synthetic.main.fragment_photo.*
import java.io.ByteArrayOutputStream
import java.io.File


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class PhotoFragment : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var callback: PhotoFragmentCallback
    private lateinit var binding: FragmentPhotoBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is PhotoFragmentCallback) {
            callback = context
        } else {
            throw RuntimeException("$context must implement PhotoFragmentCallback")
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callback.onPhotoFragmentReady()
        binding.vm = (activity as AuthActivity).viewModel

        /*binding.lifecycleOwner = this*/ //not working

        (activity as AuthActivity).viewModel.croppedImage.observe(this, Observer {
            bUsePhoto.makeVisible()
            bChangePhoto.makeVisible()
            bChoosePhoto.makeGone()

            loadImageDrawable(ivChoosePhoto, it, null, null, null, true)

            val baos = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG, 100, baos) //compressedBitmap is the bitmap object
            val encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
            (activity as AuthActivity).viewModel.signUpInput.photoInput = "data:image/jpg;base64,$encodedImage"

        }
        )

        bChoosePhoto.setOnClickListener {
            callback.onChoosePhotoClicked()
        }

        bUsePhoto.setOnClickListener {
            callback.onNextButtonClicked(GotoFragment.DATE)
        }

        bChangePhoto.setOnClickListener {
            callback.onChoosePhotoClicked()
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            PhotoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
