package com.ssessments.newsapp.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel;
import com.ssessments.newsapp.database.NewsDatabaseDao
import com.ssessments.newsapp.account.RegistrationData

class AccountViewModel(val database: NewsDatabaseDao,
                       application: Application): AndroidViewModel(application){

    val myUser=database.getUser()



    fun userChangedHisRegData(newEmail:String,newPhone:String){
    //TODO napravi funkciju za reg data
    }

}
