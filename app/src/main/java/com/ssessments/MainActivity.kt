package com.ssessments

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssessments.database.NewsDatabase
import com.ssessments.databinding.ActivityMainBinding
import com.ssessments.filter_fragments.FilterPagerSupportSharedViewModel
import com.ssessments.filter_fragments.FilterPagerSupportSharedViewModelFactory
import com.ssessments.login_and_registration.LOGGED_IN_USER_GOTO_MAIN_ACTIVITY
import com.ssessments.login_and_registration.LogIn_and_Registration_Activity
import com.ssessments.search_provider.MySuggestionProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

private const val PACKAGE_NAME="com.ssessments"
private const val CLASS_NAME="com.ssessments.MainActivity"
private const val TAG_MAIN="MY_MAIN_ACTIVITY"

const val START_LOG_REGISTRATION_ACTIVITY_MESSAGE="Sign_in_OR_Sign_up"
private const val SIGN_IN_MENU_ITEM=0
private const val SIGN_UP_MENU_ITEM=1


class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    private lateinit var navController: NavController
    lateinit var myappBar:AppBarLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG_MAIN,componentName.toString())
        Log.v(TAG_MAIN,"on create")

        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)

        val datasource= NewsDatabase.getInstance(application).newsDatabaseDao
        viewModel = ViewModelProviders.of(this, MainActivityViewModelFactory(datasource,application))
            .get(MainActivityViewModel::class.java)


       setSupportActionBar(binding.toolbar)
        binding.toolbar.title=""
        myappBar=binding.appbar

        navController=findNavController(R.id.mainNavHostFragment)

        //val appBarConfiguration = AppBarConfiguration(navController.graph, binding.myDrawerLayout)

        NavigationUI.setupWithNavController(binding.toolbar,navController)
        //NavigationUI.setupWithNavController(binding.bottomNavigation,navController)

        //povezivanje Toolbara da bi pravilno prikazao Up odnosno drawer ikonicbu
        NavigationUI.setupWithNavController(binding.toolbar, navController, binding.myDrawerLayout)

        //povezivanje drawer-a sa NavControllerom

        NavigationUI.setupWithNavController(binding.myNavigationView, navController)


        binding.bottomNavigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener {menuItem->

            when(menuItem.itemId){
                R.id.sign_in_item-> {
                                    val intent=Intent(this,LogIn_and_Registration_Activity::class.java).apply {
                                    putExtra(START_LOG_REGISTRATION_ACTIVITY_MESSAGE, SIGN_IN_MENU_ITEM)}
                                    startActivity(intent)
                                    true}
                R.id.sign_up_item->{val intent=Intent(this,LogIn_and_Registration_Activity::class.java).apply {
                                    putExtra(START_LOG_REGISTRATION_ACTIVITY_MESSAGE, SIGN_UP_MENU_ITEM)}
                                    startActivity(intent)
                                    true}

                else->false
            }

        })


        binding.myNavigationView.setNavigationItemSelectedListener {menuItem->

            when(menuItem.itemId){
                R.id.home->{
                    menuItem.setChecked(true)
                    navController.navigate(R.id.mainFragment)
                    binding.myDrawerLayout.closeDrawers()
                    true
                }
                R.id.preference_fragment->{
                    menuItem.setChecked(true)
                    navController.navigate(R.id.preference_fragment)
                    binding.myDrawerLayout.closeDrawers()
                    true
                }
                R.id.logout_menuitem->{
                    menuItem.setChecked(true)
                    viewModel.clearUser()
                    binding.myDrawerLayout.closeDrawers()
                    menuItem.setVisible(false)
                    true}

                else->false
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {

                R.id.mainFragment->{
                    binding.apply {
                        toolbar.logo_in_toolbar.visibility = View.VISIBLE
                        toolbar.title = ""
                        appbar.elevation = 12f
                        appbar.setBackgroundColor(Color.WHITE)
                        myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED)

                        //proveri da li je user ulogovan
                        if(viewModel.loggedInUser.value==null) bottom_navigation.visibility = View.VISIBLE
                        else bottom_navigation.visibility = View.GONE

                    }

                }

                R.id.detailNews -> {
                    binding.apply {
                        toolbar.logo_in_toolbar.visibility = View.GONE
                        toolbar.title = ""
                        appbar.elevation = 2f
                        appbar.setBackgroundColor(Color.TRANSPARENT)
                        bottom_navigation.visibility = View.GONE
                        myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    }
                }

                R.id.filter_menu_item->{
                    binding.apply {
                        toolbar.logo_in_toolbar.visibility = View.GONE
                        toolbar.title = ""
                        appbar.elevation = 0f
                        appbar.setBackgroundColor(Color.TRANSPARENT)
                        bottom_navigation.visibility = View.GONE
                        myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    }
                }


                R.id.preference_fragment->{
                    binding.apply {
                        toolbar.logo_in_toolbar.visibility = View.GONE
                        appbar.elevation = 2f
                        appbar.setBackgroundColor(Color.TRANSPARENT)
                        bottom_navigation.visibility = View.GONE
                        myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    }
                }
            }
        }


        viewModel.loggedInUser.observe(this, Observer {user->
            Log.i(TAG_MAIN,"loggedInUser koga posmatram ${user}")
            if(user==null){setloggedInUserUI(false)}
                else setloggedInUserUI(true)

        })



    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.v(TAG_MAIN,"on create options")

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
                 Log.i(TAG_MAIN,"u on action expand")
                 myFilterMenuItem.setVisible(false)
                 return true
             }

             override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                 Log.i(TAG_MAIN,"u on action expand")
                 myFilterMenuItem.setVisible(true)
                 return true
             }
         })

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


    override fun onStart() {
        super.onStart()
        Log.i(TAG_MAIN,"main activity on start")

        //ako je otvorena activitu pomocu intenta iz login-a
        /*if(intent?.extras?.getBoolean(LOGGED_IN_USER_GOTO_MAIN_ACTIVITY)==true){
            Log.i(TAG_MAIN,"main activity on start iz intent extra")
            viewModel.getUserFromDatabase()
        }*/
    }

    private fun setloggedInUserUI(loggedIn:Boolean){
        //ako je user ulogovan pokazi logout u burgeru i skloni bottom bar
        when(loggedIn){
            true->{
                Log.i(TAG_MAIN,"main activity bottom nav nestaje")
                binding.apply{
                    bottomNavigation.visibility=View.GONE
                    myNavigationView.menu.findItem(R.id.logout_menuitem).setVisible(true)
                }
            }
            false->{
                binding.apply{
                    bottomNavigation.visibility=View.VISIBLE
                    myNavigationView.menu.findItem(R.id.logout_menuitem).setVisible(false)
                }
            }
        }

    }
    override fun onStop() {
        super.onStop()
        Log.i(TAG_MAIN,"main activity on stop")
    }


}
