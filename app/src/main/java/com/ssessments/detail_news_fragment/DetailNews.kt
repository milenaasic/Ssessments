package com.ssessments.detail_news_fragment

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import com.ssessments.R
import com.ssessments.database.NewsDatabase
import com.ssessments.databinding.DetailNewsFragmentBinding
import com.ssessments.network.NetworkSingleNewsItem
import com.ssessments.utilities.EMPTY_TOKEN
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.detail_news_fragment.*

private const val MYTAG="MY_DETAIL_FRAGMENT"
class DetailNews : Fragment() {



    private lateinit var viewModel: DetailNewsViewModel
    private lateinit var viewModelFactory: DetailNewsViewModelFactory

    private lateinit var binding:DetailNewsFragmentBinding
    private lateinit var args:DetailNewsArgs
    private lateinit var mydetailNews:NetworkSingleNewsItem

    private lateinit var newsUrl:String


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
        val application= requireNotNull(this.activity).application
        val datasource= NewsDatabase.getInstance(application).newsDatabaseDao
        viewModelFactory= DetailNewsViewModelFactory(args.newsID,datasource,application)
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(DetailNewsViewModel::class.java)

        binding= DataBindingUtil.inflate(inflater,R.layout.detail_news_fragment,container,false)


        viewModel.user.observe(this, Observer{
            Log.i(MYTAG,"user je ${it}")
            val token:String=it?.token?:EMPTY_TOKEN
            viewModel.getDetailNews(token)
        })

        viewModel.singleNewsData.observe(this,Observer{
            mydetailNews=it
            binding.apply {
                title.setText(it.title)
                timeDate.setText(it.dateTime)
                newsBody.setText(it.body)
                author_textView.setText(it.author)
                tags_textView.setText(it.tags)
                newsUrl=it.newsurl

            }
        })

        viewModel.showProgressBar.observe(this,Observer{showBar->
            showProgressBar(showBar)
        })

        viewModel.shareNews.observe(this,Observer{
            if(it){
                    val sendIntent= Intent().apply{
                         action=Intent.ACTION_SEND
                         putExtra(Intent.EXTRA_TEXT,"news url")
                         putExtra(Intent.EXTRA_TITLE,"NASLOV")
                         type="text/plain"
                     }
                     val shareIntent=Intent.createChooser(sendIntent,null)
                     startActivity(shareIntent)

                    viewModel.shareActionComplete()
            }
        })

        return binding.root
    }

    private fun showProgressBar(shouldShow:Boolean){
    when(shouldShow){
        true->binding.apply{
                            detailnewsConstraintLayout.visibility=View.INVISIBLE
                            detailnewsprogressBar.visibility=View.VISIBLE
                            }
        false->binding.apply{
                            detailnewsConstraintLayout.visibility=View.VISIBLE
                            detailnewsprogressBar.visibility=View.GONE
        }
    }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_fragment_menu,menu)
        menu.removeItem(R.id.filter_menu_item)
        menu.removeItem(R.id.action_search)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
                R.id.share_item->{
                                    viewModel.shareItemMenuClicked()
                                    Log.v("Detail","share pressed")
                                    return true}
                else-> return false
        }

    }




}
