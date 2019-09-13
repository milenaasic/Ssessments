package com.ssessments.login_and_registration


import android.app.Activity
import android.os.Bundle
import android.util.Log
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

import com.ssessments.R
import com.ssessments.databinding.FragmentRegistration2Binding



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

        sharedViewModel.navigateBackToRegistration1.observe(this,Observer{shouldNavigate->

                    if(shouldNavigate) {
                        navController.navigateUp()
                        sharedViewModel.navigationBackToRegistration1Done()
                    }
                }
        )


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
                if (text!!.isBlank()) {
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




}
