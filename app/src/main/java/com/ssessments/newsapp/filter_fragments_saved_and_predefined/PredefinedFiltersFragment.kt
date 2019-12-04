package com.ssessments.newsapp.filter_fragments_saved_and_predefined


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.ssessments.newsapp.MainActivityViewModel

import com.ssessments.newsapp.R
import com.ssessments.newsapp.database.NewsDatabase
import com.ssessments.newsapp.database.UserData
import com.ssessments.newsapp.databinding.FragmentPredefinedFiltersBinding


class PredefinedFiltersFragment : Fragment() {

    private lateinit var binding: FragmentPredefinedFiltersBinding
    private lateinit var viewModel: PredefinedFiltersViewModel
    private lateinit var aviewModel: PredefinedFiltersViewModel
    private  var myuserData: UserData?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_predefined_filters,container,false)

        val application= requireActivity().application
        val datasource= NewsDatabase.getInstance(application).newsDatabaseDao

        viewModel = ViewModelProviders.of(this, PredefinedFiltersViewModelFactory(datasource,application))
            .get(PredefinedFiltersViewModel::class.java)



       /* viewModel.loggedInUser.observe(this, Observer{ user->
            myuserData=user
        })*/


        val adapter=PredefinedFiltersAdapter(PredefinedFilterItemClickListener {  filterId ->
                    Toast.makeText(context, "predefined ${filterId}", Toast.LENGTH_LONG).show()
                    //za probu uzimam filter iz saved filters tabele
                    //sharedViewModel.fetchPredefinedFilterWithId(filterId)

        })

        binding.predefinedFiltersRecView.adapter=adapter
        binding.predefinedFiltersRecView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))


        viewModel.predefinedfilters.observe(this, Observer {list->
            adapter.dataList=list
            if (!list.isNullOrEmpty()){
                viewModel.showPredefinedEmptyListText(false)
            }
            else {viewModel.showPredefinedEmptyListText(true)}
        })

        viewModel.showPredefinedEmptyList.observe(this, Observer {showList->
            if(showList) binding.predefinedEmptyListText.visibility=View.VISIBLE
            else binding.predefinedEmptyListText.visibility=View.GONE
        })

        viewModel.chosenPredefinedFilter.observe(this,Observer{chosenFilter->
            /*if(myuserData==null)sharedViewModel.applySavedFilter(EMPTY_TOKEN,chosenFilter)
            else sharedViewModel.applySavedFilter((myuserData!!.token),chosenFilter)*/
        })

        viewModel.navigateToMainFragmentFromPredefined.observe(this, Observer{ shouldnavigate->
            if(shouldnavigate){
                findNavController().navigateUp()
                viewModel.navigationToMainFragmentFromPredefinedFinished()
            }
        })

        /*sharedViewModel.networkErrorSavedFragment.observe(this, Observer { shouldShow ->
            if (shouldShow) {
                showSnackBar(resources.getString(R.string.network_error))
                sharedViewModel.networkErrorMessageShown()
            }
        })*/




        return binding.root
    }

    private fun showSnackBar(s:String){
        Snackbar.make(binding.constLayoutPredFilters,s, Snackbar.LENGTH_LONG).show()
    }
}
