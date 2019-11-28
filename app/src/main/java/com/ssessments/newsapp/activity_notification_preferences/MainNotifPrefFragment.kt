package com.ssessments.newsapp.activity_notification_preferences


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

import com.ssessments.newsapp.R

class MainNotifPrefFragment : PreferenceFragmentCompat() {


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_notif_pref_fragment, rootKey)

        val listPreference=preferenceScreen.findPreference<ListPreference>("list_preference_language_for_notif")
        listPreference?.summaryProvider= ListPreference.SimpleSummaryProvider.getInstance()



        findPreference<Preference>(resources.getString(R.string.markets_preferences))?.setOnPreferenceClickListener {

            findNavController().navigate(MainNotifPrefFragmentDirections.actionMainNotifPrefFragmentToMarketsNotifPrefFragment())
            true
         }

        findPreference<Preference>(resources.getString(R.string.products_preferences))?.setOnPreferenceClickListener {

            findNavController().navigate(MainNotifPrefFragmentDirections.actionMainNotifPrefFragmentToProductsNotifPrefFragment())
            true
        }

        findPreference<Preference>(resources.getString(R.string.services_preferences))?.setOnPreferenceClickListener {

            findNavController().navigate(MainNotifPrefFragmentDirections.actionMainNotifPrefFragmentToServicesNotifPrefFragment())
            true
        }
    }






}
