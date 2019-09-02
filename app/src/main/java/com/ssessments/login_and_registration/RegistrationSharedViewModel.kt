package com.ssessments.login_and_registration

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssessments.database.NewsDatabaseDao
import com.ssessments.utilities.RegistrationFields

private const val MYTAG="MY_RegistraSharedViewMo"

class RegistrationSharedViewModel(
        val database:NewsDatabaseDao,
        application: Application) : AndroidViewModel(application){

        //lista unesenih vrednosti sa prve stranice registracije-ime, telefon...
        lateinit var userRegistration1Fields:ArrayList<String?>

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


        fun setMyUserRegistration1Fields(list:ArrayList<String?>){
                userRegistration1Fields=list
                Log.i(MYTAG,"lista je {$userRegistration1Fields")
        }

}