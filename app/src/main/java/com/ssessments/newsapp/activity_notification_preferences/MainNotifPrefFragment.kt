package com.ssessments.newsapp.activity_notification_preferences


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.preference.*

import com.ssessments.newsapp.R
import com.ssessments.newsapp.database.CurrentFilter
import com.ssessments.newsapp.utilities.*
import com.ssessments.newsapp.utilities.ICSMarkets

private const val MY_TAG="MY_MainNotifPrefFrag"
class MainNotifPrefFragment : PreferenceFragmentCompat() {

    private lateinit var activityViewModel: NotifPrefActivityViewModel
    private lateinit var sharedPref:SharedPreferences
    private lateinit var lastSavedValues:Map<String,Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityViewModel=requireActivity().run {
            ViewModelProviders.of(this)[NotifPrefActivityViewModel::class.java]
        }
        setHasOptionsMenu(true)

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lastSavedValues =activityViewModel.lastSavedValues

        activityViewModel.servicesSummary.observe(this, Observer {arrayListServices->

            val pref=findPreference<Preference>(resources.getString(R.string.services_preferences))

            pref?.summary=
                if(arrayListServices.isEmpty()) NOTHING_SELECTED_IN_NOTIFICATIONS
                else arrayListServices.joinToString { it }.trim()

         })

         activityViewModel.loggedInUser.observe(this, Observer {
            Log.i(MY_TAG,"OBSERVER USER JE $it")
          })

        activityViewModel.finishAndGoToMain.observe(this, Observer{
            if(it){ activityViewModel.finishAndGoToMainOver()
                    requireActivity().finish()}

        })

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPref=PreferenceManager.getDefaultSharedPreferences(requireActivity())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.notif_activity_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.filter_news_to_notif->{
                val filter=getCurrentNotifications()
                activityViewModel.setCurrentFilterAccordingToNotifications(filter)
                return true}

            else-> return false
        }

    }

    private fun getCurrentNotifications(): CurrentFilter {

        val markets=getMarkets()
        Log.i(MY_TAG," marketi su $markets")
        return CurrentFilter(market = getMarkets().joinToString {it},
                            product = getProducts().joinToString {it},
                            ssessment = getServices().joinToString{it},
                            language=getChosenLanguage())
    }

    private fun getChosenLanguage(): String {

        return sharedPref.getString("list_preference_language_for_notif",Language.ENGLISH.value)?:Language.ENGLISH.value
    }


    private fun getMarkets(): ArrayList<String> {

        val list=ArrayList<String>()
        list.addAll(activityViewModel.asiaPacificSummary.value!!)
        if(sharedPref.getBoolean("africa",true)) list.add(AfricaMarkets.AFRICA.value)
        list.apply {
            addAll(activityViewModel.americasSummary.value!!)
            addAll(activityViewModel.europeSummary.value!!)
            addAll(activityViewModel.icsSummary.value!!)
            addAll(activityViewModel.middleeastSummary.value!!)

        }

        if(sharedPref.getBoolean("russia_and_cis",true)) list.add(RussiaMarkets.RUSSIA_AND_CIS.value)
        if(sharedPref.getBoolean("world",true)) list.add(WorldMarkets.WORLD.value)

        return list
    }


    private fun getProducts():ArrayList<String> {

        val list=ArrayList<String>()
        list.apply {
            addAll(activityViewModel.plasticsSummary.value!!)
            addAll(activityViewModel.chemicalsSummary.value!!)
            addAll(activityViewModel.energySummary.value!!)

        }
        return list
    }

    private fun getServices():ArrayList<String> {

        val list=ArrayList<String>()
        list.addAll(activityViewModel.servicesSummary.value!!)

        return list
    }



    override fun onStart() {
        super.onStart()
        setSummaryForMarkets()
        setSummaryForProducts()

    }



    fun setSummaryForMarkets(){
        val list =getMarkets()
        Log.i(MY_TAG,"markets summary iz setSummfor markets je ${getMarkets().joinToString { it }}")
        findPreference<Preference>(resources.getString(R.string.markets_preferences))?.summary=if(list.isEmpty()) NOTHING_SELECTED_IN_NOTIFICATIONS
                                                                    else list.joinToString { it }.trim()

    }


    fun setSummaryForProducts(){
        val list=getProducts()
        findPreference<Preference>(resources.getString(R.string.products_preferences))?.summary= if(list.isEmpty()) NOTHING_SELECTED_IN_NOTIFICATIONS
                                                                        else list.joinToString { it }.trim()


    }

    override fun onStop() {
        super.onStop()
        sendNotificationsToServer()
        Log.i(MY_TAG,"on stop")

    }

    private fun sendNotificationsToServer() {

        val newValues=PreferenceManager.getDefaultSharedPreferences(requireActivity()).all as Map<String, Boolean>

        Log.i(MY_TAG,"new values pref jednake starim ${newValues.equals(lastSavedValues)}")


        if(!newValues.equals(lastSavedValues)){
            Log.i(MY_TAG,"new values pref nisu jednake starim")
            activityViewModel.sendNotificationPreferencesToServer(newValues)
        }

    }

}
