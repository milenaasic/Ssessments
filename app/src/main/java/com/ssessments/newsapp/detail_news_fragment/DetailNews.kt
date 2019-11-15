package com.ssessments.newsapp.detail_news_fragment

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout

import com.ssessments.newsapp.R
import com.ssessments.newsapp.database.NewsDatabase
import com.ssessments.newsapp.databinding.DetailNewsFragmentBinding
import com.ssessments.newsapp.network.NetworkSingleNewsItem
import com.ssessments.newsapp.utilities.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.detail_news_fragment.*

import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.TextUtils


private const val MYTAG="MY_DETAIL_FRAGMENT"
class DetailNews() : Fragment() {



    private lateinit var viewModel: DetailNewsViewModel
    private lateinit var viewModelFactory: DetailNewsViewModelFactory

    private lateinit var binding:DetailNewsFragmentBinding
    private lateinit var args:DetailNewsArgs
    private lateinit var mydetailNews:NetworkSingleNewsItem


    //SCALE TEXT
    private var systemFontScale=1f
    private val defaultTextSizeTitle =24f
    private val defaultTextSizeTimeDateAuthorTags=14f
    private val defaultTextSizeBody=16f
    private val FONT_SIZE_KEY="font_size_preference"
    private val DEFAULT_FONT_SIZE="1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         args = DetailNewsArgs.fromBundle(arguments!!)
        setHasOptionsMenu(true)
        Toast.makeText(context, "news: ${args.newsID}", Toast.LENGTH_LONG).show()
        Log.i(MYTAG,"news: ${args.newsID}")
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


        viewModel.singleNewsData.observe(this,Observer{
            mydetailNews=it
            Log.i(MYTAG,"nadgledani detail je $it")
            binding.apply {


                timeDate.setText(dateStringFormatISO8601oReadableWithHours(it.dateTime))
                author_textView.setText(it.author)
                tags_textView.setText(TextUtils.join(",",it.tags))

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    newsBody.setText(Html.fromHtml(it.body, Html.FROM_HTML_MODE_COMPACT))
                    title.setText(Html.fromHtml(it.title, Html.FROM_HTML_MODE_COMPACT))
                } else {
                    newsBody.setText(Html.fromHtml(it.body))
                    title.setText(Html.fromHtml(it.title))
                }
            }
        })

        viewModel.showProgressBar.observe(this,Observer{showBar->
            showProgressBar(showBar)
        })

        viewModel.shareNews.observe(this,Observer{
            if(it){
                   val sendIntent= Intent().apply{
                         action=Intent.ACTION_SEND
                         putExtra(Intent.EXTRA_TEXT,mydetailNews.newsurl?: URL_SSESSMENTS_HOME)
                         putExtra(Intent.EXTRA_TITLE,mydetailNews.title)
                         type="text/plain"
                     }
                     val shareIntent=Intent.createChooser(sendIntent,null)
                     startActivity(shareIntent)

                viewModel.shareActionComplete()
            }
        })

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i(MYTAG,"u on activity created detailnews fragment")
        if(savedInstanceState!=null){
        Log.i(MYTAG,"u on activity created detailnews fragment bundle nije null")

        }
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

    override fun onStart() {
        super.onStart()
        //pokupi sistemski seting u vezi velicine slova
        systemFontScale= resources.configuration.fontScale
        Log.i(MYTAG,"sistem font scale je $systemFontScale")
        val s: String? = PreferenceManager.getDefaultSharedPreferences(requireActivity())
            ?.getString(FONT_SIZE_KEY, DEFAULT_FONT_SIZE)
        if (s != null) changeFontSizeAllViews( s.toFloat())

        //Log.i(MYTAG," u preferences je sacuvano (on start call) ${PreferenceManager.getDefaultSharedPreferences(requireActivity()).getFloat(FONT_SIZE_KEY, FONT_SIZE_MEDIUM)}")
    }


    private fun changeFontSizeAllViews(fontSize: Float) {
        changeFontSize(binding.title,defaultTextSizeTitle,fontSize)
        changeFontSize(binding.timeDate,defaultTextSizeTimeDateAuthorTags,fontSize)
        changeFontSize(binding.authorTextView,defaultTextSizeTimeDateAuthorTags,fontSize)
        changeFontSize(binding.tagsTextView,defaultTextSizeTimeDateAuthorTags,fontSize)
        changeFontSize(binding.newsBody,defaultTextSizeBody,fontSize)
    }


    private fun changeFontSize(textView:TextView,defaultFontSize:Float,chosenFontSize:Float) {
        textView.textSize=defaultFontSize.times(systemFontScale).times(chosenFontSize)

    }


}
