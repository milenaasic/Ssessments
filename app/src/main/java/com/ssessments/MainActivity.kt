package com.ssessments

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import com.ssessments.search_provider.MySuggestionProvider

private const val PACKAGE_NAME="com.ssessments"
private const val CLASS_NAME="com.ssessments.MainActivity"
private const val TAG="MY_MAIN_ACTIVITY"

class MainActivity : AppCompatActivity(){


    private lateinit var binding: com.ssessments.databinding.ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG,componentName.toString())

        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.title=""

        navController=findNavController(R.id.mainNavHostFragment)

        NavigationUI.setupWithNavController(binding.toolbar,navController)
        NavigationUI.setupWithNavController(binding.bottomNavigation,navController)

        //povezivanje Toolbara da bi pravilno prikazao Up odnosno drawer ikonicu
        NavigationUI.setupWithNavController(binding.toolbar, navController, binding.myDrawerLayout)

        //povezivanje drawer-a sa NavControllerom
        NavigationUI.setupWithNavController(binding.myNavigationView, navController)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.v(TAG,"on create")

        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.overflow_menu, menu)
        val myFilterMenuItem=menu!!.findItem(R.id.filter_menu_item)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView=(menu.findItem(R.id.action_search).actionView as SearchView).apply {

            // Assumes current activity is the searchable activity
            setSearchableInfo(
                searchManager.getSearchableInfo(
                    ComponentName(
                        PACKAGE_NAME,
                        CLASS_NAME
                        )
                ))
        }



         menu.findItem(R.id.action_search).setOnActionExpandListener(object:MenuItem.OnActionExpandListener{
             override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                 Log.i(TAG,"u on action expand")
                 myFilterMenuItem.setVisible(false)
                 return true
             }

             override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                 Log.i(TAG,"u on action expand")
                 myFilterMenuItem.setVisible(true)
                 return true
             }
         })

        //searchView.scrollBarStyle=resources.getResourceEntryName(R.style.AppTheme)

        return true

    }



    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return item!!.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)

    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.i("Searcheble activity","on neew intent")
        if (Intent.ACTION_SEARCH == intent!!.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                doMySearch(query)
                SearchRecentSuggestions(this, MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE)
                    .saveRecentQuery(query, null)
            }
        }
    }

    private fun doMySearch(query: String) {
        //ovde saljem preko zahtev serveru
        Log.i("Searcheble activity","pokrenut doMySearch")
        //prikazi progres dialog i informaciju dok ne implementiraim search
    }




}

