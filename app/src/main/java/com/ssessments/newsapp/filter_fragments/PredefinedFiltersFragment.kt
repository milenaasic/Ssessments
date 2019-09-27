package com.ssessments.newsapp.filter_fragments


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
import com.ssessments.newsapp.database.UserData
import com.ssessments.newsapp.databinding.FragmentPredefinedFiltersBinding
import com.ssessments.newsapp.utilities.EMPTY_TOKEN


class PredefinedFiltersFragment : Fragment() {

    private lateinit var binding: FragmentPredefinedFiltersBinding
    private lateinit var sharedViewModel: FilterPagerSupportSharedViewModel
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private  var myuserData: UserData?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_predefined_filters,container,false)

        sharedViewModel = parentFragment?.run {
            ViewModelProviders.of(this)[FilterPagerSupportSharedViewModel::class.java]
        } ?: throw Exception("Invalid Parent Fragment")

        mainActivityViewModel=requireActivity().run {
            ViewModelProviders.of(this)[MainActivityViewModel::class.java]
        }

        mainActivityViewModel.loggedInUser.observe(this, Observer{ user->
            myuserData=user
        })


        val adapter=PredefinedFiltersAdapter(PredefinedFilterItemClickListener {  filterId ->
                    Toast.makeText(context, "predefined ${filterId}", Toast.LENGTH_LONG).show()
                    //za probu uzimam filter iz saved filters tabele
                    //sharedViewModel.fetchPredefinedFilterWithId(filterId)

        })

        binding.predefinedFiltersRecView.adapter=adapter
        binding.predefinedFiltersRecView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))

    //kopirano iz saved fragmenta
        sharedViewModel.predefinedfilters.observe(requireParentFragment(), Observer {list->
            adapter.dataList=list
            if (!list.isNullOrEmpty()){
                sharedViewModel.showPredefinedEmptyListText(false)
            }
            else {sharedViewModel.showPredefinedEmptyListText(true)}
        })

        sharedViewModel.showPredefinedEmptyList.observe(requireParentFragment(), Observer {showList->
            if(showList) binding.predefinedEmptyListText.visibility=View.VISIBLE
            else binding.predefinedEmptyListText.visibility=View.GONE
        })

        sharedViewModel.chosenFilter.observe(requireParentFragment(),Observer{chosenFilter->
            if(myuserData==null)sharedViewModel.applySavedFilter(EMPTY_TOKEN,chosenFilter)
            else sharedViewModel.applySavedFilter((myuserData!!.token),chosenFilter)
        })

        sharedViewModel.navigateToMainFragmentFromSaved.observe(this, Observer{ shouldnavigate->
            if(shouldnavigate){
                findNavController().navigateUp()
                sharedViewModel.navigationToMainFragmentFromSavedFinished()
            }
        })

        sharedViewModel.networkErrorSavedFragment.observe(this, Observer { shouldShow ->
            if (shouldShow) {
                showSnackBar(resources.getString(R.string.network_error))
                sharedViewModel.networkErrorMessageShown()
            }
        })




        return binding.root
    }

    private fun showSnackBar(s:String){
        Snackbar.make(binding.constLayoutPredFilters,s, Snackbar.LENGTH_LONG).show()
    }
}
