package com.ssessments.login_and_registration


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.ssessments.R
import com.ssessments.databinding.FragmentRegistration1Binding


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

        binding.nextbutton.setOnClickListener {
                sharedViewModel.nextButtonClicked() }

        //NAVIG CONTROLER
        val navController=findNavController()

        //NAVIGATE TO REGISTRATION 2
        sharedViewModel.navigateToRegistration2.observe(this, Observer{ shouldNavigate->
            if(shouldNavigate) {
                navController.navigate(RegistrationFragment1Directions.actionRegistrationFragment1ToRegistrationFragment2())
                sharedViewModel.navigationDone()
            }
        })



        return binding.root
    }


}
