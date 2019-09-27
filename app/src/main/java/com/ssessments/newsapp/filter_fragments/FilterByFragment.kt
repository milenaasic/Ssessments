package com.ssessments.newsapp.filter_fragments

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
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.ssessments.newsapp.R
import com.ssessments.newsapp.database.FilterItem
import com.ssessments.newsapp.utilities.*
import java.util.*
import java.text.DateFormat
import kotlin.collections.ArrayList
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ssessments.newsapp.MainActivityViewModel
import com.ssessments.newsapp.database.CurrentFilter
import com.ssessments.newsapp.database.UserData
import com.ssessments.newsapp.databinding.FilterByFragmentBinding

private const val DEFAULT_FILTER_NAME="My Filter"
private const val DEFAULT_DATE_TEXT="Select"
private const val MYTAG="MYFILTERBYFRAGMENT"

class FilterByFragment : Fragment() {

    private lateinit var binding: FilterByFragmentBinding
    private lateinit var sharedviewModel: FilterPagerSupportSharedViewModel
    private lateinit var mainActivityViewModel: MainActivityViewModel
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
        } ?: throw Throwable("Invalid Parent Fragment")

        mainActivityViewModel=requireActivity().run {
            ViewModelProviders.of(this)[MainActivityViewModel::class.java]
        }

        mainActivityViewModel.loggedInUser.observe(this,Observer{ user->
            myuserData=user
            if(user==null)binding.saveButton.isEnabled=false
            else binding.saveButton.isEnabled=true

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

            val alertDialog= AlertDialog.Builder(requireActivity(),R.style.MyAlertDialogTheme)

            val myinflater = this.layoutInflater
            val dialogView = myinflater.inflate(R.layout.save_filter_alert_dialog, null)

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
            chip31.setOnCheckedChangeListener { buttonView, isChecked -> toggleChipsInGroup(chip31,marketsChips) }
            chip32.setOnCheckedChangeListener { buttonView, isChecked -> toggleChipsInGroup(chip32,marketsChips)}
            chip33.setOnCheckedChangeListener { buttonView, isChecked -> toggleChipsInGroup(chip33,marketsChips)}
            chip34.setOnCheckedChangeListener { buttonView, isChecked -> toggleChipsInGroup(chip34,marketsChips) }
            chip35.setOnCheckedChangeListener { buttonView, isChecked -> toggleChipsInGroup(chip35,marketsChips) }
            chip36.setOnCheckedChangeListener { buttonView, isChecked -> toggleChipsInGroup(chip36,marketsChips) }
        }

        binding.apply {
            chip1.setOnCheckedChangeListener { buttonView, isChecked -> toggleChipsInGroup(chip1,productChips)}
            chip2.setOnCheckedChangeListener { buttonView, isChecked -> toggleChipsInGroup(chip2,productChips) }
            chip3.setOnCheckedChangeListener { buttonView, isChecked -> toggleChipsInGroup(chip3,productChips) }
            chip4.setOnCheckedChangeListener { buttonView, isChecked -> toggleChipsInGroup(chip4,productChips) }
            chip5.setOnCheckedChangeListener { buttonView, isChecked -> toggleChipsInGroup(chip5,productChips) }
        }

        binding.apply {
            chip11.setOnCheckedChangeListener { buttonView, isChecked -> toggleChipsInGroup(chip11,ssessmentChips)}
            chip12.setOnCheckedChangeListener { buttonView, isChecked -> toggleChipsInGroup(chip12,ssessmentChips) }
            chip13.setOnCheckedChangeListener { buttonView, isChecked -> toggleChipsInGroup(chip13,ssessmentChips) }
            chip14.setOnCheckedChangeListener { buttonView, isChecked -> toggleChipsInGroup(chip14,ssessmentChips) }
            chip15.setOnCheckedChangeListener { buttonView, isChecked -> toggleChipsInGroup(chip15,ssessmentChips) }
            chip16.setOnCheckedChangeListener { buttonView, isChecked -> toggleChipsInGroup(chip16,ssessmentChips)  }
            chip17.setOnCheckedChangeListener { buttonView, isChecked -> toggleChipsInGroup(chip17,ssessmentChips) }
            chip18.setOnCheckedChangeListener { buttonView, isChecked -> toggleChipsInGroup(chip18,ssessmentChips) }
        }


        //SHARED VIEW MODEL OBSERVERS
        sharedviewModel.currentFilter.observe(this,Observer {currentFilter->
           //if(currentFilter!=null){
                Log.i(MYTAG,"current filter koga gledam je $currentFilter")
                setChipsInChipGroupToList(binding.marketsChips,convertStringWithCommasToArray(currentFilter.market))
                setChipsInChipGroupToList(binding.productChips, convertStringWithCommasToArray(currentFilter.product))
                setChipsInChipGroupToList(binding.ssessmentChips, convertStringWithCommasToArray(currentFilter.ssessment))
                binding.fromDatePicker.setText(currentFilter.dateFrom)
                binding.toDatePicker.setText(currentFilter.dateTo)
            //}
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
        //prvo iskljucim sve cipove
        chipgroup.clearCheck()
        //cekiram chipove koji su u datoj listi

        for (item in list){
            Log.i(MYTAG,"pretvorena arraz lista je $list, a item je $item")
            chipgroup.apply {
                for (index in 0..this.childCount) {
                    if (getChildAt(index) != null) {
                        val chip = getChildAt(index) as Chip
                        if (chip.tag.equals(item)) {
                            chip.isChecked = true
                        }
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

    //resetovanje:iskljucim  sve i ukljucim ALl chip
    private fun resetChipGroup(chips: ChipGroup) {
        chips.clearCheck()
        val allChip=chips.getChildAt(0) as Chip
        allChip.setChecked(true)
    }



    //ide u tabelu current_filter
    private fun getCurrentFilterValues():CurrentFilter{

        return CurrentFilter(
            market=convertArrayListToStringWithCommas(getCheckedChips(binding.marketsChips)),
            product=convertArrayListToStringWithCommas(getCheckedChips(binding.productChips)),
            ssessment = convertArrayListToStringWithCommas(getCheckedChips(binding.ssessmentChips)),
            language = getCheckedLanguage(),
            dateFrom = binding.fromDatePicker.text.toString(),
            dateTo = binding.toDatePicker.text.toString()

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

    //PROVERA DA LI SU SVI CIPOVI, OSIM ALL CIPA, ISKLJUCENI
    private fun chipsInGroupDisabledBesidesAllChip(chipGroup: ChipGroup):Boolean {
        var allDisabled = true
        chipGroup.apply {
            for (index in 1..this.childCount) {
                if (getChildAt(index) != null) {
                    val chip = getChildAt(index) as Chip
                    if (chip.isChecked) {
                            allDisabled=false
                             break
                    }
                }
            }
        }

        return allDisabled
    }

    //PROVERA DA LI SU SVI CIPOVI, OSIM ALL CIPA, UKLJUCENI
    private fun chipsInGroupEnabledBesidesAllChip(chipGroup: ChipGroup):Boolean {
        var allEnabled = true
        chipGroup.apply {
            for (index in 1..this.childCount) {
                if (getChildAt(index) != null) {
                    val chip = getChildAt(index) as Chip
                    if (!chip.isChecked) {
                        allEnabled=false
                        break
                    }
                }
            }
        }

        return allEnabled
    }

    private fun toggleChipsInGroup(chip:Chip,chipgroup: ChipGroup){
        val allChip = chipgroup.getChildAt(0) as Chip
        //ako sam upravo ukljucila neki chip
        if(chip.isChecked){
            if(chipsInGroupEnabledBesidesAllChip(chipgroup)){
                chipgroup.clearCheck()
                allChip.isChecked=true
            }else allChip.isChecked=false
        }
        //ako je upravo islljucen neki chip
        if(!chip.isChecked){
            if(chipsInGroupDisabledBesidesAllChip(chipgroup)) allChip.isChecked=true
        }

    }


    private fun showSnackBar(s:String){
        Snackbar.make(binding.filterbyrootConstraintLayout,s,Snackbar.LENGTH_LONG).show()
    }

}
