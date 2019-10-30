package com.ssessments.newsapp.login_and_registration

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ssessments.newsapp.database.NewsDatabaseDao
import com.ssessments.newsapp.network.NetworkUserRegistrationData
import com.ssessments.newsapp.network.NewsApi
import kotlinx.coroutines.launch

private const val MYTAG="MY_RegistraSharedViewMo"

class RegistrationSharedViewModel(
        val database:NewsDatabaseDao,
        application: Application) : AndroidViewModel(application){

        //lista unesenih vrednosti sa prve stranice registracije-ime, telefon...
        lateinit var userRegistration1Fields:ArrayList<String>

        private val _navigateToRegistration2= MutableLiveData<Boolean>()
        val navigateToRegistration2: LiveData<Boolean>
                get() =_navigateToRegistration2

        private val _navigateBackToRegistration1= MutableLiveData<Boolean>()
        val navigateBackToRegistration1: LiveData<Boolean>
                get() =_navigateBackToRegistration1

        private val _showProgressBarRegistration= MutableLiveData<Boolean>()
        val showProgressBarRegistration: LiveData<Boolean>
                get() =_showProgressBarRegistration

        private val _showToastRegistrationSent= MutableLiveData<Boolean>()
        val showToastRegistrationSent: LiveData<Boolean>
                get() =_showToastRegistrationSent

        private val _showToastSomethingWentWrongWithRegistration= MutableLiveData<Boolean>()
        val showToastSomethingWentWrongWithRegistration: LiveData<Boolean>
                get() =_showToastSomethingWentWrongWithRegistration

        private val _sendUserBackToMainActicvity= MutableLiveData<Boolean>()
        val sendUserBackToMainActicvity: LiveData<Boolean>
                get() =_sendUserBackToMainActicvity



        fun nextButtonClicked(){
                _navigateToRegistration2.value=true
        }

        fun backButtonClicked(){
                 _navigateBackToRegistration1.value=true
        }


        fun registerButtonClicked(username:String,password:String){

                _showProgressBarRegistration.value=true

                val userRegData:NetworkUserRegistrationData= NetworkUserRegistrationData(
                        userRegistration1Fields[0],
                        userRegistration1Fields[1],
                        userRegistration1Fields[2],
                        userRegistration1Fields[3],
                        userRegistration1Fields[4],
                        userRegistration1Fields[5],
                        username,
                        password)

                //posalji user reegistration na server
                viewModelScope.launch {
                        var getResultDeferred = NewsApi.retrofitService.postUserRegistration(userRegData)
                        try {

                                var result = getResultDeferred.await()
                                Log.i(MYTAG,"result je: ${result}")
                                _showProgressBarRegistration.value=false
                                _showToastRegistrationSent.value=true
                                _sendUserBackToMainActicvity.value=true

                        } catch (e: Exception) {
                                Log.i(MYTAG,"Failure is: ${e.message}")
                                _showProgressBarRegistration.value=false
                                _showToastSomethingWentWrongWithRegistration.value=true

                        }
                }

        }

        //naviigacija iz fragmenta reg1 u reg2
        fun navigationToregistration2Done(){
                 _navigateToRegistration2.value=false
        }

        //naviigacija nazad u fragment1
        fun navigationBackToRegistration1Done(){
                _navigateBackToRegistration1.value=false
        }

        //pokazan toast registartion sent
        fun toastRegistrationSentIsShown(){
                _showToastRegistrationSent.value=false
        }

        //pokazan toast somethingwentwrong
        fun toastSomethingWentWrongWithRegistrationIsShown(){
                _showToastSomethingWentWrongWithRegistration.value=false
        }

        fun setMyUserRegistration1Fields(list:ArrayList<String>){
                userRegistration1Fields=list
                Log.i(MYTAG,"lista je {$userRegistration1Fields")
        }

        fun userSentToMainActivity(){
                _sendUserBackToMainActicvity.value=false
        }

}