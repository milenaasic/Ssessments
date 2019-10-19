package com.ssessments.newsapp.settings


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat

import com.ssessments.newsapp.R


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_settings, rootKey)
        val listPreference=preferenceScreen.findPreference<ListPreference>("font_size_preference")
        listPreference?.summaryProvider=ListPreference.SimpleSummaryProvider.getInstance()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.filter_menu_item).setVisible(false)
        menu.findItem(R.id.action_search).setVisible(false)

    }
}
