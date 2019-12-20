 package com.ssessments.newsapp.filter_activity


import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip

import com.ssessments.newsapp.R
import com.ssessments.newsapp.database.CurrentFilter
import com.ssessments.newsapp.database.FilterItem
import com.ssessments.newsapp.databinding.FragmentCustomFilterFragmentBinding
import com.ssessments.newsapp.utilities.*
import java.util.*
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

private const val MYTAG="CustomFilterFragment"
class CustomFilterFragment : Fragment() {

    private lateinit var binding: FragmentCustomFilterFragmentBinding
    private lateinit var navController: NavController
    private lateinit var filterActivityViewModel:FilterActivityViewModel

    private var selectedFromDate:Calendar=Calendar.getInstance()
    private var selectedToDate:Calendar=Calendar.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_custom_filter_fragment,container,false)

        filterActivityViewModel=requireActivity().run {
            ViewModelProviders.of(this)[FilterActivityViewModel::class.java]
        }

        Log.i(MYTAG, " u on createView posle povezivanja view modela")


        navController=findNavController()

        binding.marketsConsLayout.setOnClickListener{view ->
            navController.navigate(CustomFilterFragmentDirections.actionCustomFilterFragmentToMarketsFragment())
        }

        binding.productConstraintLayout.setOnClickListener{view ->
            navController.navigate(CustomFilterFragmentDirections.actionCustomFilterFragmentToProductsFragment())
        }

        binding.ssessmentsConstLayout.setOnClickListener{view ->
            navController.navigate(CustomFilterFragmentDirections.actionCustomFilterFragmentToServicetypeFragment())
        }

        // setovanje cipova za jezik
        setTagsToChips(binding.languageChipGroup,convertStringWithCommasToRealArray(enumValues<Language>().joinToString { it.value }))

        //default vrednosti datuma i teksta za date pickere
        selectedFromDate.timeInMillis=System.currentTimeMillis()
        selectedToDate.timeInMillis=System.currentTimeMillis()
        binding.activityfromDatePicker.text= DATE_SELECT_TEXT
        binding.activitytoDatePicker.text= DATE_SELECT_TEXT

        // Date Picker FROM
        binding.activityfromDatePicker.setOnClickListener {
            val c = Calendar.getInstance()
            val cyear = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                requireActivity(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in Toast
                    selectedFromDate.set(year,monthOfYear,dayOfMonth)
                    //Toast.makeText(requireActivity(), """$dayOfMonth - ${monthOfYear + 1} - $year""", Toast.LENGTH_LONG).show()
                    filterActivityViewModel.setChosenFromDate(dateFormatMySQL.format(selectedFromDate.time))
                    //binding.activityfromDatePicker.text= dateFormatNoHours.format(selectedFromDate.time)

                },
                cyear,
                month,
                day
            )

            dpd.datePicker.updateDate(selectedFromDate.get(Calendar.YEAR),selectedFromDate.get(Calendar.MONTH),selectedFromDate.get(
                Calendar.DAY_OF_MONTH))
            dpd.datePicker.maxDate=selectedToDate.timeInMillis
            dpd.show()
        }

        binding.activitytoDatePicker.setOnClickListener {

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                requireActivity(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in Toast
                    selectedToDate.set(year,monthOfYear,dayOfMonth)
                   // binding.activitytoDatePicker.text= dateFormatNoHours.format(selectedToDate.time)
                    filterActivityViewModel.setChosenToDate(dateFormatMySQL.format(selectedToDate.time))
                    //Toast.makeText(requireActivity(), """$dayOfMonth - ${monthOfYear + 1} - $year""", Toast.LENGTH_LONG).show()

                },
                year,
                month,
                day
            )
            dpd.datePicker.minDate= selectedFromDate.timeInMillis
            dpd.datePicker.updateDate(selectedToDate.get(Calendar.YEAR),selectedToDate.get(Calendar.MONTH),selectedToDate.get(
                Calendar.DAY_OF_MONTH))
            dpd.datePicker.maxDate= Calendar.getInstance().timeInMillis
            dpd.show()
        }





        //MAIN RESET BUTTON
        binding.resetfilterButton.setOnClickListener({
            resetAllFields()
        })

        //APPLY BUTTON
        binding.applyfilterButton.setOnClickListener {
                filterActivityViewModel.applyFilter(CurrentFilter(  market=binding.marketsesubtitl.text.toString(),
                                                                    product = binding.productssubtitl.text.toString(),
                                                                    ssessment = binding.ssessmenntsubtitl.text.toString(),
                                                                    language = getCheckedLanguage(),
                                                                dateFrom =if(binding.activityfromDatePicker.text==DATE_SELECT_TEXT) NO_DATE_SELECTED_VALUE else dateFormatMySQL.format(selectedFromDate.time) ,
                                                                dateTo =if(binding.activitytoDatePicker.text==DATE_SELECT_TEXT)  NO_DATE_SELECTED_VALUE else dateFormatMySQL.format(selectedToDate.time) )
                                                                    )
        }

        //SAVE BUTTON
        binding.savefilterButton.setOnClickListener {

            val alertDialog= AlertDialog.Builder(requireActivity(),R.style.MyAlertDialogTheme)

            val myinflater = this.layoutInflater
            val dialogView = myinflater.inflate(R.layout.save_filter_alert_dialog, null)

            alertDialog.setView(dialogView).setCancelable(true).create()

            alertDialog.setPositiveButton("SAVE", DialogInterface.OnClickListener{ dialog, id ->

                val nameView: EditText?=dialogView.findViewById(R.id.filter_name_edit_text)


                if(nameView==null){ }
                if(nameView!=null){

                    if(nameView.text.isNullOrEmpty()){
                        Toast.makeText(requireActivity(), "Enter filter name", Toast.LENGTH_LONG)
                            .show()}
                    else{ filterActivityViewModel.saveFilter(getFilterFieldsValue(filterName = nameView.text.toString()))}
                }
            })

            alertDialog.show()
         }


        return binding.root

    }

    private fun resetAllFields() {
        filterActivityViewModel.apply {
            setChosenMarketsTags(arrayListOf(AllMarkets.ALL_MARKETS.value))
            setChosenProductsTags(arrayListOf(Products.ALL_PRODUCTS.value))
            setChosenServicesTags(arrayListOf(Services.ALL_SERVICES.value))
            setChosenFromDate(NO_DATE_SELECTED_VALUE)
            setChosenToDate(NO_DATE_SELECTED_VALUE)
            binding.chipEnglish.isChecked=true
         }


        selectedFromDate.timeInMillis=System.currentTimeMillis()
        selectedToDate.timeInMillis=System.currentTimeMillis()


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i(MYTAG, " u on ViewCreated  ")

        filterActivityViewModel.mychosenMarketsTags.observe(this,Observer{
            Log.i(MYTAG, " u on ViewCreated chosen markets $it")
            if(it!=null)binding.marketsesubtitl.text=convertArrayListToStringWithCommas(it)
        })


        filterActivityViewModel.mychosenProductsTags.observe(this, Observer{
            Log.i(MYTAG, " u on ViewCreated chosen products $it")
            if(it!=null)binding.productssubtitl.text=convertArrayListToStringWithCommas(it)
        })

        filterActivityViewModel.mychosenServicesTags.observe(this,Observer{
            Log.i(MYTAG, " u on ViewCreated chosen services $it")
            if(it!=null) binding.ssessmenntsubtitl.text=convertArrayListToStringWithCommas(it)

        })

        filterActivityViewModel.mychosenFromDate.observe(this, Observer{dateFrom->
            Log.i(MYTAG, " u on ViewCreated from date $dateFrom")
            if(dateFrom==NO_DATE_SELECTED_VALUE) {
                selectedFromDate.timeInMillis=System.currentTimeMillis()
                binding.activityfromDatePicker.text= DATE_SELECT_TEXT
            } else {
                selectedFromDate.time= dateFormatMySQL.parse(dateFrom)
                binding.activityfromDatePicker.text= dateFormatNoHours.format(selectedFromDate.time)
            }

        })

        filterActivityViewModel.mychosenToDate.observe(this, Observer{dateTo->
            Log.i(MYTAG, " u on ViewCreated to date $dateTo")

            if(dateTo==NO_DATE_SELECTED_VALUE) {
                selectedToDate.timeInMillis=System.currentTimeMillis()
                binding.activitytoDatePicker.text= DATE_SELECT_TEXT
            } else {
                selectedToDate.time= dateFormatMySQL.parse(dateTo)
                binding.activitytoDatePicker.text= dateFormatNoHours.format(selectedToDate.time)
            }

        })


        filterActivityViewModel.mychosenLanguage.observe(this, Observer {

        if(it!=null) setLanguageChipWithTitle(it)
        })

        filterActivityViewModel.navigateToMainActivity.observe(this,Observer{
            if(it){
                Log.i(MYTAG, " u on ViewCreated navigate to main activity $it")
                filterActivityViewModel.navigationToMainActivityFinished()
                requireActivity().finish()

            }

        })

        filterActivityViewModel.myloggedInUser.observe(this, Observer{ userdata->

            if(userdata.username!= EMPTY_USERNAME) binding.savefilterButton.isEnabled=true
            else binding.savefilterButton.isEnabled=false

        })

    }

    private fun setLanguageChipWithTitle(s: String?) {
        val chipGroup = binding.languageChipGroup

        myloop@
        for (i in 0..chipGroup.childCount - 1) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (chip.tag.equals(s)) {
                chip.isChecked = true
                break@myloop
            }

        }
    }


    // ZA SAVED FILTERS TABELU
    private fun getFilterFieldsValue(filterName: String): FilterItem {

        return FilterItem(
            0L,
            filterName,
            market=binding.marketsesubtitl.text.toString(),
            product = binding.productssubtitl.text.toString(),
            ssessment = binding.ssessmenntsubtitl.text.toString(),
            language=getCheckedLanguage(),
            dateFrom =if(binding.activityfromDatePicker.text==DATE_SELECT_TEXT) NO_DATE_SELECTED_VALUE else dateFormatMySQL.format(selectedFromDate.time) ,
            dateTo =if(binding.activitytoDatePicker.text==DATE_SELECT_TEXT)  NO_DATE_SELECTED_VALUE else dateFormatMySQL.format(selectedToDate.time)
            )

    }

    private fun getCheckedLanguage():String{

        var lan=Language.ENGLISH.value

        when(binding.languageChipGroup.checkedChipId){

            R.id.chipEnglish->lan= Language.ENGLISH.value
            R.id.chipVietnamese->lan= Language.VIETNAMESE.value
            R.id.chipBahamaIndonesia->lan= Language.BAHASA_INDONESIA.value

        }

        return lan

    }
}


