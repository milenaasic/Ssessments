package com.ssessments.filter_fragment

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.ssessments.R
import java.lang.IllegalStateException
import androidx.appcompat.widget.AppCompatSpinner

class FilterDialogFragment: DialogFragment() {

    val TAG="Dialog Fragment"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
                val builder=AlertDialog.Builder(it)
                Log.v(TAG,"builder ${builder.toString()}")
                val inflater=requireActivity().layoutInflater
                val rootView=inflater.inflate(R.layout.fragment_filter_dialog,null)

                //setovanje adaptera za spiner regions
                val spinner1=rootView.findViewById<AppCompatSpinner>(R.id.spinner_region)
                ArrayAdapter.createFromResource(
                        activity as Context,
                        R.array.spinner_regions,
                        android.R.layout.simple_spinner_item
                        ).also { adapter ->
                        // Specify the layout to use when the list of choices appears
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        // Apply the adapter to the spinner
                        spinner1.adapter = adapter
            }

            //setovanje adaptera za spiner products
            val spinner2=rootView.findViewById<AppCompatSpinner>(R.id.spinner_products)
            ArrayAdapter.createFromResource(
                activity as Context,
                R.array.spinner_products,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner2.adapter = adapter
            }

            //setovanje adaptera za spiner ssessments
            val spinner3=rootView.findViewById<AppCompatSpinner>(R.id.spinner_ssessments)
            ArrayAdapter.createFromResource(
                activity as Context,
                R.array.spinner_ssessments,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner3.adapter = adapter
            }
                builder.setView(rootView)
                .setPositiveButton(R.string.apply_filter,DialogInterface.OnClickListener { dialog, which ->
                                // ovde odgovor na aplly dugme 
                            })
                .setNegativeButton(R.string.cancel_filter,DialogInterface.OnClickListener { dialog, which ->
                    getDialog()?.cancel() })

            builder.create()
        }?: throw IllegalStateException("Activity can not be null")

    }
}