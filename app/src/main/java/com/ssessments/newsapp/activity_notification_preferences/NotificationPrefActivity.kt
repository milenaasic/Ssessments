package com.ssessments.newsapp.activity_notification_preferences

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.ssessments.newsapp.R
import com.ssessments.newsapp.database.CurrentFilter
import com.ssessments.newsapp.database.NewsDatabase
import com.ssessments.newsapp.databinding.ActivityNotificationPrefBinding
import com.ssessments.newsapp.utilities.Language


private const val  MY_TAG="MY_NOtifPrefAct"
class NotificationPrefActivity : AppCompatActivity() {

    private lateinit var binding:ActivityNotificationPrefBinding
    private lateinit var viewModel:NotifPrefActivityViewModel
    private lateinit var navController: NavController



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=DataBindingUtil.setContentView(this,R.layout.activity_notification_pref)


        val datasource= NewsDatabase.getInstance(application).newsDatabaseDao
        viewModel = ViewModelProviders.of(this,
            NotifPrefActivityViewModelFactory(datasource, application)
        ).get(NotifPrefActivityViewModel::class.java)

        setSupportActionBar(binding.prefNotifActivitytoolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navController=findNavController(R.id.notifprefActNavHostfragment)

        binding.prefNotifActivitytoolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back_white)

        }

    }


    override fun onSupportNavigateUp(): Boolean {
        if(navController.currentDestination?.id==R.id.mainNotifPrefFragment){
            Log.i(MY_TAG,"trenutna destinacija je pocetni fragment i pritisnut je navigate up}")
            sendNotificationsToServer()
            val sharedPref: SharedPreferences=PreferenceManager.getDefaultSharedPreferences(application)
            val newValues= sharedPref.all as Map<String, *>
            Log.i(MY_TAG,"new values keys su ${newValues.keys}")
        }

       return navController.navigateUp() || super.onSupportNavigateUp()
    }


    private fun sendNotificationsToServer() {
        val sharedPref: SharedPreferences=PreferenceManager.getDefaultSharedPreferences(application)
        val newValues= sharedPref.all as Map<String, *>
        Log.i(MY_TAG,"new values keys su ${newValues.keys}")
        Log.i(MY_TAG,"old saved values keys su ${viewModel.lastSavedValues.keys}")
        Log.i(MY_TAG,"new values pref jednake starim ${newValues.equals(viewModel.lastSavedValues)}")

        val langForNotif=sharedPref.getString("list_preference_language_for_notif", Language.ENGLISH.value)?: Language.ENGLISH.value
        Log.i(MY_TAG,"odabrani jezik za notif ${langForNotif}")

        if(!newValues.equals(viewModel.lastSavedValues)){
            Log.i(MY_TAG,"new values pref nisu jednake starim")
            viewModel.sendNotificationPreferencesToServer(newValues,langForNotif)
        }

    }

}
