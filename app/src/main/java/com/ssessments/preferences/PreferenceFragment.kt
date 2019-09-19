package com.ssessments.preferences


import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.preference.*
import com.ssessments.MainActivityViewModel
import com.ssessments.R
import com.ssessments.database.CurrentFilter
import com.ssessments.utilities.*
import java.util.prefs.PreferenceChangeEvent
import java.util.prefs.PreferenceChangeListener



private const val MYTAG="PREFERENCE FRAGMRNT"

class PreferenceFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    private lateinit var mainActivityViewModel:MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        mainActivityViewModel=requireActivity().run {
            ViewModelProviders.of(this)[MainActivityViewModel::class.java]
        }

    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_notifications, rootKey)

        Log.i(MYTAG,"broj kategorija je ${preferenceScreen.preferenceCount}")

    //SETUJEM title programski da ne bih gresila u stringovima
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
            //pokupi setovane notifikacije i postavi current filter
            val currentFilter=getCurrentPreferences()
            Log.i(MYTAG,"current filter iz preferences je $currentFilter")
            mainActivityViewModel.setCurrentFilterAccordingToNotifications(currentFilter)
            findNavController().navigateUp()
            return true
        }else return super.onOptionsItemSelected(item)
    }


    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        val value=newValue as Boolean
        Log.i(MYTAG,"new value je $newValue")
        Log.i(MYTAG,"preference key je ${preference?.key}")

        if (!value){ Log.i(MYTAG,"usao u open dialog")
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
        language = Language.ENGLISH.value,
        dateFrom = DATE_SELECT_TEXT,
        dateTo = DATE_SELECT_TEXT)

    }

    private fun getCheckedMarkets():ArrayList<String>{

        val allEntries=PreferenceManager.getDefaultSharedPreferences(requireActivity()).all

        var list=ArrayList<String>()

        for (entry in allEntries.entries) {
            Log.i(MYTAG,"entries su ${allEntries.entries} ")
            if(entry.value==true) {

                val checkBoxTitle = preferenceScreen.findPreference<CheckBoxPreference>(entry.key)?.title
                Log.i(MYTAG, "check box title je $checkBoxTitle")
                for (values in Markets.values()) {
                    Log.i(MYTAG, "value je $values.value")
                    if (values.value.equals(checkBoxTitle.toString(), true)) {
                        list.add(checkBoxTitle.toString())
                        Log.i(MYTAG, "checktitle $checkBoxTitle, a value je ${values.value.toString()}")
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
            Log.i(MYTAG,"entries su ${allEntries.entries} ")
            if(entry.value==true) {

                val checkBoxTitle = preferenceScreen.findPreference<CheckBoxPreference>(entry.key)?.title
                Log.i(MYTAG, "check box title je $checkBoxTitle")
                for (values in Products.values()) {
                    Log.i(MYTAG, "value je $values.value")
                    if (values.value.equals(checkBoxTitle.toString(), true)) {
                        list.add(checkBoxTitle.toString())
                        Log.i(MYTAG, "checktitle $checkBoxTitle, a value je ${values.value.toString()}")
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
            Log.i(MYTAG,"entries su ${allEntries.entries} ")
            if(entry.value==true) {

                val checkBoxTitle = preferenceScreen.findPreference<CheckBoxPreference>(entry.key)?.title
                Log.i(MYTAG, "check box title je $checkBoxTitle")
                for (values in Ssessments.values()) {
                    Log.i(MYTAG, "value je $values.value")
                    if (values.value.equals(checkBoxTitle.toString(), true)) {
                        list.add(checkBoxTitle.toString())
                        Log.i(MYTAG, "checktitle $checkBoxTitle, a value je ${values.value.toString()}")
                    }
                }
            }
        }
        return list
    }




    override fun onDestroyView() {
        Log.i(MYTAG,"on DESTROY VIEW")
        super.onDestroyView()


    }

    override fun onDestroy() {
        Log.i(MYTAG,"on DESTROY")
        super.onDestroy()
    }
}
