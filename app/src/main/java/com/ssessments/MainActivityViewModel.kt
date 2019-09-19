package com.ssessments

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.ssessments.database.CurrentFilter
import com.ssessments.database.NewsDatabaseDao
import com.ssessments.database.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val MY_TAG="MY_MainActivViewModel"

class MainActivityViewModel(val database:NewsDatabaseDao,
                            application: Application): AndroidViewModel(application){


    var loggedInUser=database.getUser()


    fun clearUser(){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.clearUserDataTable()
                Log.i(MY_TAG, "claer user table}")

            }
        }

    }

    fun setCurrentFilterAccordingToNotifications(currentFilter:CurrentFilter){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.clearCurrentFilterTable()
                database.insertCurrentFilterToDatabase(currentFilter)
                Log.i(MY_TAG, "claer filter table}")

            }
        }

    }


}