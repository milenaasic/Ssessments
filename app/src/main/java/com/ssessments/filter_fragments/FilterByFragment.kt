package com.ssessments.filter_fragments

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.ssessments.R
import com.ssessments.database.FilterItem
import com.ssessments.utilities.*
import kotlinx.android.synthetic.main.filter_by_fragment.*
import java.util.*
import java.text.DateFormat
import kotlin.collections.ArrayList
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ssessments.MainActivityViewModel
import com.ssessments.database.CurrentFilter
import com.ssessments.database.UserData

private const val DEFAULT_FILTER_NAME="My Filter"
private const val DEFAULT_DATE_TEXT="Select"

class FilterByFragment : Fragment() {

    private lateinit var binding: com.ssessments.databinding.FilterByFragmentBinding
    private lateinit var sharedviewModel: FilterPagerSupportSharedViewModel
    private lateinit var mainActivityViewModel:MainActivityViewModel
    private  var myuserData: UserData?=null

    private var selectedFromDate:Calendar=Calendar.getInstance()
    private var selectedToDate:Calendar=Calendar.getInstance()
    var dateFormat: DateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("FilterbyFragm","filterby create")

        binding= DataBindingUtil.inflate(inflater, R.layout.filter_by_fragment,container,false)

        sharedviewModel = parentFragment?.run {
            ViewModelProviders.of(this)[FilterPagerSupportSharedViewModel::class.java]
        } ?: throw Exception("Invalid Parent Fragment")

        mainActivityViewModel=requireActivity().run {
            ViewModelProviders.of(this)[MainActivityViewModel::class.java]
        }

        mainActivityViewModel.loggedInUser.observe(this,Observer{ user->
            myuserData=user
            if(user==null){
                binding.saveButton.isEnabled=false
            } else {
                binding.saveButton.isEnabled=true
            }
        })



        // ako bi default text bio danasnji datum
       /* binding.fromDatePicker.text=dateFormat.format(selectedFromDate.time)
        binding.toDatePicker.text=dateFormat.format(selectedToDate.time)*/

        //default vrednosti teksta za date pickere
        binding.fromDatePicker.text= DEFAULT_DATE_TEXT
        binding.toDatePicker.text= DEFAULT_DATE_TEXT

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

        //TAGOVI za čipove za pretragu, Tagovi su jednaki tekstu čipa. Tekst Čipa je u string res, ali tagovi su enum
        // i prema njima ide posle provera da li tag treba da bude ukljucen

        binding.apply{
            //market chips
            chip30.tag=Markets.ALL_MARKETS.value
            chip31.tag=Markets.SEA.value
            chip32.tag=Markets.CHINA.value
            chip33.tag=Markets.INDONESIA.value
            chip34.tag=Markets.MALAYSIA.value
            chip35.tag=Markets.VIETNAM.value
            chip36.tag=Markets.INDIA.value

            //product chips
            chip0.tag=Products.ALL_PRODUCTS.value
            chip1.tag=Products.PE.value
            chip2.tag=Products.PP.value
            chip3.tag=Products.PVC.value
            chip4.tag=Products.PET.value
            chip5.tag=Products.STYRENICS.value

            //ssessment chips
            chip10.tag=Ssessments.ALL_SERVICES.value
            chip11.tag=Ssessments.DAILY.value
            chip12.tag=Ssessments.WEEKLY.value
            chip13.tag=Ssessments.MONTHLY.value
            chip14.tag=Ssessments.QUARTERLY.value
            chip15.tag=Ssessments.NEWS.value
            chip16.tag=Ssessments.PRICE.value
            chip17.tag=Ssessments.STATS.value
            chip18.tag=Ssessments.PLANT.value

        }

        //RESET button ne radi update news liste niti menja current filter u bazi, to se dogadja samo na APPLY button
        //RESET samo resetuje polja
        binding.resetButton.setOnClickListener({
            resetAllFields()
        })

