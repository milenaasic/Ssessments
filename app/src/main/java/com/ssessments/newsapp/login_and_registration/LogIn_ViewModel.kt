package com.ssessments.newsapp.login_and_registration

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ssessments.newsapp.database.NewsDatabaseDao
import com.ssessments.newsapp.database.UserData
import com.ssessments.newsapp.network.NetworkForgotPasswordRequest
import com.ssessments.newsapp.network.NetworkUserData
import com.ssessments.newsapp.network.NetworkUserDataResponse
import com.ssessments.newsapp.network.NewsApi
import com.ssessments.newsapp.utilities.EMPTY_FIREBASEID
import com.ssessments.newsapp.utilities.EMPTY_TOKEN
import com.ssessments.newsapp.utilities.isOnline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val MY_TAG="MY_LogIn_ViewModel"

class LogIn_ViewModel(val database: NewsDatabaseDao,
                      application: Application): AndroidViewModel(application) {


    private val _showProgressBar = MutableLiveData<Boolean>()
    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar

    private val _showToastNOInternet = MutableLiveData<Boolean>()
    val showToastNOInternet: LiveData<Boolean>
        get() = _showToastNOInternet

    private val _showToastUserLoggedIN = MutableLiveData<Boolean>()
    val showToastUserLoggedIN: LiveData<Boolean>
        get() = _showToastUserLoggedIN

    private val _showToastSomethingWentWrong = MutableLiveData<Boolean>()
    val showToastSomethingWentWrong: LiveData<Boolean>
        get() = _showToastSomethingWentWrong

    private val _sendUserToHomeFragment = MutableLiveData<Boolean>()
    val sendUserToHomeFragment: LiveData<Boolean>
        get() = _sendUserToHomeFragment

    private val _showToastForgotPasswordHandeled = MutableLiveData<Boolean>()
    val showToastForgotPasswordHandeled: LiveData<Boolean>
        get() = _showToastForgotPasswordHandeled

    private val _showToastWrongPasswordOrUsername = MutableLiveData<Boolean>()
    val showToastWrongPasswordOrUsername: LiveData<Boolean>
        get() = _showToastWrongPasswordOrUsername


    init {
        //_showProgressBar.value=false
    }

    //SIGN IN
    fun signInButtonPressed(username: String, password: String) {
        if (isOnline(getApplication())) {
            _showProgressBar.value = true
            signInThisUser(username, password)
        } else {
            // _showProgressBar.value=false
            _showToastNOInternet.value = true

        }
    }


    fun signInThisUser(username: String, password: String) {

            viewModelScope.launch {
                with(Dispatchers.IO){

                val userFirebaseID = database.getUserNoLiveData().firebaseID
                var loginuserdeferred = NewsApi.retrofitService.postUserLogIn(NetworkUserData(username, password))

                try {

                    var result = loginuserdeferred.await()
                    Log.i(MY_TAG, "result je ${result}")
                    val receivedToken=result.token

                    //vratio se token upisi korisnika u bazu i vrati ga na mainactivity
                    withContext(Dispatchers.IO) {
                        database.updateUser(UserData(username = username, password = password, token = receivedToken,firebaseID = userFirebaseID))

                    }
                    _showProgressBar.value = false
                    _showToastUserLoggedIN.value = true
                    _sendUserToHomeFragment.value = true

                } catch (e: Exception) {
                    Log.i(MY_TAG, "poruka network greska ${e.message}")
                    _showProgressBar.value = false

                    val responseMessage:String?=e.message

                    if(responseMessage!=null){

                           if(responseMessage.contains("401")) _showToastWrongPasswordOrUsername.value=true
                           else _showToastSomethingWentWrong.value=true

                    }else _showToastSomethingWentWrong.value = true

                }

                }
            }

        }


        //Forgot password
        fun sendForgotPasswordEmail(usermail: String) {

            _showProgressBar.value = true

            viewModelScope.launch {

                var getResultDeferred = NewsApi.retrofitService.postForgotPassword(NetworkForgotPasswordRequest(usermail))
                try {
                    var result = getResultDeferred.await()
                    Log.i(MY_TAG, "forgot password result: ${result}")
                    _showProgressBar.value = false
                    _showToastForgotPasswordHandeled.value = true

                } catch (e: Exception) {
                    Log.i(MY_TAG, "forgot password Failure is: ${e.message}")

                    _showProgressBar.value = false

                    _showToastSomethingWentWrong.value = true

                }
            }

        }

        fun toastNoInternetIsShown() {
            _showToastNOInternet.value = false
        }

        fun toastLoggedInUserIsShown() {
            _showToastUserLoggedIN.value = false
        }

        fun toastSomethingWentWrongIsShown() {
            _showToastSomethingWentWrong.value = false
        }

        fun userSentToHomeFragment() {
            _sendUserToHomeFragment.value = false
        }

        fun toastForgotPaswordHandledIsShown() {
            _showToastForgotPasswordHandeled.value = false
        }

        fun toastWrongPasswordOrUsernameIsShown(){
            _showToastWrongPasswordOrUsername.value=false
        }


}