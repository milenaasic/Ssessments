package com.ssessments.newsapp.activity_notification_preferences

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.ssessments.newsapp.R

private const val MY_TAG="ProductsNotifPrefFragm"
class ProductsNotifPrefFragment :PreferenceFragmentCompat(){

    private lateinit var activityViewModel: NotifPrefActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityViewModel=requireActivity().run {
            ViewModelProviders.of(this)[NotifPrefActivityViewModel::class.java]
        }

    }
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.products_notif_pref_fragment, rootKey)


        findPreference<Preference>("productsPlastics")?.setOnPreferenceClickListener {

            findNavController().navigate(ProductsNotifPrefFragmentDirections.actionProductsNotifPrefFragmentToProductsPlastics2())
            true
        }

        findPreference<Preference>("productsChemicals")?.setOnPreferenceClickListener {

            findNavController().navigate(ProductsNotifPrefFragmentDirections.actionProductsNotifPrefFragmentToProductsChemicals2())
            true
        }

        findPreference<Preference>("productsEnergy")?.setOnPreferenceClickListener {

            findNavController().navigate(ProductsNotifPrefFragmentDirections.actionProductsNotifPrefFragmentToProductsEnergy2())
            true
        }


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activityViewModel.plasticsSummary.observe(this, Observer {
            Log.i(MY_TAG, "plastics lista za sumeri je $it")
            findPreference<Preference>("productsPlastics")?.summary=it.joinToString(separator = ",").trim()
        })

        activityViewModel.chemicalsSummary.observe(this, Observer {
            Log.i(MY_TAG, "chemicals lista za sumeri je $it")
            findPreference<Preference>("productsChemicals")?.summary=it.joinToString(separator = ",").trim()

        })

        activityViewModel.energySummary.observe(this, Observer {
            Log.i(MY_TAG, "energy lista za sumeri je $it")
            findPreference<Preference>("productsEnergy")?.summary=it.joinToString(separator = ",").trim()

        })

    }



}