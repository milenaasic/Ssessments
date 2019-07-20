package com.ssessments.detail_news_fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.databinding.DataBindingUtil

import com.ssessments.R
import com.ssessments.databinding.DetailNewsFragmentBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.detail_news_fragment.*

class DetailNews : Fragment() {



    private lateinit var viewModel: DetailNewsViewModel
    private lateinit var viewModelFactory: DetailNewsViewModelFactory

    private lateinit var binding:DetailNewsFragmentBinding
    private lateinit var args:DetailNewsArgs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args = DetailNewsArgs.fromBundle(arguments!!)
        setHasOptionsMenu(true)
        Toast.makeText(context, "news: ${args.newsID}", Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModelFactory= DetailNewsViewModelFactory(args.newsID)
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(DetailNewsViewModel::class.java)

        binding= DataBindingUtil.inflate(inflater,R.layout.detail_news_fragment,container,false)
        var myfakeData=MyDetailNewsFakeData()

        binding.apply {
            title.setText(myfakeData.title)
            timeDate.setText(myfakeData.time)
            newsBody.setText(myfakeData.body)
        }

        requireActivity().toolbar.apply {
        logo_in_toolbar.visibility=View.GONE
        title=""
        //setBackgroundColor(resources.getColor(R.color.logoPurpleMatchingSecondary))
        }

        requireActivity().apply {
        bottom_navigation.visibility=View.GONE
        }

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_fragment_menu,menu)
        menu.removeItem(R.id.filter_menu_item)
        menu.removeItem(R.id.action_search)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
                R.id.share_item->{Log.v("Detail","share pressed")
                                    return true}
                else-> return false
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onDestroyView() {
        requireActivity().toolbar.apply {
            logo_in_toolbar.visibility = View.VISIBLE

            requireActivity().apply {
                bottom_navigation.visibility=View.VISIBLE
            }

            super.onDestroyView()
        }
    }
}
