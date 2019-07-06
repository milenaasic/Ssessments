package com.ssessments.login


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

import com.ssessments.R


class LogIn_Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root=inflater.inflate(R.layout.fragment_log_in, container, false)
        //  i systemski bar
        activity?.apply {
        window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
         }
        val activity: AppCompatActivity = activity as AppCompatActivity
        activity.actionBar?.hide()

        // Inflate the layout for this fragment
        return root
    }


}
