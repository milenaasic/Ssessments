package com.ssessments.login_and_registration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.ssessments.R
import com.ssessments.START_LOG_REGISTRATION_ACTIVITY_MESSAGE
import com.ssessments.database.NewsDatabase
import com.ssessments.filter_fragments.FilterPagerSupportSharedViewModel
import com.ssessments.filter_fragments.FilterPagerSupportSharedViewModelFactory

private const val TAG="MY_LogInRegisActivity"
class LogIn_and_Registration_Activity : AppCompatActivity() {

    lateinit var viewModel: RegistrationSharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "on create")

        setContentView(R.layout.activity_log_in_and__registration_)

        if (savedInstanceState == null) {
            val navigatetofragment = intent?.extras?.get(START_LOG_REGISTRATION_ACTIVITY_MESSAGE)
            val navController = findNavController(R.id.login_navhost_fragment)
            when (navigatetofragment) {
                0 -> { }
                1 -> {
                    navController.navigate(LogIn_FragmentDirections.actionLogInFragmentToRegistrationFragment())
                }
            }
        }


        val datasource= NewsDatabase.getInstance(application).newsDatabaseDao
        viewModel = ViewModelProviders.of(this, RegistrationSharedViewModelFactory(datasource,application))
            .get(RegistrationSharedViewModel::class.java)




    }
}
