package com.incubasys.formblok.profile.viewmodel

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.util.Base64
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.incubasys.formblok.R
import com.incubasys.formblok.auth.extentions.EmailValidator
import com.incubasys.formblok.common.data.model.ProfileInput
import com.incubasys.formblok.common.data.model.User
import com.incubasys.formblok.common.data.remote.ApiResponse
import com.incubasys.formblok.common.data.remote.ApiStatus
import com.incubasys.formblok.common.data.repository.SharedPreferencesRepository
import com.incubasys.formblok.common.viewmodel.BaseViewModel
import com.incubasys.formblok.profile.data.ProfileRepository
import com.incubasys.formblok.profile.data.model.Profile
import com.incubasys.formblok.util.ImagePicker
import com.incubasys.formblok.util.ParseErrors
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

class EditProfileViewModel @Inject constructor(
    application: Application
    , private val parseErrors: ParseErrors
    , private val sharedPreferencesRepository: SharedPreferencesRepository
    , private val profileRepository: ProfileRepository
) : BaseViewModel(application, parseErrors) {

    var list = mutableListOf<Profile>()
    var userPhoto = ObservableField<String>()
     var user: User = sharedPreferencesRepository.getUser()
    var imagePicker: ImagePicker = ImagePicker(application.applicationContext)
    var imagePath: String? = null
    lateinit var storeImagePath: String
    var croppedImageBitmap = MutableLiveData<Bitmap>()

    var userObserver = MutableLiveData<ApiResponse<User>>()

    private var timer: Timer = Timer()
    private val delay: Long = 300 // milliseconds

    var profileInput =
        ProfileInput(
            user.name,
            user.photo?.url,
            user.dob?.toString("dd MMMM yyyy"),
            user.gender,
            user.isAgent,
            user.isBuyer,
            user.email
        )

    init {
        userPhoto.set(profileInput.photoInput)
        setUpList()
    }

    private fun setUpList() {
        list.add(0,
            Profile(1, R.drawable.ic_name, profileInput.name ?: "Name")
        )
        list.add(1,
            Profile(2, R.drawable.ic_email, profileInput.email ?: "Email")
        )
        list.add(
            2, Profile(
                3,
                R.drawable.ic_gender,
                if (profileInput.gender == 1) "Male" else if (profileInput.gender == 0) "Female" else "Gender"
            )
        )
        list.add(3, Profile(4, R.drawable.ic_dob, profileInput.dob ?: "Dob"))

    }

    /**
     *  ################### For Email Fragment #########################
     */
    var editEmailError = ObservableField<String>("")
    var visibleEditEmailText = ObservableField<Boolean>(false)
    var enableEditEmailNextButton = ObservableBoolean(false)

    private fun validateEmail(): Boolean = profileInput.email.let { EmailValidator.isEmailValid(it!!) }

    fun checkEditEmailValidation() {
        timer.cancel()
        timer = Timer()
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    if (validateEmail()) {
                        editEmailError.set(null)
                        enableEditEmailNextButton.set(true)
                    } else {
                        editEmailError.set("Valid Email Required")
                        enableEditEmailNextButton.set(false)
                    }
                }
            },
            delay
        )
        if (!user.email.isEmpty())
            visibleEditEmailText.set(true) else visibleEditEmailText.set(false)
    }

    /**
     *  ################### For Name Fragment #########################
     */
    var editNameError = ObservableField<String>("")
    var visibleEditNameText = ObservableField<Boolean>(false)
    var enableEditNameNextButton = ObservableBoolean(false)

    private fun validateName(): Boolean = profileInput.name!!.isNotEmpty()

    fun checkEditNameValidation() {
        timer.cancel()
        timer = Timer()
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    if (validateName()) {
                        editNameError.set(null)
                        enableEditNameNextButton.set(true)
                    } else {
                        editNameError.set("Valid Name Required")
                        enableEditNameNextButton.set(false)
                    }
                }
            },
            delay
        )
        if (!user.name.isEmpty())
            visibleEditNameText.set(true) else visibleEditNameText.set(false)
    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        imagePath = imagePicker.getSelectedImagePath(requestCode, resultCode, data)
        if (imagePath != null) {
            this.storeImagePath = imagePath!!
        }
    }

    fun encodeToBase64(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos) //bitmap is the bitmap object
        val encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
        profileInput.photoInput = "data:image/jpg;base64,$encodedImage"
    }

    fun updateProfile() {
        compositeDisposable.add(profileRepository.updateProfile(profileInput)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                userObserver.value = ApiResponse(ApiStatus.LOADING)
            }
            .subscribeBy(
                onNext = {
                    sharedPreferencesRepository.setUser(it)
                    userObserver.value = ApiResponse(ApiStatus.SUCCESS, it)
                }
                ,
                onError = {
                    userObserver.value = ApiResponse(ApiStatus.ERROR, parseErrors.interpretErrors(it))
                }
            ))
    }

}
