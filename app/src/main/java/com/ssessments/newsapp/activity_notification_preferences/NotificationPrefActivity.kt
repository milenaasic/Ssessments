package com.ssessments.newsapp.activity_notification_preferences

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.ssessments.newsapp.R
import com.ssessments.newsapp.databinding.ActivityNotificationPrefBinding

class NotificationPrefActivity : AppCompatActivity() {

    private lateinit var binding:ActivityNotificationPrefBinding
    private lateinit var viewModel:NotifPrefActivityViewModel
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=DataBindingUtil.setContentView(this,R.layout.activity_notification_pref)
        viewModel=ViewModelProviders.of(this)[NotifPrefActivityViewModel::class.java]


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
