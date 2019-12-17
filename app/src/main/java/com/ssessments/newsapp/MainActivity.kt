package com.ssessments.newsapp

import android.annotation.TargetApi
import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
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
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.ssessments.newsapp.activity_notification_preferences.NotificationPrefActivity
import com.ssessments.newsapp.database.NewsDatabase
import com.ssessments.newsapp.database.UserData
import com.ssessments.newsapp.databinding.ActivityMainBinding
import com.ssessments.newsapp.filter_activity.FilterActivity
import com.ssessments.newsapp.login_and_registration.LogIn_and_Registration_Activity
import com.ssessments.newsapp.myfirebase.EXTRA_NEWSID
import com.ssessments.newsapp.search_provider.MySuggestionProvider
import com.ssessments.newsapp.utilities.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*


private const val PACKAGE_NAME="com.ssessments.newsapp"
private const val CLASS_NAME="com.ssessments.newsapp.MainActivity"
private const val TAG_MAIN="MY_MAIN_ACTIVITY"


class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    private lateinit var navController: NavController
    lateinit var myappBar:AppBarLayout
    private lateinit var mySearchViewWidget:SearchView
    private lateinit var mySearchViewMenuItem:MenuItem
    private  var myUser:UserData?=null

    private var mySavedInstanceState: Bundle?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        NavigationUI.setupWithNavController(binding.toolbar,navController)

        NavigationUI.setupWithNavController(binding.toolbar, navController, binding.myDrawerLayout)

        NavigationUI.setupWithNavController(binding.myNavigationView, navController)


        binding.bottomNavigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener {menuItem->

            when(menuItem.itemId){
                R.id.sign_in_item -> { signIn()
                                        true}
                R.id.sign_up_item ->{signUp()
                                    true}

                else->false
            }

        })




        binding.myNavigationView.setNavigationItemSelectedListener {menuItem->

            when(menuItem.itemId) {
                R.id.goto_ssessments_linkedIn -> {
                    //menuItem.setChecked(true)
                    val webpage: Uri = Uri.parse(URL_SSESSMENTS_LINKEDIN_PAGE)
                    val intent = Intent(Intent.ACTION_VIEW, webpage)
                    binding.myDrawerLayout.closeDrawers()
                    menuItem.setChecked(false)
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    }
                    true
                }

                R.id.goto_ssessments_facebook -> {

                    val webpage: Uri = Uri.parse(URL_SSESSMENTS_FACEBOOK)
                    val intent = Intent(Intent.ACTION_VIEW, webpage)
                    binding.myDrawerLayout.closeDrawers()
                    menuItem.setChecked(false)
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    }
                    true
                }

                R.id.preferences->{
                    val intent=Intent(this, NotificationPrefActivity::class.java)
                    binding.myDrawerLayout.closeDrawers()
                    menuItem.setChecked(false)
                    startActivity(intent)
                    true
                }

                R.id.logout_menuitem ->{
                    menuItem.setChecked(true)
                    viewModel.clearUsernameAndPassword()
                    binding.myDrawerLayout.closeDrawers()
                    menuItem.setChecked(false)
                    menuItem.setVisible(false)
                    true}


                else -> menuItem.onNavDestinationSelected(navController)
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {

                R.id.mainFragment ->{
                    setMainFragmentUI()
                }

                R.id.detailNews -> {
                    setDetailNewsUI()

                }

                R.id.filter_menu_item ->{
                    setFiltersUI()
                }

                R.id.settings_fragment ->{
                    setSettingsFragmentUI()

                }

                R.id.accountFragment-> setAccountFragmentUI()

            }
        }


        viewModel.loggedInUser.observe(this, Observer {user->
            when{
                user==null->setloggedInUserUI(false)
                user.username.equals(EMPTY_USERNAME)-> {setloggedInUserUI(false)
                                                        myUser=user}
                else->{myUser=user
                        setloggedInUserUI(true)}
            }

        })

        viewModel.showAuthentificationFailedMessage.observe(this,Observer{showSnackbar->
            if(showSnackbar){
                Toast.makeText(this,R.string.authfailed,Toast.LENGTH_LONG).show()
                viewModel.authentificationFailedMessaheShown()
            }
        })

        viewModel.goToLogInPage.observe(this, Observer {shouldGo->
                if(shouldGo){
                    signIn()
                    viewModel.goToLogInPageFinished()
                }
         })

        viewModel.closeSearchWidget.observe(this,Observer{shouldClose->
                if(shouldClose){
                mySearchViewMenuItem.collapseActionView()
                viewModel.searchWidgetClosed()
                }
        })

        viewModel.networkErrorMainActivity.observe(this, Observer { showSnackbar->
            if(showSnackbar){
                Snackbar.make(binding.constraintlayout,R.string.network_error,Snackbar.LENGTH_LONG).show()
                viewModel.networkErrorSnackbarShown()
            }
         })

         viewModel.showProgressBarMainActivity.observe(this,Observer{
             showMainActivivtyProgressBar(it)
         })



        //activity je orvorena iz Firebase service
        val newsIdFromIntent=intent?.getStringExtra(EXTRA_NEWSID)
        newsIdFromIntent?.run {
            val bundle = Bundle()
            val i: Int = newsIdFromIntent.toInt()
            bundle.putInt("newsID", i)
            navController.popBackStack(R.id.mainFragment, false)
            navController.navigate(R.id.detailNews, bundle)
        }

        val ver=getVersionCode(packageManager, packageName)

        Log.i(TAG_MAIN,"version code je $ver")
    }



    private fun signUp() {
        val intent=Intent(this,LogIn_and_Registration_Activity::class.java).apply {
            putExtra(
                START_LOG_REGISTRATION_ACTIVITY_MESSAGE,
                SIGN_UP_MENU_ITEM
            )}
        startActivity(intent)
    }

    private fun signIn() {
        val intent=Intent(this,LogIn_and_Registration_Activity::class.java).apply {
            putExtra(
                START_LOG_REGISTRATION_ACTIVITY_MESSAGE,
                SIGN_IN_MENU_ITEM
            )}
        startActivity(intent)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {


        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.overflow_menu, menu)
        val myFilterMenuItem=menu!!.findItem(R.id.filter_menu_item)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        mySearchViewMenuItem=menu.findItem(R.id.action_search)
        mySearchViewWidget=(mySearchViewMenuItem.actionView as SearchView).apply {

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

                 myFilterMenuItem.setVisible(false)
                 viewModel.setSwipeRefreshEnabled(false)
                 return true
             }

             override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {

                 myFilterMenuItem.setVisible(true)
                 viewModel.setSwipeRefreshEnabled(true)
                 return true
             }
         })

        return true

    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId==R.id.filter_menu_item){
            val intent=Intent(this, FilterActivity::class.java)
            startActivity(intent)
            return true
        }
        else {return item!!.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)}
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (Intent.ACTION_SEARCH == intent!!.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                mySearchViewWidget.setQuery(query,false)
                doMySearch(query)
                SearchRecentSuggestions(this, MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE)
                    .saveRecentQuery(query, null)
            }
        }else{
        if(intent.hasExtra(EXTRA_NEWSID)){
            val newsIdFromIntent=intent.getStringExtra(EXTRA_NEWSID)

            val bundle=Bundle()
            val i:Int=newsIdFromIntent.toInt()

            bundle.putInt("newsID",i)
            navController.popBackStack(R.id.mainFragment,false)
            navController.navigate(R.id.detailNews,bundle)
            }

        }

    }

    private fun doMySearch(query: String) {
        //ovde saljem preko zahtev serveru

        if(!query.isNullOrBlank()){
                    viewModel.doCustomNewsSearch(query)
                    mySearchViewMenuItem.collapseActionView()}
    }


    override fun onStart() {
        super.onStart()

        binding.myNavigationView.menu.apply {
            findItem (R.id.goto_ssessments_linkedIn).setChecked(false)
            findItem (R.id.goto_ssessments_facebook).setChecked(false)
         }


    }


    override fun onResume() {
        super.onResume()
        if (mySavedInstanceState!= null){
            // ako je ova activity bil unistena od sistema moram da podesim UI za fragmente
            when (navController.currentDestination?.id){
                R.id.detailNews->{setDetailNewsUI()}
                R.id.filter_menu_item->{setFiltersUI()}

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

            when{
                viewModel.loggedInUser.value==null->setloggedInUserUI(false)
                viewModel.loggedInUser.value?.username.equals(EMPTY_USERNAME)->setloggedInUserUI(false)
                else-> {setloggedInUserUI(loggedIn = true) }
            }
            
        }

    }

    /*private fun setPreferenceFragmentUI() {

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
                appbar.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark,null))
            }else{
                appbar.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))}

            bottom_navigation.visibility = View.GONE
            myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }*/

    private fun setFiltersUI() {

        binding.apply {
            toolbar.logo_in_toolbar.visibility = View.GONE
            toolbar.title=" "
            toolbar.navigationIcon=resources.getDrawable(R.drawable.ic_close,null)
            val lp:com.google.android.material.appbar.AppBarLayout.LayoutParams=toolbar.layoutParams as AppBarLayout.LayoutParams
            lp.scrollFlags=0
            appbar.elevation =0F
            toolbar.elevation=0F
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                appbar.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark,null))
                toolbar.setTitleTextColor(resources.getColor(R.color.colorAccent,null))
            }else{
                appbar.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
                toolbar.setTitleTextColor(resources.getColor(R.color.colorAccent))
            }

            bottom_navigation.visibility = View.GONE
            myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

    private fun setSettingsFragmentUI() {
        binding.apply {
            toolbar.apply{
                logo_in_toolbar.visibility = View.GONE
                setTitleTextColor(Color.WHITE)
                navigationIcon=resources.getDrawable(R.drawable.ic_arrow_back_white,null)
            }

            appbar.elevation=(4 * resources.displayMetrics.density)

            val lp:AppBarLayout.LayoutParams=toolbar.layoutParams as AppBarLayout.LayoutParams
            lp.scrollFlags=0

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                appbar.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark,null))
            }else{
                appbar.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))}

            bottom_navigation.visibility = View.GONE
            myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }


    private fun setDetailNewsUI() {

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

    private fun setAccountFragmentUI() {
        binding.apply {
            toolbar.apply{
                logo_in_toolbar.visibility = View.GONE
                setTitleTextColor(Color.WHITE)
                navigationIcon=resources.getDrawable(R.drawable.ic_arrow_back_white,null)
            }

            appbar.elevation=(4 * resources.displayMetrics.density)
            val lp:AppBarLayout.LayoutParams=toolbar.layoutParams as AppBarLayout.LayoutParams
            lp.scrollFlags=0

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                appbar.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark,null))
            }else{
                appbar.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))}

            bottom_navigation.visibility = View.GONE
            myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

    private fun setloggedInUserUI(loggedIn:Boolean){
        //ako je user ulogovan pokazi logout u burgeru i skloni bottom bar
        when(loggedIn){
            true->{

                binding.apply{
                    if(navController.currentDestination?.id==R.id.mainFragment){
                            bottomNavigation.visibility=View.GONE}
                    myNavigationView.menu.findItem(R.id.logout_menuitem).setVisible(true)
                    myNavigationView.menu.findItem(R.id.accountFragment).setVisible(true)
                }
            }
            false->{
                binding.apply{

                    if(navController.currentDestination?.id==R.id.mainFragment){
                        bottomNavigation.visibility=View.VISIBLE
                        }
                    myNavigationView.menu.findItem(R.id.logout_menuitem).setVisible(false)
                    myNavigationView.menu.findItem(R.id.accountFragment).setVisible(false)
                }
            }
        }

    }





    fun showMainActivivtyProgressBar(shouldShow: Boolean){
        if(shouldShow){
            binding.myNestedScroolView.alpha=0.3f
            binding.mainActivityprogressBar.visibility=View.VISIBLE
        }else{
            binding.myNestedScroolView.alpha=1f
            binding.mainActivityprogressBar.visibility=View.GONE
        }
    }



}
