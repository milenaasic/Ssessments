package com.ssessments.login_and_registration


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.ssessments.R
import com.ssessments.databinding.FragmentRegistration2Binding



class RegistrationFragment2 : Fragment() {

    private lateinit var binding: FragmentRegistration2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_registration2,container,false)

        //binding.backbutton.Click


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
