package com.ssessments.detail_news_fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil

import com.ssessments.R
import com.ssessments.databinding.DetailNewsFragmentBinding
import kotlinx.android.synthetic.main.detail_news_fragment.*

class DetailNews : Fragment() {



    private lateinit var viewModel: DetailNewsViewModel
    private lateinit var viewModelFactory: DetailNewsViewModelFactory

    private lateinit var binding:DetailNewsFragmentBinding
    private lateinit var args:DetailNewsArgs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args = DetailNewsArgs.fromBundle(arguments!!)
        Toast.makeText(context, "news: ${args.newsID}", Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModelFactory= DetailNewsViewModelFactory(args.newsID)
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(DetailNewsViewModel::class.java)

        binding= DataBindingUtil.inflate(inflater,R.layout.detail_news_fragment,container,false)
        var fakeData=DetailNewsFakeData()

        binding.title.setText(fakeData.title)
        binding.timeDate.setText(fakeData.time)
        binding.newsBody.setText(fakeData.body)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

}
