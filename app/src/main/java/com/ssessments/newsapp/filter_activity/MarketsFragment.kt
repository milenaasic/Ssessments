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
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.ChipGroup
import com.ssessments.newsapp.MainActivityViewModel

import com.ssessments.newsapp.R
import com.ssessments.newsapp.databinding.FragmentMarketsBinding
import com.ssessments.newsapp.utilities.*


class MarketsFragment : Fragment() {


    private lateinit var binding: FragmentMarketsBinding
    private lateinit var filterActivityViewModel:FilterActivityViewModel
    private lateinit var arrayChipGroups:Array<ChipGroup>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_markets,container,false)

        filterActivityViewModel=requireActivity().run {
            ViewModelProviders.of(this)[FilterActivityViewModel::class.java]
        }

        setTagsToChips(binding.allmarketsChipGroup,convertStringWithCommasToRealArray(enumValues<AllMarkets>().joinToString { it.value }))
        setTagsToChips(binding.asiapacificChipGroup,convertStringWithCommasToRealArray(enumValues<AsiaPacificMarkets>().joinToString { it.value }))
        setTagsToChips(binding.africaChipGroup,convertStringWithCommasToRealArray(enumValues<AfricaMarkets>().joinToString { it.value }))
        setTagsToChips(binding.americasChipGroup,convertStringWithCommasToRealArray(enumValues<AmericasMarkets>().joinToString { it.value }))
        setTagsToChips(binding.europeChipGroup,convertStringWithCommasToRealArray(enumValues<EuropeMarkets>().joinToString { it.value }))
        setTagsToChips(binding.icsmarketsChipGroup,convertStringWithCommasToRealArray(enumValues<ICSMarkets>().joinToString { it.value }))
        setTagsToChips(binding.middleeastChipGroup, convertStringWithCommasToRealArray(enumValues<MiddleEastMarkets>().joinToString { it.value }))
        setTagsToChips(binding.russiaChipGroup,convertStringWithCommasToRealArray(enumValues<RussiaMarkets>().joinToString { it.value }))
        setTagsToChips(binding.worldChipGroup,convertStringWithCommasToRealArray(enumValues<WorldMarkets>().joinToString { it.value }))

        arrayChipGroups=arrayOf(
            binding.asiapacificChipGroup,
            binding.africaChipGroup,
            binding.americasChipGroup,
            binding.europeChipGroup,
            binding.icsmarketsChipGroup,
            binding.middleeastChipGroup,
            binding.russiaChipGroup,
            binding.worldChipGroup)


        // All markets chip
        binding.chip0.setOnCheckedChangeListener { button, isChecked -> if(isChecked) clearChipGroups( arrayChipGroups)
                                                                        else {
                                                                                if(!isThereACheckedChip(arrayChipGroups)) {
                                                                                    button.isChecked=true
                                                                                }
                                                                        }


        }


        //chip listeners
        setChipListeners(binding.asiapacificChipGroup,binding.chip0, arrayChipGroups)
        setChipListeners(binding.africaChipGroup,binding.chip0,arrayChipGroups)
        setChipListeners(binding.americasChipGroup,binding.chip0,arrayChipGroups)
        setChipListeners(binding.europeChipGroup,binding.chip0, arrayChipGroups)
        setChipListeners(binding.icsmarketsChipGroup,binding.chip0,arrayChipGroups)
        setChipListeners(binding.middleeastChipGroup,binding.chip0,arrayChipGroups)
        setChipListeners(binding.russiaChipGroup,binding.chip0,arrayChipGroups)
        setChipListeners(binding.worldChipGroup,binding.chip0,arrayChipGroups)


        // Clear buttons
        binding.clearAllAP.setOnClickListener { clearChipGroups(arrayOf(binding.asiapacificChipGroup)) }

        binding.clearAllAM.setOnClickListener { clearChipGroups(arrayOf(binding.americasChipGroup)) }

        binding.clearAllEU.setOnClickListener { clearChipGroups(arrayOf(binding.europeChipGroup)) }

        binding.clearAllICS.setOnClickListener { clearChipGroups(arrayOf(binding.icsmarketsChipGroup)) }

        binding.clearAllMiddleEast.setOnClickListener { clearChipGroups(arrayOf(binding.middleeastChipGroup)) }



        binding.resetfilterButtonMarkets.setOnClickListener { 
                                                            clearChipGroups(arrayChipGroups)
                                                            binding.chip0.isChecked=true }
        
        binding.applyfilterButtonmarkets.setOnClickListener { 
                val tags=getCheckedMarketTags()
                filterActivityViewModel.setChosenMarketsTags(tags)
                filterActivityViewModel.startNavigateFromMarketsToCustomFragment()

         }


        return binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filterActivityViewModel.mychosenMarketsTags.observe(this,Observer{
            setCheckedMarketsChips(it)

        })


        filterActivityViewModel.navigateFromMarketsToCustomFragment.observe(this, Observer{
            if(it){
                findNavController().navigate(MarketsFragmentDirections.actionMarketsFragmentToCustomFilterFragment())
                filterActivityViewModel.navigateFromMarketsToCustomFragmentFinished()
            }
        })

    }

    private fun getCheckedMarketTags(): ArrayList<String> {

        if (binding.chip0.isChecked) return arrayListOf(AllMarkets.ALL_MARKETS.value)
        else return getCheckedChipsInTheseChipGroups(arrayChipGroups)


    }

    private fun setCheckedMarketsChips(currentlySetMarketChips: ArrayList<String>?) {
    if(currentlySetMarketChips!=null){
        if(!currentlySetMarketChips.contains(AllMarkets.ALL_MARKETS.value)) {

            setCheckedChipsInTheseChipGroups(currentlySetMarketChips,arrayChipGroups)
        }
        }
    }


}



