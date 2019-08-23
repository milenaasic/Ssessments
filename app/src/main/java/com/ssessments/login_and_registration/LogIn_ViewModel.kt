package com.ssessments.login_and_registration

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ssessments.database.NewsDatabaseDao
import com.ssessments.database.UserData
import com.ssessments.network.NetworkUserData
import com.ssessments.network.NewsApi
import com.ssessments.utilities.isOnline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val MY_TAG="MY_LogIn_ViewModel"

class LogIn_ViewModel(val database: NewsDatabaseDao,
                      application: Application): AndroidViewModel(application) {



    /*private val _navigateToRegistration= MutableLiveData<Boolean>()
    val navigateToRegistration:LiveData<Boolean>
        get() =_navigateToRegistration*/

    private val _showProgressBar= MutableLiveData<Boolean>()
    val showProgressBar:LiveData<Boolean>
        get() =_showProgressBar


    private val _showToastNOInternet=MutableLiveData<Boolean>()
    val showToastNOInternet:LiveData<Boolean>
        get() = _showToastNOInternet

    private val _showToastUserLoggedIN=MutableLiveData<Boolean>()
    val showToastUserLoggedIN:LiveData<Boolean>
        get() = _showToastUserLoggedIN

    private val _sendUserToHomeFragment=MutableLiveData<Boolean>()
    val sendUserToHomeFragment:LiveData<Boolean>
        get() = _sendUserToHomeFragment


    init {
            _showProgressBar.value=false
    }

    //SIGN IN
    fun signInButtonPressed(username:String,password:String){
        if (isOnline(getApplication())) {
             _showProgressBar.value=true
            signInThisUser(username,password)
        } else {
            _showProgressBar.value=false
            _showToastNOInternet.value=true

        }
    }


    fun signInThisUser(username:String,password:String){

        viewModelScope.launch {
            var loginuserdeferred=NewsApi.retrofitService.postUserLogIn(NetworkUserData(username,password,null))
            try {
                //uspesan login vraca mi se token kao String
                var resulttoken=loginuserdeferred.await()
                //vratio se token upisi korisnika u bazu i vrati ga na mainactivity
                withContext(Dispatchers.IO){
                    database.apply{
                        clearUserDataTable()
                        insertUser(UserData(username,password,resulttoken))
                        //kako da proverim da je izvrsen upis u bazu?
                    }
                }
                _showProgressBar.value=false
                _showToastUserLoggedIN.value=true

                //posalji usera u main fragment
                _sendUserToHomeFragment.value=true

            }catch (t:Throwable){
                Toast.makeText(getApplication(),"LOGIN FAILED",Toast.LENGTH_LONG).show()


                //proba dok ne proradi server-obrisi posle
                withContext(Dispatchers.IO){
                    database.clearUserDataTable()
                    database.insertUser(UserData(username,password,"12345token"))
                    //Log.i(MY_TAG,"upisan user je ${database.getUser().toString()}")

                }
                _showProgressBar.value=false
                _showToastUserLoggedIN.value=true
                _sendUserToHomeFragment.value=true
            }
        }

    }


    //SIGN UP
    /*fun signUpButtomPressed(){
        _navigateToRegistration.value=true
    }

    //NAVIGATION TO REGISTRATION FRAGMENT IS DONE
    fun navigationDone(){
        _navigateToRegistration.value=false
    }*/

    fun toastNoInternetIsShown(){
        _showToastNOInternet.value=false
    }

    fun toastLoggedInUserIsShown(){
        _showToastUserLoggedIN.value=false
    }

    fun userSentToHomeFragment(){
        _sendUserToHomeFragment.value=false
    }
}