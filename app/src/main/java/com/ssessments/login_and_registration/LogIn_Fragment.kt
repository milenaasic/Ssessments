package com.ssessments.login_and_registration


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

import com.ssessments.R
import com.ssessments.databinding.FragmentLogInBinding
import kotlinx.android.synthetic.main.fragment_log_in.*

private const val TAG_LOGIN="MY_LOGIN_FRAGMENT"

class LogIn_Fragment : Fragment() {

    private lateinit var binding:FragmentLogInBinding
    private lateinit var viewModel:LogIn_ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_log_in,container,false)
        Log.i(TAG_LOGIN,"on Create VIew")

        viewModel=ViewModelProviders.of(this).get(LogIn_ViewModel::class.java)
        val navController=this.findNavController()

        //SIGN IN
        binding.signInButton.setOnClickListener{
            if(isUsernameValid(binding.usernameEditText.toString())&& isPasswordValid(binding.passwordEditText.toString()))
            viewModel.signInButtonPressed(binding.usernameEditText.toString(),binding.passwordEditText.toString())
        }

        //SIGN UP
        binding.signUpButton.setOnClickListener{
                viewModel.signUpButtomPressed()
        }

        //NAVIGATE TO REGISTRATION
        viewModel.navigateToRegistration.observe(this,Observer{shouldNavigate->
             if(shouldNavigate) {
                navController.navigate(R.id.action_logIn_Fragment_to_registrationFragment)
                viewModel.navigationDone()
             }
        })


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


        return binding.root

    }


    private fun isPasswordValid(password:String):Boolean{
        return password.length > 5
    }

    private fun isUsernameValid(username:String):Boolean{
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
