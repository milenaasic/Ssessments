package com.ssessments.newsapp.account

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ssessments.newsapp.database.NewsDatabaseDao
import com.ssessments.newsapp.network.NetworkRequestChangeEmailPhone
import com.ssessments.newsapp.network.NetworkSingleNewsItem
import com.ssessments.newsapp.network.NewsApi
import com.ssessments.newsapp.utilities.EMPTY_TOKEN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

private const val MY_TAG="MY_AccountViewModel"
class AccountViewModel(val database: NewsDatabaseDao,
                       application: Application): AndroidViewModel(application){

    companion object{
        private const val BOTH_EMAIL_AND_PHONE_CHANGED="BOTH_EMAIL_AND_PHONE_CHANGED"
        private const val EMAIL_CHANGED="EMAIL_CHANGED"
        private const val PHONE_CHANGED="PHONE_CHANGED"
        private const val NOTHING_CHANGED="NOTHING_CHANGED"

    }


    val myUser=database.getUser()

    private val _changeEmailPhoneWasSuccessful = MutableLiveData<Boolean>()
    val changeEmailPhoneWasSuccessful: LiveData<Boolean>
        get() = _changeEmailPhoneWasSuccessful


    private val _somethingWentWrongAccount = MutableLiveData<Boolean>()
    val somethingWentWrongAccount: LiveData<Boolean>
        get() = _somethingWentWrongAccount

    fun userChangedHisRegData(changedEmail:String,changedPhone:String){

        when(somethingHasReallyChanged(changedEmail,changedPhone)){
            BOTH_EMAIL_AND_PHONE_CHANGED-> sendRequestToServer(changedEmail,changedPhone)
            EMAIL_CHANGED->sendRequestToServer(newEmail=changedEmail)
            PHONE_CHANGED->sendRequestToServer(newPhone=changedPhone)
            NOTHING_CHANGED->{}
        }
    }



    private fun somethingHasReallyChanged(newEmail: String, newPhone: String): String {
        when{

            !newEmail.equals(myUser.value?.email) && !newPhone.equals(myUser.value?.mobilephone)->return BOTH_EMAIL_AND_PHONE_CHANGED
            !newEmail.equals(myUser.value?.email)->return EMAIL_CHANGED
            !newPhone.equals(myUser.value?.mobilephone)->return PHONE_CHANGED
            else->return NOTHING_CHANGED
        }
    }

    private fun sendRequestToServer(newEmail: String="", newPhone: String="") {
    Log.i(MY_TAG,"vrednost emaile je $newEmail, a phone je $newPhone")

        viewModelScope.launch {

            val deferredResult=NewsApi.retrofitService.sendChangedEmailOrPhoneToServer(
                                                                            NetworkRequestChangeEmailPhone(token=myUser.value?.token?: EMPTY_TOKEN,email = newEmail,phone=newPhone))

            try {
                val result=deferredResult.await()
                updateUserRegistrationDataInDatabase(newEmail,newPhone)
               _changeEmailPhoneWasSuccessful.value=true
            }catch (e:Exception){
                Log.i(MY_TAG," network greska je ${e.message}")
                _somethingWentWrongAccount.value=true
                //proba dok ne proradi ruta
                updateUserRegistrationDataInDatabase(newEmail,newPhone)
            }

        }

    }

    private fun updateUserRegistrationDataInDatabase(newEmail: String, newPhone: String) {

    viewModelScope.launch {
        withContext(Dispatchers.IO){
            if(newEmail.isNotEmpty()) database.updateEmailInUserTable(newEmail)
            if(newPhone.isNotEmpty()) database.updatePhoneInUserTable(newPhone)

        }
             }
    }

}
