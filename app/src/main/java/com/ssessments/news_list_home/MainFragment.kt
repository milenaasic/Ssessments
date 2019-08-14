package com.ssessments.news_list_home


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ssessments.R
import com.ssessments.data.getNewsItemArray
import com.ssessments.database.NewsDatabase
import com.ssessments.databinding.FragmentMainBinding


class mainFragment : Fragment() {

    private lateinit var viewModel:NewsListHome_ViewModel
    private lateinit var binding:FragmentMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_main,container,false)

        /*val application= requireNotNull(this.activity).application
        val database=NewsDatabase.getInstance(application)
        //val dataSource=NewsDatabase.getInstance(application).newsDatabaseDao

        //viewModelFactory= NewsListHome_ViewModelFactory(repository)*/

        viewModel = ViewModelProviders.of(this).get(NewsListHome_ViewModel::class.java)

        val adapter=NewsAdapter(NewsItemClickListener {  newsId ->
            Toast.makeText(context, "${newsId}", Toast.LENGTH_LONG).show()
            viewModel.fetchNewsWithID(newsId)
        })


        binding.mainRecView.adapter= adapter
        binding.mainRecView.addItemDecoration(DividerItemDecoration(activity,DividerItemDecoration.VERTICAL))

        viewModel.newsList.observe(this, Observer { newList->
            if(newList!=null) adapter.dataList = newList
               })

        viewModel.showNoAccessRightsSnackbar.observe(this, Observer { showSnackbar->
                if(showSnackbar){
                Snackbar.make(binding.fragmentLinLay,R.string.notifications,Snackbar.LENGTH_LONG).show()
                }
                viewModel.noAccessRightsSnackbarShown()
            })


        viewModel.newsToBeOpenedID.observe(this, Observer { id->
            openDetailNewsFragment(id) })


        return binding.root
    }


    fun openDetailNewsFragment(id:Int){
         if(id!=-1){
            val action= mainFragmentDirections.actionMainFragmentToDetailNews(id)
            findNavController().navigate(action)
            viewModel.resetNewsID()}


    }
    override fun onResume() {
        super.onResume()
        val activity:AppCompatActivity= activity as AppCompatActivity

    }

    fun showSnakbar(snackbarText:String){


    }
}
