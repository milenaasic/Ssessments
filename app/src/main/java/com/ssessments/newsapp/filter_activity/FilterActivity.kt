package com.ssessments.newsapp.filter_activity

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.tabs.TabLayout
import com.ssessments.newsapp.R
import com.ssessments.newsapp.database.NewsDatabase
import com.ssessments.newsapp.databinding.ActivityFilterBinding
import com.ssessments.newsapp.filter_fragments_saved_and_predefined.PredefinedFiltersFragmentDirections
import com.ssessments.newsapp.filter_fragments_saved_and_predefined.SavedFiltersFragmentDirections
import com.ssessments.newsapp.utilities.ICSMarkets
import com.ssessments.newsapp.utilities.Markets
import com.ssessments.newsapp.utilities.convertStringWithCommasToArray
import kotlinx.android.synthetic.main.activity_filter.*

private const val FilterActivityTag="FilterActivity"

private const val CUSTOM_FRAGMENT_LABEL="Custom"
private const val SAVED_FRAGMENT_LABEL="Saved"
private const val PREDEFINED_FRAGMENT_LABEL="Predefined"

class FilterActivity : AppCompatActivity() {

    private lateinit var binding:ActivityFilterBinding
    private lateinit var viewModel:FilterActivityViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= DataBindingUtil.setContentView(this, R.layout.activity_filter)

        setSupportActionBar(binding.filteractivitytoolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navController=findNavController(R.id.filterActivitynavHostFragment)

        //NavigationUI.setupWithNavController(binding.filteractivitytoolbar,navController)

        binding.filteractivitytoolbar.apply {
            setNavigationIcon(R.drawable.ic_close_white)
         }


        val datasource= NewsDatabase.getInstance(application).newsDatabaseDao
        viewModel = ViewModelProviders.of(this,
            FilterActivityViewModelFactory(datasource, application)
        ).get(FilterActivityViewModel::class.java)


        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            when(destination.id){

                R.id.customFilterFragment->{setUICustomFilterFragment()}
                R.id.marketsFragment->{setUIforMarkProdService()}
                R.id.productsFragment->{setUIforMarkProdService()}
                R.id.servicetypeFragment->{setUIforMarkProdService()}

            }
        }

        binding.filteractivitytablayout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(selectedTab: TabLayout.Tab?) {
               val currentDest=navController.currentDestination
               when(selectedTab?.position){
                    0->{
                        Log.i(FilterActivityTag,"pritisnu tab 0")
                        Log.i(FilterActivityTag,"current dest label ${currentDest?.label} i id ${currentDest?.id}")
                        when(currentDest?.label){
                            SAVED_FRAGMENT_LABEL->{navController.navigate(SavedFiltersFragmentDirections.actionSavedFiltersFragmentToCustomFilterFragment())}
                            PREDEFINED_FRAGMENT_LABEL->{navController.navigate(PredefinedFiltersFragmentDirections.actionPredefinedFiltersFragmentToCustomFilterFragment())}
                        }
                    }
                   1->{
                        Log.i(FilterActivityTag,"pritisnu tab 1")
                       Log.i(FilterActivityTag,"current dest label ${currentDest?.label} i id ${currentDest?.id}")
                       when(currentDest?.label){
                           CUSTOM_FRAGMENT_LABEL->{navController.navigate(CustomFilterFragmentDirections.actionCustomFilterFragmentToSavedFiltersFragment())}
                           PREDEFINED_FRAGMENT_LABEL->{navController.navigate(PredefinedFiltersFragmentDirections.actionPredefinedFiltersFragmentToSavedFiltersFragment())}
                       }


                       }
                   2->{Log.i(FilterActivityTag,"pritisnu tab 2")
                       Log.i(FilterActivityTag,"current dest label ${currentDest?.label} i id ${currentDest?.id}")
                       when(currentDest?.label){
                           CUSTOM_FRAGMENT_LABEL->{navController.navigate(CustomFilterFragmentDirections.actionCustomFilterFragmentToPredefinedFiltersFragment())}
                           SAVED_FRAGMENT_LABEL->{navController.navigate(SavedFiltersFragmentDirections.actionSavedFiltersFragmentToPredefinedFiltersFragment())}
                       }


                   }
                   else->Log.i(FilterActivityTag,"nije nista nego else")
               }
            }

        })

    }

    override fun onSupportNavigateUp(): Boolean {
        //Log.i(FilterActivityTag,"prolazi kroz onnavigate up")
        //Log.i(FilterActivityTag,"on nav Controller navigate up vraca ${navController.navigateUp()}")
        val currentDest=navController.currentDestination
        when(currentDest?.label){
            CUSTOM_FRAGMENT_LABEL, PREDEFINED_FRAGMENT_LABEL, SAVED_FRAGMENT_LABEL-> return super.onSupportNavigateUp()
            else->return navController.navigateUp()
        }

    }

    private fun setUICustomFilterFragment() {

        binding.apply {
            filteractivitytablayout.visibility= View.VISIBLE
            filteractivitytoolbar.setNavigationIcon(R.drawable.ic_close_white)
            filteractivitytoolbar.setTitle(resources.getString(R.string.filters_title))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                appbarFilterActivity.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark,null))
            }else{
                appbarFilterActivity.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))}

        }
    }

    private fun setUIforMarkProdService() {

        binding.apply {
            filteractivitytablayout.visibility= View.GONE
            filteractivitytoolbar.apply {
               title=" "
                setNavigationIcon(R.drawable.abc_ic_ab_back_material)

             }

            appbarFilterActivity.elevation = 0.0f
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                appbarFilterActivity.setBackgroundColor(resources.getColor(android.R.color.transparent,null))
            }else{
                appbarFilterActivity.setBackgroundColor(resources.getColor(android.R.color.transparent))}
         }

    }
}
