package com.ssessments.newsapp.login_and_registration


import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

import com.ssessments.newsapp.R
import com.ssessments.newsapp.databinding.FragmentRegistration2Binding
import kotlinx.android.synthetic.main.fragment_registration2.*


class RegistrationFragment2 : Fragment() {

    private lateinit var binding: FragmentRegistration2Binding
    private lateinit var sharedViewModel:RegistrationSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_registration2,container,false)

        sharedViewModel = requireActivity().run {
            ViewModelProviders.of(this)[RegistrationSharedViewModel::class.java]
        }

        //NAVIG CONTROLER
        val navController=findNavController()
        //da se ne otvori prvi edittext sam od sebe
        binding.constraintLayoutusername.requestFocus()

    //zatvori softkeyboard ako je user rekao DONE
        binding.confirmPasswordeditText.setOnEditorActionListener { v, actionId, event ->
            when (actionId){
                EditorInfo.IME_ACTION_DONE-> {

                    val inputMethodManager = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(binding.confirmPasswordeditText.windowToken, 0)
                    binding.confirmPasswordeditText.clearFocus()
                    true
                }

                else->false
            }
        }

        //BACK BUTTON
        binding.backbutton.setOnClickListener {
            binding.constraintLayoutusername.requestFocus()
            sharedViewModel.backButtonClicked()

        }

        //REGISTER BUTTON
        binding.registerbutton.setOnClickListener{
            if(checkIfRequiredFieldsAreEmpty()) sharedViewModel.registerButtonClicked(binding.chooseUsernameeditText.toString(),binding.passwordeditText.toString())
        }

        sharedViewModel.navigateBackToRegistration1.observe(this,Observer{shouldNavigate->
                    if(shouldNavigate) {
                        navController.navigateUp()
                        sharedViewModel.navigationBackToRegistration1Done()
                    }
                }
        )

        sharedViewModel.showProgressBarRegistration.observe(this,Observer{
                when(it){
                    true-> progressBarVisible(true)
                    false->progressBarVisible(false)}
        })

        sharedViewModel.showToastRegistrationSent.observe(this,Observer{
            if(it){
                Snackbar.make(binding.constraintLayoutusername,R.string.registration_sent,Snackbar.LENGTH_LONG).show()
                sharedViewModel.toastRegistrationSentIsShown()
            }
        })

        sharedViewModel.showToastSomethingWentWrongWithRegistration.observe(this,Observer{
            if(it){
                Snackbar.make(binding.constraintLayoutusername,R.string.network_error,Snackbar.LENGTH_LONG).show()
                sharedViewModel.toastSomethingWentWrongWithRegistrationIsShown()
            }
        })

        return binding.root
    }

    private fun checkIfRequiredFieldsAreEmpty():Boolean {

        var registerUser = true

        binding.apply {

            chooseUsernameeditText.apply {
                if (text!!.isBlank()) {
                    setError("reqired field")
                    registerUser = false
                }
            }

            passwordeditText.apply {
                if (text!!.isBlank() || text!!.isEmpty()) {
                    setError("reqired field")
                    registerUser = false
                }
                if(!isPasswordValid(text!!.toString())){
                    setError(resources.getString(R.string.invalid_password))
                    registerUser = false
                }


            }

            confirmPasswordeditText.apply {
                if (text!!.isBlank()) {
                    setError("reqired field")
                    registerUser= false
                }

                if (binding.passwordeditText.text!!.isBlank() || !text.toString().equals(binding.passwordeditText.text.toString())) {
                    setError("confirm password")
                    registerUser = false
                }

            }
        }
        return registerUser
    }

    private fun progressBarVisible(show: Boolean){
        if(show) {
            binding.apply {
                constraintLayoutusername.alpha = 0.3F
                progressBarRegistration.visibility = View.VISIBLE
            }
        }

        if(!show){
            binding.apply {
                constraintLayoutusername.alpha = 1F
                progressBarRegistration.visibility = View.GONE
            }
        }

    }


}
