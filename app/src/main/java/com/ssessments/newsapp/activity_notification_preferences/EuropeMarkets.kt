package com.ssessments.newsapp.activity_notification_preferences

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.children
import com.ssessments.newsapp.R
import com.ssessments.newsapp.utilities.AmericasMarkets
import com.ssessments.newsapp.utilities.EuropeMarkets
import com.ssessments.newsapp.utilities.convertStringWithCommasToRealArray

class EuropeMarkets: PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {
    private lateinit var activityViewModel: NotifPrefActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityViewModel=requireActivity().run {
            ViewModelProviders.of(this)[NotifPrefActivityViewModel::class.java]
        }

    }
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.markets_europe, rootKey)

        setTitleToPreferences(
            preferenceScreen,
            convertStringWithCommasToRealArray(enumValues<EuropeMarkets>().joinToString { it.value })
        )

        //set change listenere
        for (value in preferenceScreen.children){
            value.setOnPreferenceChangeListener(this)
        }

    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        val value=newValue as Boolean
        if (!value){
            openConfirmDialog(preference)
        }
        return true
    }

    private fun openConfirmDialog(preference: Preference?) {

        val alertDialog= AlertDialog.Builder(requireActivity(),R.style.MyAlertDialogTheme )
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

    override fun onStop() {
        super.onStop()
        activityViewModel.setEuropeSummaryFromFragment(convertStringWithCommasToRealArray(enumValues<EuropeMarkets>().joinToString {it.value}))

    }

}