package com.ssessments.news_list_home


import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.ssessments.R
import com.ssessments.database.NewsDatabase
import com.ssessments.databinding.FragmentMainBinding
import com.ssessments.filter_fragments.FilterPagerSupportSharedViewModel
import com.ssessments.filter_fragments.FilterPagerSupportSharedViewModelFactory

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

        /*val application= requireNotNull(this.activity).application
        val database=NewsDatabase.getInstance(application)
        //val dataSource=NewsDatabase.getInstance(application).newsDatabaseDao*/

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

        binding.mySwipeRefreshLayout.setOnRefreshListener {
            Log.i(mytag, "onRefresh called from SwipeRefreshLayout")
            viewModel.initializeNewsList()
        }

        viewModel.swiperefreshfinished.observe(this,Observer{

            if(it) {
                binding.mySwipeRefreshLayout.isRefreshing=false
                viewModel.setSwiperefreshedfinishedToFalse()}

        })




        viewModel.newsList.observe(this, Observer { newList->
            Log.i(mytag,"main fragment observe news list")
            if(newList!=null) adapter.dataList = newList
               })

        viewModel.noInternet.observe(this, Observer { showSnackbar->
                if(showSnackbar){
                Snackbar.make(binding.fragmentLinLay,R.string.nointernet,Snackbar.LENGTH_LONG).show()
                viewModel.noInternetSnackBarShown()
                }
            })


        viewModel.newsToBeOpenedID.observe(this, Observer { id->
            Log.i(mytag,"main fragment news to be opened")
            openDetailNewsFragment(id) })


        return binding.root
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



}
