package com.ssessments.filter_fragments

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.ssessments.R
import com.ssessments.database.FilterItem
import kotlinx.android.synthetic.main.filter_by_fragment.*
import java.util.*
import java.text.DateFormat
import java.text.DateFormat.MEDIUM


class FilterByFragment : Fragment() {

    private lateinit var binding: com.ssessments.databinding.FilterByFragmentBinding
    private lateinit var sharedviewModel: FilterPagerSupportSharedViewModel

    private var selectedFromDate:Calendar=Calendar.getInstance()
    private var selectedToDate:Calendar=Calendar.getInstance()
    var dateFormat: DateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("FilterbyFragm","filterby create")

        binding= DataBindingUtil.inflate(inflater, com.ssessments.R.layout.filter_by_fragment,container,false)

        sharedviewModel = parentFragment?.run {
            ViewModelProviders.of(this)[FilterPagerSupportSharedViewModel::class.java]
        } ?: throw Exception("Invalid Parent Fragment")

        //binding.fromDatePicker.text=dateFormat.format(selectedFromDate.time)
        //binding.toDatePicker.text=dateFormat.format(selectedToDate.time)


        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.spinner_regions,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.spinnerRegion.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.spinner_products,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerProduct.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.spinner_ssessments,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerSsessments.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.spinner_language,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerLanguage.adapter = adapter
              }

        // Date Picker FROM
        binding.fromDatePicker.setOnClickListener {

            val c = Calendar.getInstance()
            val cyear = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                requireActivity(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in Toast
                    selectedFromDate.set(year,monthOfYear,dayOfMonth)
                    Toast.makeText(requireActivity(), """$dayOfMonth - ${monthOfYear + 1} - $year""", Toast.LENGTH_LONG)
                        .show()
                    binding.fromDatePicker.text=dateFormat.format(selectedFromDate.time)
                },
                cyear,
                month,
                day
            )
            dpd.datePicker.updateDate(selectedFromDate.get(Calendar.YEAR),selectedFromDate.get(Calendar.MONTH),selectedFromDate.get(Calendar.DAY_OF_MONTH))
            dpd.datePicker.maxDate=selectedToDate.timeInMillis
            dpd.show()
        }

        binding.toDatePicker.setOnClickListener {

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                requireActivity(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in Toast
                    selectedToDate.set(year,monthOfYear,dayOfMonth)
                    binding.toDatePicker.text=dateFormat.format(selectedToDate.time)
                    Toast.makeText(requireActivity(), """$dayOfMonth - ${monthOfYear + 1} - $year""", Toast.LENGTH_LONG)
                        .show()

                },
                year,
                month,
                day
            )
             dpd.datePicker.minDate= selectedFromDate.timeInMillis
            dpd.datePicker.updateDate(selectedToDate.get(Calendar.YEAR),selectedToDate.get(Calendar.MONTH),selectedToDate.get(Calendar.DAY_OF_MONTH))
            dpd.datePicker.maxDate=Calendar.getInstance().timeInMillis
            dpd.show()
        }

        binding.resetButton.setOnClickListener({
            resetAllFields()
        })

        binding.saveFilterButton.setOnClickListener {
            val alertDialog=AlertDialog.Builder(requireActivity(),R.style.MyAlertDialogTheme)
            alertDialog.setView(R.layout.save_filter_alert_dialog)
                .setPositiveButton("SAVE",DialogInterface.OnClickListener{dialog, id ->
                    val name=requireActivity().findViewById<EditText>(R.id.filter_name_edit_text).text.toString()
                    sharedviewModel.saveFilter(getCurrentFieldsValue(filterName = name))
                })
                .setCancelable(true)
                .create()


            alertDialog.show()

        }

        binding.applyFilterButton.setOnClickListener {
            sharedviewModel.applyFilterButton(getCurrentFieldsValue())
        }

        return binding.root
    }



    fun resetAllFields(){
        binding.apply {
            spinnerProduct.setSelection(0)
            spinnerRegion.setSelection(0)
            spinnerSsessments.setSelection(0)
            //from_date_picker.text=currentDateFrom
            //to_date_picker.text= currentDateTo
            spinnerLanguage.setSelection(0)


        }
    }

    fun getCurrentFieldsValue(filterName:String="default"):FilterItem{

        return FilterItem(
            0L,
            filterName,
            binding.spinnerRegion.selectedItem.toString(),
            binding.spinnerProduct.selectedItem.toString(),
            binding.spinnerSsessments.selectedItem.toString(),
            binding.fromDatePicker.text.toString(),
            binding.toDatePicker.text.toString(),
            binding.spinnerLanguage.selectedItem.toString()
        )
    }

}
