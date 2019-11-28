package com.ssessments.newsapp.activity_notification_preferences

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.ssessments.newsapp.R

class ProductsNotifPrefFragment :PreferenceFragmentCompat(){

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.products_notif_pref_fragment, rootKey)
    }

}