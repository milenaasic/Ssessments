package com.ssessments.filter_fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.ssessments.R
import java.util.logging.Filter
import kotlin.reflect.jvm.internal.impl.serialization.deserialization.FlexibleTypeDeserializer

class FilterPagerSupportFragment: Fragment() {

    val TAG="FilterPagerSupportFragm"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(TAG, "on create")
        val root=inflater.inflate(R.layout.filter_fragment_pager_support, container, false)

        val myTabLayout:TabLayout=root.findViewById(R.id.filters_tablayout)
       val myViewPager=root.findViewById<ViewPager>(R.id.my_view_pager)

        //myTabLayout.getTabAt(1)?.apply { select() }
        myTabLayout.setupWithViewPager(myViewPager)
        myViewPager.adapter=MyFilterAdapter(childFragmentManager)



        return root
    }



}


const val TAG="MyFILTERADAPTER"
const val NUMBER_OF_ITEM_FRAGMENTS=2
class MyFilterAdapter(fm:FragmentManager):FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    override fun getItem(position: Int): Fragment {
        Log.i(TAG, "getItem")
        when(position){
            0->{ Log.i(TAG, "Item 1")
                return FilterByFragment()}
            1->{Log.i(TAG, "Item 2")
                return SavedFiltersFragment()}
            else->return FilterByFragment()
        }
    }


    override fun getCount(): Int {
        Log.i(TAG, "get count $NUMBER_OF_ITEM_FRAGMENTS")
        return NUMBER_OF_ITEM_FRAGMENTS
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0->{
                return "Filter By"}
            1->{Log.i(TAG, "Item 2")
                return "My Filters"}
            else->return ""
        }
    }
}