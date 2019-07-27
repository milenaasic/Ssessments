package com.ssessments.filter_fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ssessments.databinding.FragmentSavedFiltersBinding
import com.ssessments.R


class SavedFiltersFragment : Fragment() {
    private lateinit var binding: FragmentSavedFiltersBinding
    private lateinit var viewModel: SavedFiltersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.i("SavedFiltersFR","on create")

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_saved_filters,container,false)
        viewModel = ViewModelProviders.of(this, ViewModelProvider.AndroidViewModelFactory(activity?.application!!)).get(SavedFiltersViewModel::class.java)



        return binding.root
    }


}
