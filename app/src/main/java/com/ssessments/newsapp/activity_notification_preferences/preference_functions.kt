package com.ssessments.newsapp.activity_notification_preferences

import android.util.Log
import androidx.preference.PreferenceScreen
import androidx.preference.children
import java.util.*

private const val MY_TAG="MY_Preference_funtions"
fun setTitleToPreferences(screen: PreferenceScreen, list:Array<String>) {

    var index=1
    for(value in screen.children){
        try{
            value.title=list[index]
            index++
        }catch (e:Exception ){
            Log.i(MY_TAG,"lista za setovanje key i title out of bound, ${e.message}")
        }
    }


}