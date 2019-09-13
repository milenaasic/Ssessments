package com.ssessments.filter_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ssessments.R

private const val MY_TAG="Activity_Filter"

class FilterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        Log.i(MY_TAG,"oncreate")
    }
}
