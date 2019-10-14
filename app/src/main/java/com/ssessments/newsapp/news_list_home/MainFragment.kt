package com.ssessments.newsapp.news_list_home


import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.ssessments.newsapp.R
import com.ssessments.newsapp.database.NewsDatabase
import com.ssessments.newsapp.databinding.FragmentMainBinding
import com.ssessments.newsapp.utilities.INITIALIZED_FROM_SWIPE_REFRESH

private const val mytag="MY_MAIN_FRAGMENT"
class mainFragment : Fragment() {

    private lateinit var viewModel:MainFragmentViewModel
    private lateinit var binding:FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.i(mytag,"main fragment on createview")
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_main,container,false)

        val application= requireActivity().application
        val datasource= NewsDatabase.getInstance(application).newsDatabaseDao
        viewModel = ViewModelProviders.of(this, MainFragmentViewModelFactory(datasource,application))
            .get(MainFragmentViewModel::class.java)

        val adapter=NewsAdapter(NewsItemClickListener {  newsId ->
            Toast.makeText(context, "${newsId}", Toast.LENGTH_LONG).show()
            viewModel.fetchNewsWithID(newsId)
        })


        binding.mainRecView.adapter= adapter
        binding.mainRecView.addItemDecoration(DividerItemDecoration(activity,DividerItemDecoration.VERTICAL))
        ViewCompat.setNestedScrollingEnabled(binding.mainRecView,false)

        ViewCompat.setNestedScrollingEnabled(binding.mainRecView, true)

        binding.mySwipeRefreshLayout.setOnRefreshListener {
            Log.i(mytag, "onRefresh called from SwipeRefreshLayout")
            viewModel.initializeNewsList(INITIALIZED_FROM_SWIPE_REFRESH)
        }

        viewModel.swiperefreshfinished.observe(this,Observer{
            if(it) {
                binding.mySwipeRefreshLayout.isRefreshing=false
                viewModel.setSwiperefreshedfinishedToFalse()}
        })


        viewModel.newsList.observe(this, Observer { newList->
            Log.i(mytag,"main fragment observe news list")
                    if(newList.isEmpty())showNoResultTextView(true)
                    else showNoResultTextView(false)
                    adapter.dataList = newList
        })

        viewModel.showToastnoInternet.observe(this, Observer { showSnackbar->
                if(showSnackbar){
                Snackbar.make(binding.fragmentLinLay,R.string.nointernet,Snackbar.LENGTH_LONG).show()
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

        return binding.root
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

    override fun onResume() {
        super.onResume()
        //val activity:AppCompatActivity= activity as AppCompatActivity

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

}
