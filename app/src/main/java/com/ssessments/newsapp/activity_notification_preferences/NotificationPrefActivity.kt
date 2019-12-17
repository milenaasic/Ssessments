package com.ssessments.newsapp.activity_notification_preferences

import android.content.Intent
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
import com.ssessments.newsapp.R
import com.ssessments.newsapp.database.CurrentFilter
import com.ssessments.newsapp.database.NewsDatabase
import com.ssessments.newsapp.databinding.ActivityNotificationPrefBinding


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
       return navController.navigateUp() || super.onSupportNavigateUp()
    }



}
