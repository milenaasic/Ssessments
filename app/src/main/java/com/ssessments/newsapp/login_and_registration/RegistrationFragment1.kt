package com.ssessments.newsapp.login_and_registration


import android.app.Activity
import android.os.Bundle
import android.telephony.PhoneNumberUtils
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

import com.ssessments.newsapp.R
import com.ssessments.newsapp.databinding.FragmentRegistration1Binding


class RegistrationFragment1 : Fragment() {

    private lateinit var binding:FragmentRegistration1Binding
    private lateinit var sharedViewModel:RegistrationSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_registration1,container,false)
        sharedViewModel = requireActivity().run {
            ViewModelProviders.of(this)[RegistrationSharedViewModel::class.java]
        }

        //da se ne otvoti prvi edittext sam od sebe
        binding.constrlayoutForText.requestFocus()

        //ukoliko se podize vec postojeci fragment koji je bio unisten, i view model je takodje bio unisten
         if(savedInstanceState!=null){
                if(checkIfRequiredFieldsAreEmpty())sharedViewModel.setMyUserRegistration1Fields(getEnteredfieldValues())
                Log.i("RegFragment1","entered values ${getEnteredfieldValues()}")
         }

        binding.nextbutton.setOnClickListener {

                binding.constrlayoutForText.requestFocus()

                if(checkIfRequiredFieldsAreEmpty()) {
                    sharedViewModel.apply {
                        setMyUserRegistration1Fields( getEnteredfieldValues())
                        nextButtonClicked()
                     }
                }
        }

        binding.countryedittext.setOnEditorActionListener { v, actionId, event ->
            when (actionId){
                EditorInfo.IME_ACTION_DONE-> {

                    val inputMethodManager = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(binding.countryedittext.windowToken, 0)
                    binding.countryedittext.clearFocus()
                    true
                }

                else->false
                }
         }

        //NAVIG CONTROLER
        val navController=findNavController()

        //NAVIGATE TO REGISTRATION 2
        sharedViewModel.navigateToRegistration2.observe(this, Observer{ shouldNavigate->
            if(shouldNavigate) {
                navController.navigate(RegistrationFragment1Directions.actionRegistrationFragment1ToRegistrationFragment2())
                sharedViewModel.navigationToregistration2Done()
            }
        })



        return binding.root
    }

    private fun checkIfRequiredFieldsAreEmpty():Boolean {

        var navigateToSecondPage=true

        binding.apply{

            firstNameeditText.apply {
                if (text!!.isBlank()) {
                setError("reqired field")
                navigateToSecondPage=false}
            }

            lastNameeditText.apply{
                if (text!!.isBlank()) {
                    setError("reqired field")
                    navigateToSecondPage=false
                    }
            }

            emaileditText.apply {
                if (text!!.isBlank()) {
                    setError("reqired field")
                    navigateToSecondPage=false}
                if(!(text!!.contains("@")&& text!!.contains("."))) {
                    setError("not a valid e-mail address")
                    navigateToSecondPage=false}

            }

            confirmemaileditText.apply {
                if(text!!.isBlank()){
                    setError("reqired field")
                    navigateToSecondPage=false
                    }

                if( binding.emaileditText.text!!.isBlank()|| !text.toString().equals(binding.emaileditText.text.toString())){
                    setError ("confirm e-mail")
                    navigateToSecondPage=false
                    }

            }

            mobilePhoneeditText.apply {

                if(text!!.isBlank()) {
                    setError("reqired field")
                    navigateToSecondPage=false}

                if(!PhoneNumberUtils.isGlobalPhoneNumber(text.toString())){
                    setError("not a valid phone number!")
                    navigateToSecondPage=false
                    }

            }

            compoanyNameeditText.apply{
                if (text!!.isBlank()) {
                setError("reqired field")
                navigateToSecondPage=false
                }
            }



        }

        return navigateToSecondPage

    }

    private fun getEnteredfieldValues():ArrayList<String>{

        var list= ArrayList<String>(6)

         binding.apply {

            list.apply {

                add(firstNameeditText.text.toString())
                add(lastNameeditText.text.toString())
                add(confirmemaileditText.text.toString())
                add(mobilePhoneeditText.text.toString())
                if(compoanyNameeditText.text.isNullOrBlank()) add("") else add(compoanyNameeditText.text.toString())
                add(countryedittext.text.toString())
                }

         }
        return list
    }






}
