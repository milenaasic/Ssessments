package com.ssessments.newsapp.filter_fragments

import android.os.Bundle
import android.util.Log
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

    val TAG="FilterPagerSupportFragm"
    private lateinit var mactivity:AppCompatActivity
    private lateinit var sharedViewModel: FilterPagerSupportSharedViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(TAG, "on create")
        val root=inflater.inflate(R.layout.filter_fragment_pager_support, container, false)

        val application= requireNotNull(this.activity).application
        val datasource=NewsDatabase.getInstance(application).newsDatabaseDao
        sharedViewModel = ViewModelProviders.of(this,FilterPagerSupportSharedViewModelFactory(datasource,application))
                        .get(FilterPagerSupportSharedViewModel::class.java)

        val myTabLayout:TabLayout?=root.findViewById(R.id.filters_tablayout)
        val myViewPager=root.findViewById<ViewPager>(R.id.my_view_pager)

        myTabLayout?.setupWithViewPager(myViewPager)
        myViewPager.adapter=MyFilterAdapter(childFragmentManager)

        /*myTabLayout?.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(p0: TabLayout.Tab?) {
                //p0?.text="unselect"
               // p0?.icon.
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                //p0?.text="select"
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {

            }
        })*/
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
        Log.i(TAG, "getItem")
        when(position){
            0->{ Log.i(TAG, "Item 1")
                return FilterByFragment()}
            1->{Log.i(TAG, "Item 2")
                return SavedFiltersFragment()}
            2-> return PredefinedFiltersFragment()
            else->return FilterByFragment()
        }
    }


    override fun getCount(): Int {
        Log.i(TAG, "get count $NUMBER_OF_ITEM_FRAGMENTS")
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