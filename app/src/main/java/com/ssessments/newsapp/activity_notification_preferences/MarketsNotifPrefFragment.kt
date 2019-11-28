package com.ssessments.newsapp.activity_notification_preferences

import android.content.res.TypedArray
import android.os.Bundle
import android.util.Log
import androidx.preference.EditTextPreference
import androidx.preference.MultiSelectListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.ssessments.newsapp.R
import com.ssessments.newsapp.utilities.AsiaPacificMarkets
import com.ssessments.newsapp.utilities.convertStringWithCommasToRealArray


private const val MY_TAG="MY_PreferenceFragmentCo"

class MarketsNotifPrefFragment: PreferenceFragmentCompat() {


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.markets_pref_notif, rootKey)

        val marketsAsiaPac=preferenceScreen.findPreference<MultiSelectListPreference>("marketsAsiaPacific")
        val asipaPacificCharSequence=convertStringWithCommasToRealArray(enumValues<AsiaPacificMarkets>().joinToString{ it.value })
        marketsAsiaPac?.apply {
            //setEntries(asipaPacificCharSequence)
            //setEntryValues(asipaPacificCharSequence)
            //setDefaultValue(resources.getStringArray(R.array.multilist))

         }

        val map=PreferenceManager.getDefaultSharedPreferences(activity).all
        Log.i(MY_TAG, "u preferences su ${map.values.toString()}")




    }



}
