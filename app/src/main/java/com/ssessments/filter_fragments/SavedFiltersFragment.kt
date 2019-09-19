package com.ssessments.filter_fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.ssessments.MainActivityViewModel
import com.ssessments.databinding.FragmentSavedFiltersBinding
import com.ssessments.R
import com.ssessments.database.UserData
import com.ssessments.utilities.EMPTY_TOKEN
import com.ssessments.utilities.convertCurrentFilterToNetworkNewsFilterObject
import com.ssessments.utilities.convertFilterItemFromDatabaseToNetworkNewsFilterObject


class SavedFiltersFragment : Fragment() {
    private lateinit var binding: FragmentSavedFiltersBinding
    private lateinit var sharedViewModel: FilterPagerSupportSharedViewModel
    private lateinit var mainActivityViewModel:MainActivityViewModel
    private  var myuserData: UserData?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.i("SavedFiltersFR","on create")

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_saved_filters,container,false)

        sharedViewModel = parentFragment?.run {
            ViewModelProviders.of(this)[FilterPagerSupportSharedViewModel::class.java]
        } ?: throw Exception("Invalid Parent Fragment")

        mainActivityViewModel=requireActivity().run {
            ViewModelProviders.of(this)[MainActivityViewModel::class.java]
        }

        mainActivityViewModel.loggedInUser.observe(this,Observer{user->
            myuserData=user
        })


        val adapter=SavedFiltersAdapter(FilterItemClickListener { filterId ->
            Toast.makeText(context, "${filterId}", Toast.LENGTH_LONG).show()
            sharedViewModel.fetchSavedFilterWithId(filterId)
        }, FilterItemDeleteClickListener { item->
           // sharedViewModel.deleteFilter(item)
            val alertDialog=AlertDialog.Builder(requireActivity(),R.style.MyAlertDialogTheme)
                            .setTitle("Delete this item?")
                            .setPositiveButton("YES",DialogInterface.OnClickListener{dialog: DialogInterface?, which: Int ->
                                        sharedViewModel.deleteFilter(item)
                                        Toast.makeText(context, "delete $item}", Toast.LENGTH_LONG).show()
                        })
                        .setNegativeButton("CANCEL",DialogInterface.OnClickListener { dialog, which ->
                            dialog.dismiss()})
            alertDialog.create().show()

        })

        binding.savedFiltersRecView.adapter= adapter
        binding.savedFiltersRecView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))

        sharedViewModel.filters.observe(requireParentFragment(), Observer {list->
            adapter.dataList=list
            if (list.isNullOrEmpty()){
                sharedViewModel.showEmptyListText(true)
            }else{sharedViewModel.showEmptyListText(false)}
        })

        sharedViewModel.showEmptyList.observe(requireParentFragment(), Observer {showList->
            if(showList) binding.savedEmptyListText.visibility=View.VISIBLE
            else binding.savedEmptyListText.visibility=View.GONE
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
        Snackbar.make(binding.constLayout,s, Snackbar.LENGTH_LONG).show()
    }

}
