package com.ssessment

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.ssessment.search.MySuggestionProvider

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    lateinit var tags: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myToolbar=findViewById<Toolbar>(R.id.toolbar)
        val myDrawerLayout: DrawerLayout =findViewById<DrawerLayout>(R.id.myDrawerLayout)
        val myNavigationView: NavigationView =findViewById<NavigationView>(R.id.myNavigationView)

        setSupportActionBar(myToolbar)

        tags=findViewById<ConstraintLayout>(R.id.tagsLayout)

        navController=findNavController(R.id.mainNavHostFragment)
        NavigationUI.setupWithNavController(myToolbar,navController)

        //povezivanje Toolbara da bi pravilno prikazao Up odnosno drawer ikonicu
        NavigationUI.setupWithNavController(myToolbar, navController, myDrawerLayout)

        //povezivanje drawer-a sa NavControllerom
        NavigationUI.setupWithNavController(myNavigationView, navController)

        Log.v("milena ",componentName.toString())
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.settings, menu)
        Log.v("milena ","on create")
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        var searchView = (menu!!.findItem(R.id.action_search).actionView as SearchView).apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(
                searchManager.getSearchableInfo(
                    ComponentName(
                        "com.ssessment",
                        "com.ssessment.MainActivity")
                ))


        }
        //searchView.scrollBarStyle=resources.getResourceEntryName(R.style.AppTheme)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        navController.navigate(R.id.mySettingsFragment)
        tags.visibility= View.GONE
        return true
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.i("Searcheble activity","on neew intent")
        if (Intent.ACTION_SEARCH == intent!!.action) {
            intent!!.getStringExtra(SearchManager.QUERY)?.also { query ->
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

