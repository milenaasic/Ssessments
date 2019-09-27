package com.ssessments.newsapp

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.iid.FirebaseInstanceId
import com.ssessments.newsapp.R
import com.ssessments.newsapp.database.NewsDatabase
import com.ssessments.newsapp.databinding.ActivityMainBinding
import com.ssessments.newsapp.login_and_registration.LogIn_and_Registration_Activity
import com.ssessments.newsapp.myfirebase.EXTRA_NEWSID
import com.ssessments.newsapp.search_provider.MySuggestionProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlin.math.floor

private const val PACKAGE_NAME="com.ssessments.newsapp"
private const val CLASS_NAME="com.ssessments.newsapp.MainActivity"
private const val TAG_MAIN="MY_MAIN_ACTIVITY"

const val START_LOG_REGISTRATION_ACTIVITY_MESSAGE="Sign_in_OR_Sign_up"
private const val SIGN_IN_MENU_ITEM=0
private const val SIGN_UP_MENU_ITEM=1


class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    private lateinit var navController: NavController
    lateinit var myappBar:AppBarLayout

    private var mySavedInstanceState: Bundle?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG_MAIN,componentName.toString())
        Log.d(TAG_MAIN,"on create")

        binding=DataBindingUtil.setContentView(this, R.layout.activity_main)

        val datasource= NewsDatabase.getInstance(application).newsDatabaseDao
        viewModel = ViewModelProviders.of(this,
            MainActivityViewModelFactory(datasource, application)
        ).get(MainActivityViewModel::class.java)

        navController=findNavController(R.id.mainNavHostFragment)

        if(savedInstanceState!=null){
            mySavedInstanceState=savedInstanceState
        }

       setSupportActionBar(binding.toolbar)

        binding.toolbar.title=" "
        myappBar=binding.appbar

        //val appBarConfiguration = AppBarConfiguration(navController.graph, binding.myDrawerLayout)

        NavigationUI.setupWithNavController(binding.toolbar,navController)
        //NavigationUI.setupWithNavController(binding.bottomNavigation,navController)

        //povezivanje Toolbara da bi pravilno prikazao Up odnosno drawer ikonicbu
        NavigationUI.setupWithNavController(binding.toolbar, navController, binding.myDrawerLayout)

        //povezivanje drawer-a sa NavControllerom

        NavigationUI.setupWithNavController(binding.myNavigationView, navController)


        binding.bottomNavigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener {menuItem->

            when(menuItem.itemId){
                R.id.sign_in_item -> {
                                    val intent=Intent(this,LogIn_and_Registration_Activity::class.java).apply {
                                    putExtra(
                                        START_LOG_REGISTRATION_ACTIVITY_MESSAGE,
                                        SIGN_IN_MENU_ITEM
                                    )}
                                    startActivity(intent)
                                    true}
                R.id.sign_up_item ->{val intent=Intent(this,LogIn_and_Registration_Activity::class.java).apply {
                                    putExtra(
                                        START_LOG_REGISTRATION_ACTIVITY_MESSAGE,
                                        SIGN_UP_MENU_ITEM
                                    )}
                                    startActivity(intent)
                                    true}

                else->false
            }

        })


        binding.myNavigationView.setNavigationItemSelectedListener {menuItem->

            when(menuItem.itemId){
                R.id.preference_fragment ->{
                    menuItem.setChecked(true)
                    navController.navigate(R.id.preference_fragment)
                    binding.myDrawerLayout.closeDrawers()
                    true
                }
                R.id.logout_menuitem ->{
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

                R.id.mainFragment ->{
                    Log.i(TAG_MAIN,"on destination changed listener main fragm ")
                    setMainFragmentUI()
                }

                R.id.detailNews -> {
                    Log.i(TAG_MAIN,"on destination changed listener detail fragm ")
                    setDetailNewsUI()

                }

                R.id.filter_menu_item ->{
                    Log.i(TAG_MAIN,"on destination changed listener filter fragm ")
                    setFiltersUI()
                }


                R.id.preference_fragment ->{
                    Log.i(TAG_MAIN,"on destination changed listener prefer fragm ")
                    setPreferenceFragmentUI()
                }

            }
        }


        viewModel.loggedInUser.observe(this, Observer {user->
            Log.i(TAG_MAIN,"loggedInUser koga posmatram ${user}")
            if(user==null){setloggedInUserUI(false)}
                else setloggedInUserUI(true)

        })


        //Firebase registration token
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG_MAIN, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                //val msg = getString("token je", token)
                Log.d(TAG_MAIN, "token je $token")
                Toast.makeText(this, "token je $token", Toast.LENGTH_SHORT).show()
            })

        //activity je orvorena iz Firebase service
        val newsIdFromIntent=intent?.getStringExtra(EXTRA_NEWSID)
        Log.i(TAG_MAIN,"extra news iz notifikacije $newsIdFromIntent")
        newsIdFromIntent?.run {
            val bundle = Bundle()
            val i: Int = newsIdFromIntent.toInt()
            Log.i(TAG_MAIN, "int iz extra intenta je $i")
            bundle.putInt("newsID", i)
            navController.popBackStack(R.id.mainFragment, false)
            navController.navigate(R.id.detailNews, bundle)
        }


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
        Log.i(TAG_MAIN,"on neew intent")
        if (Intent.ACTION_SEARCH == intent!!.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                doMySearch(query)
                SearchRecentSuggestions(this, MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE)
                    .saveRecentQuery(query, null)
            }
        }else{
        if(intent.hasExtra(EXTRA_NEWSID)){
            val newsIdFromIntent=intent.getStringExtra(EXTRA_NEWSID)
            Log.i(TAG_MAIN,"extra news iz notifikacije $newsIdFromIntent")
            val bundle=Bundle()
            val i:Int=newsIdFromIntent.toInt()
            Log.i(TAG_MAIN,"int iz extra intenta je $i")
            bundle.putInt("newsID",i)
            navController.popBackStack(R.id.mainFragment,false)
            navController.navigate(R.id.detailNews,bundle)
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



    }


    override fun onResume() {
        super.onResume()
        if (mySavedInstanceState!= null){
            // ako je ova activity bil unistena od sistema moram da podesim UI za fragmente
            when (navController.currentDestination?.id){
                R.id.detailNews->{setDetailNewsUI()}
                R.id.filter_menu_item->{setFiltersUI()}
                R.id.preference_fragment->{setPreferenceFragmentUI()}
            }
        }

    }

    private fun setMainFragmentUI(){
        binding.apply {
            toolbar.logo_in_toolbar.visibility = View.VISIBLE
            toolbar.title = ""
            appbar.elevation=(4 * resources.displayMetrics.density)
            //appbar.elevation = 12f
            appbar.setBackgroundColor(Color.WHITE)
            myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED)

            var pm=toolbar.layoutParams as AppBarLayout.LayoutParams
            pm.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL+AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS)

            if(viewModel.loggedInUser.value==null) bottom_navigation.visibility = View.VISIBLE
            else bottom_navigation.visibility = View.GONE

        }

    }

    private fun setPreferenceFragmentUI() {
        Log.i(TAG_MAIN,"setPreferenceFragmentUI ")
        binding.apply {

            toolbar.apply{
                logo_in_toolbar.visibility = View.GONE
                setTitleTextColor(Color.WHITE)
                navigationIcon=resources.getDrawable(R.drawable.ic_arrow_back_white,null)
                overflowIcon=resources.getDrawable(R.drawable.ic_overlow_white, null)
            }

            appbar.elevation=(4 * resources.displayMetrics.density)
            val lp:AppBarLayout.LayoutParams=toolbar.layoutParams as AppBarLayout.LayoutParams
            lp.scrollFlags=0

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                appbar.setBackgroundColor(resources.getColor(R.color.logoPurpleMatchingSecondary,null))
            }else{
                appbar.setBackgroundColor(resources.getColor(R.color.logoPurpleMatchingSecondary))}

            bottom_navigation.visibility = View.GONE
            myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

    private fun setFiltersUI() {
        Log.i(TAG_MAIN,"setFiltersUI method ")
        binding.apply {
            toolbar.logo_in_toolbar.visibility = View.GONE
            toolbar.title=" "
            toolbar.navigationIcon=resources.getDrawable(R.drawable.ic_close,null)
            val lp:com.google.android.material.appbar.AppBarLayout.LayoutParams=toolbar.layoutParams as AppBarLayout.LayoutParams
            lp.scrollFlags=0
            appbar.elevation =0F
            toolbar.elevation=0F
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                appbar.setBackgroundColor(resources.getColor(R.color.logoPurpleMatchingSecondary,null))
                toolbar.setTitleTextColor(resources.getColor(R.color.colorAccent,null))
            }else{
                appbar.setBackgroundColor(resources.getColor(R.color.logoPurpleMatchingSecondary))
                toolbar.setTitleTextColor(resources.getColor(R.color.colorAccent))
            }

            bottom_navigation.visibility = View.GONE
            myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

    private fun setDetailNewsUI() {
        Log.i(TAG_MAIN,"detail news ui method ")
        binding.apply {
            toolbar.logo_in_toolbar.visibility = View.GONE
            toolbar.title =" "
            toolbar.elevation=0F
            toolbar.setBackgroundColor(Color.TRANSPARENT)
            val lp: com.google.android.material.appbar.AppBarLayout.LayoutParams =
                toolbar.layoutParams as AppBarLayout.LayoutParams
            lp.scrollFlags = 0
            appbar.elevation = (0.5f * resources.displayMetrics.density)
            appbar.setBackgroundColor(Color.TRANSPARENT)
            bottom_navigation.visibility = View.GONE
            myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        }
    }

    private fun setloggedInUserUI(loggedIn:Boolean){
        //ako je user ulogovan pokazi logout u burgeru i skloni bottom bar
        when(loggedIn){
            true->{
                Log.i(TAG_MAIN,"main activity bottom nav nestaje")
                binding.apply{
                    if(navController.currentDestination?.id==R.id.mainFragment){
                            bottomNavigation.visibility=View.GONE}
                    myNavigationView.menu.findItem(R.id.logout_menuitem).setVisible(true)
                }
            }
            false->{
                binding.apply{
                    if(navController.currentDestination?.id==R.id.mainFragment){
                        bottomNavigation.visibility=View.VISIBLE}
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
