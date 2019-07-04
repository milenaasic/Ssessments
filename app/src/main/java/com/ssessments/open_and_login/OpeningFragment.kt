package com.ssessments
.open_and_login


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ssessments.R


class OpeningFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root=inflater.inflate(R.layout.fragment_opening, container, false)

        // d systemski bar
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        return root
    }

    override fun onStart() {
        super.onStart()
        /* Handler(Looper.getMainLooper()).postDelayed({
            val extras = FragmentNavigator.Extras.Builder()
                    .addSharedElement(mimageView, "imageToTransit")
                    .build()

            navController.navigate(R.id.action_openingFragment_to_homeFragment, // Bundle of args
                    null, null, // NavOptions
                    extras)
        }, 5000)*/
    }
}
