package com.ssessments.filter_fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.ssessments.databinding.FragmentSavedFiltersBinding
import com.ssessments.R


class SavedFiltersFragment : Fragment() {
    private lateinit var binding: FragmentSavedFiltersBinding
    private lateinit var sharedViewModel: FilterPagerSupportSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.i("SavedFiltersFR","on create")

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_saved_filters,container,false)

        sharedViewModel = parentFragment?.run {
            ViewModelProviders.of(this)[FilterPagerSupportSharedViewModel::class.java]
        } ?: throw Exception("Invalid Parent Fragment")


        val adapter=SavedFiltersAdapter(FilterItemClickListener {filterId ->
            Toast.makeText(context, "${filterId}", Toast.LENGTH_LONG).show()
            //viewModel.fetchFilterWithID(filterId)
        })

        binding.savedFiltersRecView.adapter= adapter
        binding.savedFiltersRecView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))


        return binding.root
    }


}
