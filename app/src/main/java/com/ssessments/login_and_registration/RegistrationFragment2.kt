package com.ssessments.login_and_registration


import android.os.Bundle
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


        return binding.root
    }


}
