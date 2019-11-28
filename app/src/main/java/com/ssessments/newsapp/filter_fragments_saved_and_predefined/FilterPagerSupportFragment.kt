package com.ssessments.newsapp.filter_fragments_saved_and_predefined

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.ssessments.newsapp.R
import com.ssessments.newsapp.database.NewsDatabase

class FilterPagerSupportFragment: Fragment() {

    private lateinit var mactivity:AppCompatActivity
    private lateinit var sharedViewModel: FilterPagerSupportSharedViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root=inflater.inflate(R.layout.filter_fragment_pager_support, container, false)

        val application= requireNotNull(this.activity).application
        val datasource=NewsDatabase.getInstance(application).newsDatabaseDao
        sharedViewModel = ViewModelProviders.of(this,FilterPagerSupportSharedViewModelFactory(datasource,application))
                        .get(FilterPagerSupportSharedViewModel::class.java)

        val myTabLayout:TabLayout?=root.findViewById(R.id.filters_tablayout)
        val myViewPager=root.findViewById<ViewPager>(R.id.my_view_pager)

        myTabLayout?.setupWithViewPager(myViewPager)
        myViewPager.adapter=MyFilterAdapter(childFragmentManager)


        setHasOptionsMenu(true)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
       mactivity=requireActivity() as AppCompatActivity
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.removeItem(R.id.filter_menu_item)
        menu.removeItem(R.id.action_search)

    }

    override fun onStart() {
        super.onStart()

    }

    override fun onStop() {
        super.onStop()

    }
}


const val TAG="MyFILTERADAPTER"
const val NUMBER_OF_ITEM_FRAGMENTS=3
class MyFilterAdapter(fm:FragmentManager):FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    override fun getItem(position: Int): Fragment {

        when(position){
            0->{ return FilterByFragment()}
            1->{ return SavedFiltersFragment()}
            2-> return PredefinedFiltersFragment()
            else->return FilterByFragment()
        }
    }


    override fun getCount(): Int {
        return NUMBER_OF_ITEM_FRAGMENTS
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0->{ return "Custom Filter"}
            1->{ return "Saved Filter"}
            2->{ return "Predefined Filter"}
            else->return ""
        }
    }
}