        binding.saveButton.setOnClickListener {

            val alertDialog= AlertDialog.Builder(requireActivity(),android.R.style.Theme_Material_Light_Dialog_Alert)

            val myinflater = this.layoutInflater
            val dialogView = myinflater.inflate(com.ssessments.R.layout.save_filter_alert_dialog, null)

            alertDialog.setView(dialogView).setCancelable(true).create()

            alertDialog.setPositiveButton("SAVE", DialogInterface.OnClickListener{ dialog, id ->

                        val nameView: EditText?=dialogView.findViewById(R.id.filter_name_edit_text)
                        Log.i("FilterByFragment","pre provere save Filter name nije null")

                        if(nameView==null){ Log.i("FilterByFragment","name view je null")}
                        if(nameView!=null){
                                Log.i("FilterByFragment","save Filter name nije null")
                                if(nameView.text.isNullOrEmpty()){
                                Toast.makeText(requireActivity(), "Enter filter name", Toast.LENGTH_LONG)
                                .show()}
                        else{ sharedviewModel.saveFilter(getFilterFieldsValue(filterName = nameView.text.toString()))}
                        }
            })

            alertDialog.show()

        }

        binding.applyButton.setOnClickListener {
            if(myuserData==null)sharedviewModel.applyFilter(EMPTY_TOKEN,getCurrentFilterValues())
            else sharedviewModel.applyFilter(myuserData!!.token,getCurrentFilterValues())
        }

        //All chip ako je chekiran ostalo treba da se resetuje
        binding.apply {
            chip30.setOnCheckedChangeListener { buttonView, isChecked -> if (isChecked) resetChipGroup(binding.marketsChips) }
            chip0.setOnCheckedChangeListener { buttonView, isChecked -> if (isChecked) resetChipGroup(binding.productChips) }
            chip10.setOnCheckedChangeListener { buttonView, isChecked -> if (isChecked) resetChipGroup(binding.ssessmentChips) }
        }

        // ako je bilo koji cip pritisnut all chip se iskljuci
        binding.apply {
            chip31.setOnCheckedChangeListener { buttonView, isChecked -> if (isChecked) binding.chip30.isChecked = false }
            chip32.setOnCheckedChangeListener { buttonView, isChecked -> if (isChecked) binding.chip30.isChecked = false }
            chip33.setOnCheckedChangeListener { buttonView, isChecked -> if (isChecked) binding.chip30.isChecked = false }
            chip34.setOnCheckedChangeListener { buttonView, isChecked -> if (isChecked) binding.chip30.isChecked = false }
            chip35.setOnCheckedChangeListener { buttonView, isChecked -> if (isChecked) binding.chip30.isChecked = false }
            chip36.setOnCheckedChangeListener { buttonView, isChecked -> if (isChecked) binding.chip30.isChecked = false }
        }

        binding.apply {
            chip1.setOnCheckedChangeListener { buttonView, isChecked -> if (isChecked) binding.chip0.isChecked = false }
            chip2.setOnCheckedChangeListener { buttonView, isChecked -> if (isChecked) binding.chip0.isChecked = false }
            chip3.setOnCheckedChangeListener { buttonView, isChecked -> if (isChecked) binding.chip0.isChecked = false }
            chip4.setOnCheckedChangeListener { buttonView, isChecked -> if (isChecked) binding.chip0.isChecked = false }
            chip5.setOnCheckedChangeListener { buttonView, isChecked -> if (isChecked) binding.chip0.isChecked = false }
        }

        binding.apply {
            chip11.setOnCheckedChangeListener { buttonView, isChecked -> if (isChecked) binding.chip10.isChecked = false }
            chip12.setOnCheckedChangeListener { buttonView, isChecked -> if (isChecked) binding.chip10.isChecked = false }
            chip13.setOnCheckedChangeListener { buttonView, isChecked -> if (isChecked) binding.chip10.isChecked = false }
            chip14.setOnCheckedChangeListener { buttonView, isChecked -> if (isChecked) binding.chip10.isChecked = false }
            chip15.setOnCheckedChangeListener { buttonView, isChecked -> if (isChecked) binding.chip10.isChecked = false }
            chip16.setOnCheckedChangeListener { buttonView, isChecked -> if (isChecked) binding.chip10.isChecked = false }
            chip17.setOnCheckedChangeListener { buttonView, isChecked -> if (isChecked) binding.chip10.isChecked = false }
            chip18.setOnCheckedChangeListener { buttonView, isChecked -> if (isChecked) binding.chip10.isChecked = false }
        }


        //SHARED VIEW MODEL OBSERVERS
        sharedviewModel.currentFilter.observe(this,Observer {currentFilter->
           if(currentFilter!=null){
                setChipsInChipGroupToList(binding.marketsChips,convertStringWithCommasToArray(currentFilter.market))
                setChipsInChipGroupToList(binding.productChips, convertStringWithCommasToArray(currentFilter.product))
                setChipsInChipGroupToList(binding.ssessmentChips, convertStringWithCommasToArray(currentFilter.ssessment))
                binding.fromDatePicker.setText(currentFilter.dateFrom)
                binding.toDatePicker.setText(currentFilter.dateTo)
            }
        })

