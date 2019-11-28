package com.ssessments.newsapp.activity_notification_preferences

import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import androidx.preference.children
import com.ssessments.newsapp.R
import com.ssessments.newsapp.utilities.AllMarkets
import com.ssessments.newsapp.utilities.Services
import com.ssessments.newsapp.utilities.convertStringWithCommasToRealArray

private const val MY_TAG="ServicesNotifPrefFragme"
class ServicesNotifPrefFragment:PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.services_notif_pref_fragment,rootKey)

        setKeyAndTitleToPreferences(preferenceScreen,
            convertStringWithCommasToRealArray(enumValues<Services>().joinToString { it.value })
        )
    }

    private fun setKeyAndTitleToPreferences(screen: PreferenceScreen, list:Array<String>) {

        var index=1
        for(value in screen.children){
            try{
                value.key=list[index]
                value.title=list[index]
                index++
            }catch (e:Exception ){
                Log.i(MY_TAG,"lista za setovanje key i title out of bound")
            }
        }


    }

}