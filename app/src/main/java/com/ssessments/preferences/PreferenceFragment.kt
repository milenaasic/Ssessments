package com.ssessments.preferences


import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.ssessments.R


class PreferenceFragment : PreferenceFragmentCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val activity: AppCompatActivity = activity as AppCompatActivity

    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_notifications, rootKey)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val menuItem1:MenuItem=menu.findItem(R.id.action_search)
        menuItem1.setVisible(false)

    }
}