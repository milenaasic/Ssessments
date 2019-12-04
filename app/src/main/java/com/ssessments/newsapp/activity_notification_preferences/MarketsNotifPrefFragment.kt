package com.ssessments.newsapp.activity_notification_preferences

import android.content.DialogInterface
import android.content.res.TypedArray
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.preference.*
import com.ssessments.newsapp.R
import com.ssessments.newsapp.utilities.*
import com.ssessments.newsapp.utilities.AsiaPacificMarkets
import androidx.lifecycle.Observer
import java.util.*


private const val MY_TAG="MY_PreferenceFragmentCo"

class MarketsNotifPrefFragment: PreferenceFragmentCompat(),Preference.OnPreferenceChangeListener {

    private lateinit var activityViewModel: NotifPrefActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityViewModel=requireActivity().run {
            ViewModelProviders.of(this)[NotifPrefActivityViewModel::class.java]
        }

    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.markets_pref_notif, rootKey)

        findPreference<Preference>("marketAsiaPacific")?.setOnPreferenceClickListener {

            findNavController().navigate(MarketsNotifPrefFragmentDirections.actionMarketsNotifPrefFragmentToAsiaPacificMarkets())
            true
        }

        findPreference<Preference>("marketAmericas")?.setOnPreferenceClickListener {

            findNavController().navigate(MarketsNotifPrefFragmentDirections.actionMarketsNotifPrefFragmentToAmericasMarkets())
            true
        }

        findPreference<Preference>("marketEurope")?.setOnPreferenceClickListener {

            findNavController().navigate(MarketsNotifPrefFragmentDirections.actionMarketsNotifPrefFragmentToEuropeMarkets())
            true
        }

        findPreference<Preference>("marketICS")?.setOnPreferenceClickListener {

            findNavController().navigate(MarketsNotifPrefFragmentDirections.actionMarketsNotifPrefFragmentToICSMarkets())
            true
        }

        findPreference<Preference>("marketMiddleEast")?.setOnPreferenceClickListener {

            findNavController().navigate(MarketsNotifPrefFragmentDirections.actionMarketsNotifPrefFragmentToMiddleEastMarkets())
            true
        }

        findPreference<CheckBoxPreference>("africa")?.apply {

            setOnPreferenceChangeListener(this@MarketsNotifPrefFragment)

         }

        findPreference<CheckBoxPreference>("russia_and_cis")?.apply {

            setOnPreferenceChangeListener(this@MarketsNotifPrefFragment)

        }

        findPreference<CheckBoxPreference>("world")?.apply {

            key=WorldMarkets.WORLD.value.trim().toLowerCase(Locale.US)
            setOnPreferenceChangeListener(this@MarketsNotifPrefFragment)

        }


        Log.i(MY_TAG,"africa key je ${findPreference<CheckBoxPreference>("africa")?.key}")


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activityViewModel.asiaPacificSummary.observe(this, Observer {
                Log.i(MY_TAG,"observe asia pacific summary iz view modela ${it}")
                findPreference<Preference>("marketAsiaPacific")?.summary=it.joinToString(separator = ",").trim()
        })

        activityViewModel.americasSummary.observe(this, Observer {
            findPreference<Preference>("marketAmericas")?.summary=it.joinToString(separator = ",").trim()

        })

        activityViewModel.europeSummary.observe(this, Observer {
            findPreference<Preference>("marketEurope")?.summary=it.joinToString(separator = ",").trim()

        })

        activityViewModel.icsSummary.observe(this, Observer {
            findPreference<Preference>("marketICS")?.summary=it.joinToString(separator = ",").trim()

        })

        activityViewModel.middleeastSummary.observe(this, Observer {
            findPreference<Preference>("marketMiddleEast")?.summary=it.joinToString(separator = ",").trim()

        })

    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        val value=newValue as Boolean
        if (!value){
            openConfirmDialog(preference)
        }
        return true
    }

    private fun openConfirmDialog(preference: Preference?) {

        val alertDialog= AlertDialog.Builder(requireActivity(), R.style.MyAlertDialogTheme )
            .setTitle(R.string.turnOffNotification)
            .setPositiveButton("YES", DialogInterface.OnClickListener{ dialog, id ->
            })
            .setNegativeButton("CANCEL", DialogInterface.OnClickListener{ dialog, id ->
                val checkBox=preference as CheckBoxPreference
                checkBox.isChecked=true
            })
            .setCancelable(false)

        alertDialog.show()

    }



}
