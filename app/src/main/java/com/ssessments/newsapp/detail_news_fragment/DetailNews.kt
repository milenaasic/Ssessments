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


private const val MYTAG="MY_DETAIL_FRAGMENT"
class DetailNews() : Fragment() {



    private lateinit var viewModel: DetailNewsViewModel
    private lateinit var viewModelFactory: DetailNewsViewModelFactory

    private lateinit var binding:DetailNewsFragmentBinding
    private lateinit var args:DetailNewsArgs
    private lateinit var mydetailNews:NetworkSingleNewsItem

    private lateinit var newsUrl:String


    //SCALE TEXT
    private var systemFontScale=1f
    private val defaultTextSizeTitle =24f
    private val defaultTextSizeTimeDateAuthorTags=14f
    private val defaultTextSizeBody=16f
    private val FONT_SIZE_KEY="FONT_SIZE"

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


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i(MYTAG,"u on activity created detailnews fragment")
        if(savedInstanceState!=null){
        Log.i(MYTAG,"u on activity created detailnews fragment bundle nije null")


            /*requireActivity().apply {
                bottom_navigation.visibility=View.GONE
                appbar.apply {
                    elevation = 2f
                    setBackgroundColor(Color.BLUE)
                }
                toolbar.apply {
                    toolbar.logo_in_toolbar.visibility = View.GONE
                    toolbar.title = ""
                 }

                val lp:com.google.android.material.appbar.AppBarLayout.LayoutParams=toolbar.layoutParams as AppBarLayout.LayoutParams
                lp.scrollFlags=0
                myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }*/
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
            R.id.font_size_item->{  openChooseFontSizeDialog()

                                    return true}



            else-> return false
        }

    }

    override fun onStart() {
        super.onStart()
        //pokupi sistemski seting u vezi velicine slova
        systemFontScale= resources.configuration.fontScale
        Log.i(MYTAG,"sistem font scale je $systemFontScale")
        changeFontSizeAllViews(PreferenceManager.getDefaultSharedPreferences(requireActivity()).getFloat(FONT_SIZE_KEY, FONT_SIZE_MEDIUM))
        Log.i(MYTAG," u preferences je sacuvano (on start call) ${PreferenceManager.getDefaultSharedPreferences(requireActivity()).getFloat(FONT_SIZE_KEY, FONT_SIZE_MEDIUM)}")
    }

    private fun openChooseFontSizeDialog(){

        val alertDialog= AlertDialog.Builder(requireActivity(), R.style.MyAlertDialogTheme)
        val dialogView = this.layoutInflater.inflate(R.layout.font_size_chooser_layout, null)
        val group=dialogView.findViewById<RadioGroup>(R.id.fontsizeRadioGroup)

        val sharedPref=PreferenceManager.getDefaultSharedPreferences(requireActivity())
        when(sharedPref.getFloat(FONT_SIZE_KEY, FONT_SIZE_MEDIUM)){
                    FONT_SIZE_SMALL->group.check(R.id.radioButtonSmall)
                    FONT_SIZE_MEDIUM->group.check(R.id.radioButtonMedium)
                    FONT_SIZE_LARGE->group.check(R.id.radioButtonLarge)
                    FONT_SIZE_EXTRA_LARGE->group.check(R.id.radioButtonExtra)

        }

        group.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    Log.i(MYTAG,"oncheched change ${checkedId}")

                    var currentValue= FONT_SIZE_MEDIUM
                    when(checkedId){
                        R.id.radioButtonSmall-> {changeFontSizeAllViews(FONT_SIZE_SMALL)
                                                currentValue= FONT_SIZE_SMALL }
                        R.id.radioButtonMedium-> {changeFontSizeAllViews(FONT_SIZE_MEDIUM)
                                                currentValue= FONT_SIZE_MEDIUM }
                        R.id.radioButtonLarge-> {changeFontSizeAllViews(FONT_SIZE_LARGE)
                                                currentValue= FONT_SIZE_LARGE}
                        R.id.radioButtonExtra-> {changeFontSizeAllViews(FONT_SIZE_EXTRA_LARGE)
                                                currentValue= FONT_SIZE_EXTRA_LARGE}
                    }

                    sharedPref.edit().putFloat(FONT_SIZE_KEY,currentValue).apply()

        })

        alertDialog.setView(dialogView).setPositiveButton("OK",DialogInterface.OnClickListener { dialog, which -> dialog.cancel() }).setCancelable(true).create().show()

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
