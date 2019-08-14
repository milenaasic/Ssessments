package com.ssessments.login_and_registration

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ssessments.utilities.isOnline

class LogIn_ViewModel(application: Application): AndroidViewModel(application) {



    private val _navigateToRegistration= MutableLiveData<Boolean>()
    val navigateToRegistration:LiveData<Boolean>
        get() =_navigateToRegistration

    private val _showProgressBar= MutableLiveData<Boolean>()
    val showProgressBar:LiveData<Boolean>
        get() =_showProgressBar


    private val _showToastNOInternet=MutableLiveData<Boolean>()
    val showToastNOInternet:LiveData<Boolean>
        get() = _showToastNOInternet



    init {
            _showProgressBar.value=false
    }

    //SIGN IN
    fun signInButtonPressed(username:String,password:String){
        if (isOnline(getApplication())) {
             _showProgressBar.value=true
            //startuje kotlin courutines da posaljem username i password i token koji uzimam iz baze

        } else {
            _showProgressBar.value=false
            _showToastNOInternet.value=true

        }
    }


    //SIGN UP
    fun signUpButtomPressed(){
        _navigateToRegistration.value=true
    }

    //NAVIGATION TO REGISTRATION FRAGMENT IS DONE
    fun navigationDone(){
        _navigateToRegistration.value=false
    }

    fun toastNoInternetIsShown(){
        _showToastNOInternet.value=false
    }
}