        sharedviewModel.navigateToMainFragment.observe(this, Observer{ shouldnavigate->
            if(shouldnavigate){
                findNavController().navigateUp()
                sharedviewModel.navigationToMainFragmentFinished()
            }
        })


        sharedviewModel.showProgressBar.observe(this, Observer { shouldShow ->
            if (shouldShow) {
                binding.progressbarlayout.visibility = View.VISIBLE
                binding.filterbyrootConstraintLayout.alpha = 0.2f
            } else {
                binding.progressbarlayout.visibility = View.GONE
                binding.filterbyrootConstraintLayout.alpha = 1f

            }
        })

        sharedviewModel.networkError.observe(this, Observer { shouldShow ->
            if (shouldShow) {
                showSnackBar(resources.getString(R.string.network_error))
                sharedviewModel.networkErrorMessageShown()
            }
         })


        return binding.root
    }

    // ZA SAVED FILTERS TABELU
    private fun getFilterFieldsValue(filterName: String): FilterItem {
        return FilterItem(
            0L,
            filterName,
            convertArrayListToStringWithCommas(getCheckedChips(binding.marketsChips)),
            convertArrayListToStringWithCommas(getCheckedChips(binding.productChips)),
            convertArrayListToStringWithCommas(getCheckedChips(binding.ssessmentChips)),
            getCheckedLanguage(),
            binding.fromDatePicker.text.toString(),
            binding.toDatePicker.text.toString()

        )
    }


    private fun setChipsInChipGroupToList(chipgroup: ChipGroup, list: ArrayList<String>) {
        for (item in list){
            chipgroup.apply {
                for (index in 0..this.childCount) {
                    if(getChildAt(index)!=null) {
                        val chip = getChildAt(index) as Chip
                        if (chip.tag.equals(item)) {chip.isChecked=true
                        }else chip.isChecked=false
                    }

                }

            }
        }
    }


    fun resetAllFields(){
        binding.apply {
            resetChipGroup(marketsChips)
            resetChipGroup(productChips)
            resetChipGroup(ssessmentChips)
            fromDatePicker.setText(DEFAULT_DATE_TEXT)
            toDatePicker.setText(DEFAULT_DATE_TEXT)
        }
    }

    private fun resetChipGroup(chips: ChipGroup) {
        chips.clearCheck()
        val allChip=chips.getChildAt(0) as Chip
        allChip.setChecked(true)
    }

    //ide u tabelu current_filter
    private fun getCurrentFilterValues():CurrentFilter{

        return CurrentFilter(
            convertArrayListToStringWithCommas(getCheckedChips(binding.marketsChips)),
            convertArrayListToStringWithCommas(getCheckedChips(binding.productChips)),
            convertArrayListToStringWithCommas(getCheckedChips(binding.ssessmentChips)),
            getCheckedLanguage(),
            binding.fromDatePicker.text.toString(),
            binding.toDatePicker.text.toString()

        )
    }


    private fun getCheckedChips(chipGroup:ChipGroup): ArrayList<String> {
        val list=ArrayList<String>()
        chipGroup.apply {
            for (index in 0..this.childCount) {
                if(getChildAt(index)!=null) {
                    val chip = getChildAt(index) as Chip
                    if (chip.isChecked) list.add(chip.text.toString())
                }

            }

        }
        return list

    }

    private fun getCheckedLanguage(): String {
        return Language.ENGLISH.value
    }

    //PROVERA DA LI JE BILO KOJI ČIP U GRUPI ČEKURAN (OSIM ALL ČIPA)
    private fun isAnyChipInGroupChecked(chipGroup: ChipGroup):Boolean {
        Toast.makeText(requireActivity(),"is any chip metod",Toast.LENGTH_SHORT).show()
        var checked = false
        chipGroup.apply {
            for (index in 1..this.childCount) {
                if (getChildAt(index) != null) {
                    val chip = getChildAt(index) as Chip
                    if (chip.isChecked) checked = true
                }
            }
        }

        return checked
    }

    private fun showSnackBar(s:String){
        Snackbar.make(binding.filterbyrootConstraintLayout,s,Snackbar.LENGTH_LONG).show()
    }

}
