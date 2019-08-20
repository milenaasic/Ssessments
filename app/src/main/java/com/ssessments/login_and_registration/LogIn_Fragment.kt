package com.ssessments.login_and_registration


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ssessments.*

import com.ssessments.database.NewsDatabase
import com.ssessments.databinding.FragmentLogInBinding
import kotlinx.android.synthetic.main.fragment_log_in.*

private const val TAG_LOGIN="MY_LOGIN_FRAGMENT"
const val LOGGED_IN_USER_GOTO_MAIN_ACTIVITY="LOGGED_IN_USER_GOTO_MAIN_ACTIVITY"

class LogIn_Fragment : Fragment() {

    private lateinit var binding:FragmentLogInBinding
    private lateinit var viewModel:LogIn_ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_log_in,container,false)
        Log.i(TAG_LOGIN,"on Create VIew")

        val application= requireNotNull(this.activity).application
        val datasource= NewsDatabase.getInstance(application).newsDatabaseDao
        viewModel = ViewModelProviders.of(this, LogInViewModelFactory(datasource,application))
            .get(LogIn_ViewModel::class.java)

        val navController=this.findNavController()

        //SIGN IN
        binding.signInButton.setOnClickListener{
            if(!isUsernameValid(binding.usernameEditText.text.toString())){
                binding.usernameEditText.setError(resources.getString(R.string.invalid_username))
            }else if(!isPasswordValid(binding.passwordEditText.text.toString())){
                binding.passwordEditText.setError(resources.getString(R.string.invalid_password))
            }else {
                Log.i(TAG_LOGIN,"username ${binding.usernameEditText.text.toString()}")
                Log.i(TAG_LOGIN,"password ${binding.passwordEditText.text.toString()}")
                viewModel.signInButtonPressed(binding.usernameEditText.text.toString(), binding.passwordEditText.text.toString())
            }
        }

        /*//SIGN UP
        binding.signUpButton.setOnClickListener{
                viewModel.signUpButtomPressed()
        }

        //NAVIGATE TO REGISTRATION
        viewModel.navigateToRegistration.observe(this,Observer{shouldNavigate->
             if(shouldNavigate) {
                navController.navigate(R.id.action_logIn_Fragment_to_registrationFragment)
                viewModel.navigationDone()
             }
        })*/


        viewModel.showProgressBar.observe(this, Observer {
                when(it){
                true-> progressBarVisible(true)
                false->progressBarVisible(false)}

         })

        viewModel.showToastNOInternet.observe(this,Observer{
            if(it){
            Toast.makeText(requireActivity(),"No Internet Connection",Toast.LENGTH_LONG).show()
            viewModel.toastNoInternetIsShown()
            }
        })

        viewModel.showToastUserLoggedIN.observe(this,Observer{
            if(it){
                Toast.makeText(requireActivity(),"You are logged in",Toast.LENGTH_LONG).show()
                viewModel.toastLoggedInUserIsShown()
            }
        })

        viewModel.sendUserToHomeFragment.observe(this,Observer{
            if(it){
                val intent= Intent(requireActivity(),MainActivity::class.java).apply {
                    putExtra( LOGGED_IN_USER_GOTO_MAIN_ACTIVITY,true)}
                    startActivity(intent)

                viewModel.userSentToHomeFragment()
            }
        })


        return binding.root

    }


    private fun isPasswordValid(password:String):Boolean{
        Log.i(TAG_LOGIN,"${password.length}")
        Log.i(TAG_LOGIN,"da li je duza od 5: ${password.length>5}")
        return password.length > 5
    }

    private fun isUsernameValid(username:String):Boolean{
        Log.i(TAG_LOGIN,"prazan user: ${username.isNotBlank()}")
       return username.isNotBlank()
    }


    private fun progressBarVisible(show: Boolean){
       if(show) {
           binding.apply {
               rootConstLayout.alpha = 0.3F
               myprogressBar.visibility = View.VISIBLE
           }
       }

       if(!show){
           binding.apply {
               rootConstLayout.alpha = 1F
               myprogressBar.visibility = View.GONE
           }
       }

    }



    override fun onStart() {
        super.onStart()
        Log.i(TAG_LOGIN,"onstart")

    }

    override fun onStop() {
        super.onStop()

    }
}
