package com.ssessments.login_and_registration

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssessments.database.NewsDatabaseDao

class RegistrationSharedViewModel(
        val database:NewsDatabaseDao,
        application: Application) : AndroidViewModel(application){


        private val _navigateToRegistration2= MutableLiveData<Boolean>()
        val navigateToRegistration2: LiveData<Boolean>
                get() =_navigateToRegistration2


        fun nextButtonClicked(){
                _navigateToRegistration2.value=true
        }

        //naviigacija iz fragmenta reg1 u reg2
        fun navigationDone(){
                 _navigateToRegistration2.value=false
        }

}