package com.ssessment


import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import kotlinx.android.synthetic.main.activity_main.*


class MySettingsFragment : PreferenceFragmentCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val activity: AppCompatActivity = activity as AppCompatActivity
        //activity.tagsLayout.visibility=View.GONE
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_notifications, rootKey)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val menuItem2:MenuItem=menu.findItem(R.id.action_search)
        menuItem2.setVisible(false)

    }
}
