package com.ssessments.news_list_home


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.ssessments.R
import com.ssessments.data.getNewsItemArray
import com.ssessments.databinding.FragmentMainBinding


class mainFragment : Fragment() {

    private lateinit var viewModel:NewsListHome_ViewModel
    private lateinit var binding:FragmentMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    var adapter=NewsAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_main,container,false)

        viewModel = ViewModelProviders.of(this).get(NewsListHome_ViewModel::class.java)

        //val root=inflater.inflate(R.layout.fragment_main, container, false)
        //recyclerView=root.findViewById(R.id.mainRecView)
        binding.mainRecView.adapter= adapter
        binding.mainRecView.addItemDecoration(DividerItemDecoration(activity,DividerItemDecoration.VERTICAL))

        viewModel.newsList.observe(this, Observer { newList->
            if(newList!=null) adapter.dataList = newList
               })

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        val activity:AppCompatActivity= activity as AppCompatActivity
        //activity.tagsLayout.visibility=View.VISIBLE
    }
}
