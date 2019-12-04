package com.ssessments.newsapp.activity_notification_preferences


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.preference.*

import com.ssessments.newsapp.R
import com.ssessments.newsapp.utilities.AfricaMarkets
import com.ssessments.newsapp.utilities.RussiaMarkets
import com.ssessments.newsapp.utilities.WorldMarkets

private const val MY_TAG="MY_MainNotifPrefFragme"
class MainNotifPrefFragment : PreferenceFragmentCompat() {

    private lateinit var activityViewModel: NotifPrefActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityViewModel=requireActivity().run {
            ViewModelProviders.of(this)[NotifPrefActivityViewModel::class.java]
        }

    }

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

    override fun onStart() {
        super.onStart()

        setSummaryForMarkets()
        setSummaryForProducts()
        setSummaryForServices()

    }

    private fun setSummaryForServices() {
        var mysummary=activityViewModel.servicesSummary.value?.joinToString (separator = ",")?.trim()
        findPreference<Preference>(resources.getString(R.string.services_preferences))?.summary=mysummary
    }

    fun setSummaryForMarkets(){

    val sharedPref=PreferenceManager.getDefaultSharedPreferences(requireActivity())
        var africaSummary=""
        if(sharedPref.getBoolean("africa",true)) africaSummary=AfricaMarkets.AFRICA.value

        var russiaSummary=""
        if(sharedPref.getBoolean("russia_and_cis",true)) russiaSummary=RussiaMarkets.RUSSIA_AND_CIS.value

        var worldSummary=""
        if(sharedPref.getBoolean("world",true)) worldSummary=WorldMarkets.WORLD.value

        var mysummary=activityViewModel.asiaPacificSummary.value?.joinToString (separator = ",")?.trim()+", "+
                            africaSummary+", "+
                            activityViewModel.americasSummary.value?.joinToString (separator = ",")?.trim()+", "+
                            activityViewModel.europeSummary.value?.joinToString (separator = ",")?.trim()+", "+
                            activityViewModel.icsSummary.value?.joinToString (separator = ",")?.trim()+", "+
                            activityViewModel.middleeastSummary.value?.joinToString (separator = ",")?.trim()+", "+
                            russiaSummary+", "+worldSummary

        findPreference<Preference>(resources.getString(R.string.markets_preferences))?.summary=mysummary


    }


    fun setSummaryForProducts(){


        var mysummary=activityViewModel.plasticsSummary.value?.joinToString (separator = ",")?.trim()+", "+
                activityViewModel.chemicalsSummary.value?.joinToString (separator = ",")?.trim()+", "+
                activityViewModel.energySummary.value?.joinToString (separator = ",")?.trim()

        findPreference<Preference>(resources.getString(R.string.products_preferences))?.summary=mysummary


    }

}
