package com.ssessments.newsapp.preferences


import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.preference.*
import com.ssessments.newsapp.MainActivityViewModel
import com.ssessments.newsapp.R
import com.ssessments.newsapp.database.CurrentFilter
import com.ssessments.newsapp.utilities.*


private const val MYTAG="PREFERENCE FRAGMRNT"

class PreferenceFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var lastSavedValues:Map<String,Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        mainActivityViewModel=requireActivity().run {
            ViewModelProviders.of(this)[MainActivityViewModel::class.java]
        }

        lastSavedValues =PreferenceManager.getDefaultSharedPreferences(requireActivity()).all as Map<String, Boolean>

    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_notifications, rootKey)


        val prefMarkets=preferenceScreen.findPreference<PreferenceCategory>("markets")
        val prefproducts=preferenceScreen.findPreference<PreferenceCategory>("products")
        val prefSsessments=preferenceScreen.findPreference<PreferenceCategory>("ssessments")

       if(prefMarkets!=null){
            for(index in 0..prefMarkets.preferenceCount-1){
                prefMarkets.getPreference(index).title=Markets.values()[index+1].value

            }
        }

        if(prefproducts!=null){
            for(index in 0..prefproducts.preferenceCount-1){
                prefproducts.getPreference(index).title= Products.values()[index+1].value

            }
        }

        if(prefSsessments!=null){
            for(index in 0..prefSsessments.preferenceCount-1){
                prefSsessments.getPreference(index).title= Ssessments.values()[index+1].value

            }
        }

        for (categoryindex in 0..preferenceScreen.preferenceCount-1){
            val pref=preferenceScreen.getPreference(categoryindex) as PreferenceCategory
                for(index in 0..pref.preferenceCount-1){
                    pref.getPreference(index).setOnPreferenceChangeListener(this)
                }
        }

    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.notifications_menu,menu)
        menu.findItem(R.id.filter_menu_item).setVisible(false)
        menu.findItem(R.id.action_search).setVisible(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId==R.id.filterNews){
            val currentFilter=getCurrentPreferences()
            mainActivityViewModel.setCurrentFilterAccordingToNotifications(currentFilter)
            findNavController().navigateUp()
            return true
        }else return super.onOptionsItemSelected(item)
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
                        .setNegativeButton("CANCEL",DialogInterface.OnClickListener{ dialog, id ->
                                        val checkBox=preference as CheckBoxPreference
                                        checkBox.isChecked=true
                        })
                        .setCancelable(false)

        alertDialog.show()

    }


    private fun getCurrentPreferences():CurrentFilter{
        return CurrentFilter(
        market = convertArrayListToStringWithCommas(getCheckedMarkets()),
        product = convertArrayListToStringWithCommas(getCheckedProducts()),
        ssessment = convertArrayListToStringWithCommas(getCheckedSsessments()),
        language = Language.ENGLISH.value
        )

    }

    private fun getCheckedMarkets():ArrayList<String>{

        val allEntries=PreferenceManager.getDefaultSharedPreferences(requireActivity()).all

        var list=ArrayList<String>()

        for (entry in allEntries.entries) {

            if(entry.value==true) {

                val checkBoxTitle = preferenceScreen.findPreference<CheckBoxPreference>(entry.key)?.title

                for (values in Markets.values()) {

                    if (values.value.equals(checkBoxTitle.toString(), true)) {
                        list.add(checkBoxTitle.toString())

                    }
                }
            }
        }
        return list
    }

    private fun getCheckedProducts():ArrayList<String>{

        val allEntries=PreferenceManager.getDefaultSharedPreferences(requireActivity()).all

        var list=ArrayList<String>()

        for (entry in allEntries.entries) {

            if(entry.value==true) {
                val checkBoxTitle = preferenceScreen.findPreference<CheckBoxPreference>(entry.key)?.title
                for (values in Products.values()) {
                    if (values.value.equals(checkBoxTitle.toString(), true)) {
                        list.add(checkBoxTitle.toString())

                    }
                }
            }
        }
        return list
    }

    private fun getCheckedSsessments():ArrayList<String>{

        val allEntries=PreferenceManager.getDefaultSharedPreferences(requireActivity()).all

        var list=ArrayList<String>()

        for (entry in allEntries.entries) {

            if(entry.value==true) {

                val checkBoxTitle = preferenceScreen.findPreference<CheckBoxPreference>(entry.key)?.title
                for (values in Ssessments.values()) {

                    if (values.value.equals(checkBoxTitle.toString(), true)) {
                        list.add(checkBoxTitle.toString())

                    }
                }
            }
        }
        return list
    }

    override fun onStop() {
        super.onStop()
        sendNotificationsToServer()

    }

    private fun sendNotificationsToServer() {

        val newValues=PreferenceManager.getDefaultSharedPreferences(requireActivity()).all
        if(!newValues.equals(lastSavedValues)){
            mainActivityViewModel.sendNotificationPreferencesToServer(newValues)
         }

    }


}
