package com.ssessments.newsapp.filter_fragments_saved_and_predefined

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.ssessments.newsapp.MainActivityViewModel
import com.ssessments.newsapp.databinding.FragmentSavedFiltersBinding
import com.ssessments.newsapp.R
import com.ssessments.newsapp.database.NewsDatabase
import com.ssessments.newsapp.database.UserData
import com.ssessments.newsapp.news_list_home.MainFragmentViewModel
import com.ssessments.newsapp.news_list_home.MainFragmentViewModelFactory
import com.ssessments.newsapp.utilities.EMPTY_USERNAME

private const val MY_TAG="MY_SavedFiltersFragment"
class SavedFiltersFragment : Fragment() {
    private lateinit var binding: FragmentSavedFiltersBinding
    private lateinit var viewModel: SavedFiltersViewModel
    private  var myuserData: UserData?=null
    private lateinit var adapter:SavedFiltersAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_saved_filters,container,false)

        val application= requireActivity().application
        val datasource= NewsDatabase.getInstance(application).newsDatabaseDao

        viewModel = ViewModelProviders.of(this, SavedFiltersViewModelFactory(datasource,application))
            .get(SavedFiltersViewModel::class.java)



        viewModel.loggedInUser.observe(this,Observer{user->
            myuserData=user
            if(user.username.equals(EMPTY_USERNAME)) {
                adapter.dataList= emptyList()
                viewModel.showEmptyListText(true)
            }
        })


        adapter=SavedFiltersAdapter(FilterItemClickListener { filterId ->
            //Toast.makeText(context, "${filterId}", Toast.LENGTH_LONG).show()
            viewModel.fetchSavedFilterWithId(filterId)
        }, FilterItemDeleteClickListener { item->
           // sharedViewModel.deleteFilter(item)
            val alertDialog=AlertDialog.Builder(requireActivity(),R.style.MyAlertDialogTheme)
                            .setTitle("Delete this item?")
                            .setPositiveButton("YES",DialogInterface.OnClickListener{dialog: DialogInterface?, which: Int ->
                                        viewModel.deleteFilter(item)

                        })
                        .setNegativeButton("CANCEL",DialogInterface.OnClickListener { dialog, which ->
                            dialog.dismiss()})
            alertDialog.create().show()

        })

        binding.savedFiltersRecView.adapter= adapter
        binding.savedFiltersRecView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))





        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.filters.observe(this, Observer {list->
            if(viewModel.loggedInUser.value?.username.equals(EMPTY_USERNAME)) {
                viewModel.showEmptyListText(true)
            }else {
                adapter.dataList = list
                if (list.isNullOrEmpty()) {
                    viewModel.showEmptyListText(true)
                } else {
                    viewModel.showEmptyListText(false)
                }
            }
        })

        viewModel.showEmptyList.observe(this, Observer {showList->
            if(showList) binding.savedEmptyListText.visibility=View.VISIBLE
            else binding.savedEmptyListText.visibility=View.GONE
        })

        viewModel.chosenFilter.observe(this,Observer{chosenFilter->
            viewModel.applySavedFilter(chosenFilter)
        })

        viewModel.navigateToMainFragmentFromSaved.observe(this, Observer{ shouldnavigate->
            if(shouldnavigate){
                requireActivity().finish()
                viewModel.navigationToMainFragmentFromSavedFinished()
            }
        })
    }


    private fun showSnackBar(s:String){
        Snackbar.make(binding.constLayout,s, Snackbar.LENGTH_LONG).show()
    }

}
