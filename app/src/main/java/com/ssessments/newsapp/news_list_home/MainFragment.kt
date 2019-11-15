package com.ssessments.newsapp.news_list_home


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.ssessments.newsapp.MainActivityViewModel
import com.ssessments.newsapp.R
import com.ssessments.newsapp.database.CurrentFilter
import com.ssessments.newsapp.database.NewsDatabase
import com.ssessments.newsapp.database.UserData
import com.ssessments.newsapp.databinding.FragmentMainBinding
import com.ssessments.newsapp.login_and_registration.LogIn_and_Registration_Activity
import com.ssessments.newsapp.network.NetworkNewsFilterObject
import com.ssessments.newsapp.utilities.*

private const val mytag="MY_MAIN_FRAGMENT"
class mainFragment : Fragment() {

    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var viewModel:MainFragmentViewModel
    private lateinit var binding:FragmentMainBinding

    private lateinit var adapter:NewsAdapter

    var myCurrentFilter:CurrentFilter?=null
    var myUserData:UserData?=null


    //SCALE TEXT
    private var systemFontScale=1f
    private val defaultTextSizeTitle =16f
    private val defaultTextSizeUserTypeTimeDateTags=12f
    private val FONT_SIZE_KEY="font_size_preference"
    private val DEFAULT_FONT_SIZE="1"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.i(mytag,"main fragment on createview")
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_main,container,false)

        mainActivityViewModel=requireActivity().run {
            ViewModelProviders.of(this)[MainActivityViewModel::class.java]
        }

        val application= requireActivity().application
        val datasource= NewsDatabase.getInstance(application).newsDatabaseDao
        viewModel = ViewModelProviders.of(this, MainFragmentViewModelFactory(datasource,application))
            .get(MainFragmentViewModel::class.java)

        adapter=NewsAdapter(NewsItemClickListener {  newsId ->
            Toast.makeText(context, "${newsId}", Toast.LENGTH_LONG).show()
            viewModel.fetchNewsWithID(newsId)
        })


        binding.mainRecView.adapter= adapter
        binding.mainRecView.addItemDecoration(DividerItemDecoration(activity,DividerItemDecoration.VERTICAL))
        ViewCompat.setNestedScrollingEnabled(binding.mainRecView,false)

        ViewCompat.setNestedScrollingEnabled(binding.mainRecView, true)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentFilter.observe(this,Observer{ newFilter->

            Log.i(mytag,"main fragment observe mycurrent filter , $myCurrentFilter")
            Log.i(mytag,"main fragment observe new filter , $newFilter")
            Log.i(mytag,"main fragment observe lastFilterUsed , ${viewModel.getLastFilterUsedByMainFragment()}")

        if(newFilter==null) return@Observer

            if(viewModel.getLastFilterUsedByMainFragment()!=null) {
                if( newFilter.equals(viewModel.getLastFilterUsedByMainFragment())) return@Observer
            }

            if(myCurrentFilter!=null){
                if(newFilter.equals(myCurrentFilter))return@Observer
            }

            myCurrentFilter = newFilter
            Log.i(mytag,"userdata pre pokretanja getFilteredNewsList , ${myUserData?.token?:"my USerje NULL"}")
            //Log.i(mytag,"main fragment current filter = lastfilterUSedByMain , ${viewModel.getLastFilterUsedByMainFragment().equals(newFilter)}")
            if(myUserData!=null){
            val f=convertCurrentFilterToNetworkNewsFilterObject(myUserData?.token ?: EMPTY_TOKEN, myCurrentFilter!!)
            Log.i(mytag,"filter news netowork je $f")
            viewModel.getFilteredNewsListFromServer(f,initializedFromSwipeRefresh = false)

            }

        })


        viewModel.newsList.observe(this, Observer { newList->
            Log.i(mytag,"main fragment observe news list")
            Log.i(mytag,"main fragment new list je $newList")

            when{
                newList==null-> { //adapter.dataList = EMPTY_LIST
                                //showNoResultTextView(true)
                                }
                newList.isEmpty()->{}
                newList[0].title== NO_RESULT->{ adapter.dataList = EMPTY_LIST
                                                showNoResultTextView(true)
                                                }
                else-> {adapter.dataList = newList
                        showNoResultTextView(false)
                        }
            }

            viewModel.hideProgressBar()

        })

        viewModel.myUser.observe(this, Observer {user->
            Log.i(mytag,"main fragment observe user , $user")
            myUserData=user
        })

        binding.mySwipeRefreshLayout.setOnRefreshListener {
            Log.i(mytag, "onRefresh called from SwipeRefreshLayout")

            val filterToApply=myCurrentFilter
            if(filterToApply==null){
                viewModel.getFilteredNewsListFromServer(NetworkNewsFilterObject(token=myUserData?.token?:EMPTY_TOKEN,
                    markets =arrayOf(Markets.ALL_MARKETS.value),
                    products=arrayOf(Products.ALL_PRODUCTS.value),
                    ssessments = arrayOf(Ssessments.ALL_SERVICES.value)),
                    initializedFromSwipeRefresh = true)
            }

            if(filterToApply!=null)
                viewModel.getFilteredNewsListFromServer(convertCurrentFilterToNetworkNewsFilterObject(myUserData?.token?:EMPTY_TOKEN,filterToApply),
                    initializedFromSwipeRefresh = true)

        }

        viewModel.swiperefreshfinished.observe(this,Observer{
            if(it) {
                binding.mySwipeRefreshLayout.isRefreshing=false
                viewModel.setSwiperefreshedfinishedToFalse()}
        })


        viewModel.showToastnoInternet.observe(this, Observer { showSnackbar->
            if(showSnackbar){
                Toast.makeText(activity,R.string.nointernet,Toast.LENGTH_LONG).show()
                //Snackbar.make(binding.fragmentLinLay,R.string.nointernet,Snackbar.LENGTH_LONG).show()
                viewModel.noInternetSnackBarShown()
            }
        })

        viewModel.newsToBeOpenedID.observe(this, Observer { id->
            openDetailNewsFragment(id) })

        viewModel.showProgressBar.observe(this,Observer{
            when(it){
                true-> progressBarVisible(true)
                false->progressBarVisible(false)}

        })

        viewModel.showToastNetworkError.observe(this,Observer{
            if(it){
                Toast.makeText(activity,R.string.network_error,Toast.LENGTH_LONG).show()
                //Snackbar.make(binding.fragmentLinLay,R.string.network_error,Snackbar.LENGTH_LONG).show()
                viewModel.networkErrorSnackBarShown()

            }
        })

        viewModel.showToastAuthentificationFailed.observe(this, Observer{
            if(it){
                Toast.makeText(activity,R.string.authfailed,Toast.LENGTH_LONG).show()
                viewModel.authFailedSnackBarShown()}
        })

        viewModel.goToLogInPage.observe(this, Observer{

            if(it){
                val intent= Intent(requireActivity(), LogIn_and_Registration_Activity::class.java).apply {
                    putExtra(
                        START_LOG_REGISTRATION_ACTIVITY_MESSAGE,
                        SIGN_IN_MENU_ITEM
                    )}
                startActivity(intent)
                viewModel.goToLogInFinished()

            }
        })

        mainActivityViewModel.swipeRefreshEnabled.observe(this,Observer{shouldEnable->

            binding.mySwipeRefreshLayout.setEnabled(shouldEnable)
        })






    }

    private fun showNoResultTextView(shouldShow: Boolean) {
        binding.noResultTextMainFragment.apply{
            if(shouldShow) visibility=View.VISIBLE
            else visibility=View.GONE
        }

    }

    fun openDetailNewsFragment(id:Int){
         if(id!=-1){
            val action= mainFragmentDirections.actionMainFragmentToDetailNews(id)
            findNavController().navigate(action)
            viewModel.resetNewsID()
         }
    }

    override fun onStart() {
        super.onStart()
        //pokupi sistemski seting u vezi velicine slova
        systemFontScale= resources.configuration.fontScale
        Log.i(mytag,"sistem font scale je $systemFontScale")
        val s: String? = PreferenceManager.getDefaultSharedPreferences(requireActivity())
            ?.getString(FONT_SIZE_KEY, DEFAULT_FONT_SIZE)
        if (s != null) adapter.setTextSizeParameter(s.toFloat())
    }



    fun showSnakbar(snackbarText:String){


    }

    private fun progressBarVisible(show: Boolean) {
        if(show) {
            binding.apply {
                fragmentLinLay.alpha = 0.3F
                mainProgressbar.visibility = View.VISIBLE
            }
        }

        if(!show){
            binding.apply {
                fragmentLinLay.alpha = 1F
                mainProgressbar.visibility = View.GONE
            }
        }
    }

    override fun onStop() {
        super.onStop()
        val filter=myCurrentFilter
        if (filter!=null) {viewModel.setLastFilterUsedByMainFragment(filter)}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(mytag,"on destroy view")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(mytag,"on destroy")
    }






}
