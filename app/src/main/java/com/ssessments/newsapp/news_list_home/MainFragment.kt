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



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_main,container,false)

        mainActivityViewModel=requireActivity().run {
            ViewModelProviders.of(this)[MainActivityViewModel::class.java]
        }

        val application= requireActivity().application
        val datasource= NewsDatabase.getInstance(application).newsDatabaseDao
        viewModel = ViewModelProviders.of(this, MainFragmentViewModelFactory(datasource,application))
            .get(MainFragmentViewModel::class.java)

        adapter=NewsAdapter(NewsItemClickListener { newsId ->
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

        if(newFilter==null) return@Observer

            if(viewModel.getLastFilterUsedByMainFragment()!=null) {
                if( newFilter.equals(viewModel.getLastFilterUsedByMainFragment())) return@Observer
            }

            if(myCurrentFilter!=null){
                if(newFilter.equals(myCurrentFilter))return@Observer
            }

            myCurrentFilter = newFilter
            Log.i(mytag,"current filter je $myCurrentFilter")

            if(myUserData!=null){
            val f=convertCurrentFilterToNetworkNewsFilterObject(myUserData?.token ?: EMPTY_TOKEN, myCurrentFilter!!)

            viewModel.getFilteredNewsListFromServer(f,initializedFromSwipeRefresh = false)

            }

        })


        viewModel.newsList.observe(this, Observer { newList->


            when{
                newList==null-> {  }
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

            myUserData=user
            Log.i(mytag,"posmatranje usera u bazi iz main fragmenta $user")
        })

        binding.mySwipeRefreshLayout.setOnRefreshListener {


            val filterToApply=myCurrentFilter
            if(filterToApply==null){
                viewModel.getFilteredNewsListFromServer(NetworkNewsFilterObject(token=myUserData?.token?:EMPTY_TOKEN,
                    markets =arrayOf(AllMarkets.ALL_MARKETS.value),
                    products=arrayOf(AllProducts.ALL_PRODUCTS.value),
                    ssessments = arrayOf(Services.ALL_SERVICES.value),
                    language=Language.ENGLISH.value),
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

                viewModel.noInternetSnackBarShown()
            }
        })

        viewModel.newsToBeOpenedID.observe(this, Observer { id->
            Log.i(mytag,"news id iz observe news id u mainfragment view modelu je $id")
            openDetailNewsFragment(id) })

        viewModel.showProgressBar.observe(this,Observer{
            when(it){
                true-> progressBarVisible(true)
                false->progressBarVisible(false)}

        })

        viewModel.showToastNetworkError.observe(this,Observer{
            if(it){
                Toast.makeText(activity,R.string.network_error,Toast.LENGTH_LONG).show()

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
         Log.i(mytag,"news id je $id")
            val action= mainFragmentDirections.actionMainFragmentToDetailNews(id)
            findNavController().navigate(action)
            viewModel.resetNewsID()
         }
    }

    override fun onStart() {
        super.onStart()

        systemFontScale= resources.configuration.fontScale

        val s: String? = PreferenceManager.getDefaultSharedPreferences(requireActivity())
            ?.getString(FONT_SIZE_KEY, DEFAULT_FONT_SIZE)
        if (s != null) adapter.setTextSizeParameter(s.toFloat())
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








}
