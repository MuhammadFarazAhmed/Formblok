package com.incubasys.formblok.profile.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import com.incubasys.formblok.R
import com.incubasys.formblok.common.data.model.User
import com.incubasys.formblok.common.data.repository.SharedPreferencesRepository
import com.incubasys.formblok.common.viewmodel.BaseViewModel
import com.incubasys.formblok.profile.data.model.Profile
import com.incubasys.formblok.util.ParseErrors
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    application: Application,
    parseErrors: ParseErrors,
    sharedPreferencesRepository: SharedPreferencesRepository
) : BaseViewModel(application, parseErrors) {

    var list = mutableListOf<Profile>()
    var userPhoto = ObservableField<String>()
    private var user: User = sharedPreferencesRepository.getUser()

    init {
        userPhoto.set(sharedPreferencesRepository.getUser().photo?.url)
        setUpList()
    }

    private fun setUpList() {
        list.add(0, Profile(1, R.drawable.ic_name, user.name))
        list.add(1, Profile(2, R.drawable.ic_email, user.email))
        list.add(2,
            Profile(
                3,
                R.drawable.ic_user_type,
                if (user.isBuyer) "Buyer" else "Agent"
            )
        )
        list.add(
            3, Profile(
                4,
                R.drawable.ic_gender,
                if (user.gender == 1) "Male" else if (user.gender == 0) "Female" else "Gender"
            )
        )
        list.add(4,
            Profile(
                5,
                R.drawable.ic_dob,
                user.dob?.toString("dd MMMM yyyy") ?: "Dob"
            )
        )
    }

}
