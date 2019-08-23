package com.ssessments

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.ssessments.database.NewsDatabaseDao
import com.ssessments.database.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val MY_TAG="MY_MainActivViewModel"

class MainActivityViewModel(val database:NewsDatabaseDao,
                            application: Application): AndroidViewModel(application){

    /*private val _loggedInUser=MutableLiveData<UserData?>()
    val loggedInUser:LiveData<UserData?>
        get()=_loggedInUser*/

    var loggedInUser=database.getUser()


    fun clearUser(){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.clearUserDataTable()
                Log.i(MY_TAG, "claer user table}")

            }
        }

    }



    /*fun getUserFromDatabase() {

        var n: Int = -1
        var myuser: List<UserData>? = null

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                n = database.getNumberOfUsers()
                Log.i(MY_TAG, "broj korisnika je ${n}")
                if(n==1) myuser = database.getUser()
                Log.i(MY_TAG," getfromdatabase ${myuser}")
                _loggedInUser.value=myuser?.get(0)
            }
        }


    }*/



}