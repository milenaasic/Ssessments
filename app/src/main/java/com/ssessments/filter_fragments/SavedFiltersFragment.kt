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


        val adapter=SavedFiltersAdapter(FilterItemClickListener { filterId ->
            Toast.makeText(context, "${filterId}", Toast.LENGTH_LONG).show()
            //viewModel.fetchFilterWithID(filterId)
        }, FilterItemDeleteClickListener { item->
            sharedViewModel.deleteFilter(item)


            /*val alertDialog=AlertDialog.Builder(requireActivity(),R.style.MyAlertDialogTheme)

            alertDialog.setTitle("Delete this item?")
                        .setPositiveButton("YES",DialogInterface.OnClickListener{dialog: DialogInterface?, which: Int ->
                            sharedViewModel.deleteFilter(item)
                            Toast.makeText(context, "delete $item}", Toast.LENGTH_LONG).show()
                        })
                        .setNegativeButton("NO",DialogInterface.OnClickListener { dialog, which ->
                            dialog.dismiss()})

            alertDialog.create().show()*/

        })

        binding.savedFiltersRecView.adapter= adapter
        binding.savedFiltersRecView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))

        sharedViewModel.filters.observe(requireParentFragment(), Observer {list->
            adapter.dataList=list
            if (!list.isNullOrEmpty()){
            //Toast.makeText(context,list.size,Toast.LENGTH_LONG).show()
                sharedViewModel.showEmptyListText(false)
            }
            else {sharedViewModel.showEmptyListText(true)}
        })

        sharedViewModel.showEmptyList.observe(requireParentFragment(), Observer {showList->
            if(showList) binding.emptyListText.visibility=View.VISIBLE
            else binding.emptyListText.visibility=View.GONE
         })

        return binding.root
    }


}
