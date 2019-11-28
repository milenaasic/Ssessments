package com.ssessments.newsapp.filter_activity


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
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

import com.ssessments.newsapp.R
import com.ssessments.newsapp.databinding.FragmentServicetypeBinding
import com.ssessments.newsapp.utilities.Services
import com.ssessments.newsapp.utilities.convertStringWithCommasToRealArray

private const val MYTAG="ServicetypeFragment"
class ServicetypeFragment : Fragment() {


    private lateinit var binding:FragmentServicetypeBinding
    private lateinit var filterActivityViewModel:FilterActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_servicetype,container,false)

        filterActivityViewModel=requireActivity().run {
            ViewModelProviders.of(this)[FilterActivityViewModel::class.java]
        }

        setTagsToChips(binding.serviceChipGroup,convertStringWithCommasToRealArray(enumValues<Services>().joinToString { it.value }))

        // All services chip
        binding.sechip0.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked){
                binding.serviceChipGroup.clearCheck()
                binding.sechip0.isChecked=true
            }
            else {
                if (!isThereACheckedChip(arrayOf(binding.serviceChipGroup))) {
                    binding.sechip0.isChecked=true
                }
            }
        }


        //chip listeners
        setChipListenerForSingleChipGroup(binding.serviceChipGroup)


        binding.resetfilterButtonServices.setOnClickListener {
            clearChipGroups(arrayOf(binding.serviceChipGroup))
            binding.sechip0.isChecked=true }

        binding.applyfilterButtonServices.setOnClickListener {
            val tags=getCheckedservicesTags()
            filterActivityViewModel.setChosenServicesTags(tags)
            filterActivityViewModel.startNavigateFromServicesToCustomFragment()

        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filterActivityViewModel.mychosenServicesTags.observe(this,Observer{
            setCheckedservicesChips(it)
        })


        filterActivityViewModel.navigateFromServicesToCustomFragment.observe(this, Observer{
            if(it){
                findNavController().navigate(ServicetypeFragmentDirections.actionServicetypeFragmentToCustomFilterFragment())
                filterActivityViewModel.navigateFromServicesToCustomFragmentFinished()
            }
        })

    }



    private fun getCheckedservicesTags(): ArrayList<String> {
        if (binding.sechip0.isChecked) return arrayListOf(Services.ALL_SERVICES.value)
        else return getCheckedChipsInTheseChipGroups(arrayOf(binding.serviceChipGroup))
    }


    private fun setCheckedservicesChips(serviceChips: ArrayList<String>?) {
    Log.i(MYTAG,"service chips $serviceChips")
        if (serviceChips!=null) {
            if (!serviceChips.contains(Services.ALL_SERVICES.value)) {
                setCheckedChipsInTheseChipGroups(serviceChips, arrayOf(binding.serviceChipGroup))
            }
        }
    }

    fun setChipListenerForSingleChipGroup(chipgroup:ChipGroup){

        val  chip0=chipgroup.getChildAt(0)as Chip

        for (i in 0..chipgroup.childCount - 1) {
            val chip = chipgroup.getChildAt(i) as Chip
            when {
                i == 0 -> chip.setOnCheckedChangeListener { button, isChecked ->
                    if (isChecked) {
                        chipgroup.clearCheck()
                        chip.isChecked = true

                    } else if (!isThereACheckedChip(arrayOf(chipgroup))) chip0.isChecked = true
                }

                else -> chip.setOnCheckedChangeListener { button, isChecked ->
                    if(isChecked){
                        chip0.isChecked=false
                    }else {
                        if(!isThereACheckedChip(arrayOf(chipgroup))) chip0.isChecked = true

                    }

                }
            }

        }
    }



}
