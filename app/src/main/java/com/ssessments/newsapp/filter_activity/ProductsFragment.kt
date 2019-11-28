package com.ssessments.newsapp.filter_activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.ChipGroup
import com.ssessments.newsapp.R
import com.ssessments.newsapp.databinding.FragmentProductsBinding
import com.ssessments.newsapp.utilities.*

private const val MYTAGPr="ProductsFragment"

class ProductsFragment : Fragment() {

    private lateinit var binding:FragmentProductsBinding
    private lateinit var filterActivityViewModel:FilterActivityViewModel
    private lateinit var arrayChipGroupsProducts:Array<ChipGroup>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_products,container,false)

        filterActivityViewModel=requireActivity().run {
            ViewModelProviders.of(this)[FilterActivityViewModel::class.java]
        }


        setTagsToChips(binding.allproductsChipGroup,convertStringWithCommasToRealArray(enumValues<AllProducts>().joinToString { it.value }))
        setTagsToChips(binding.plasticsChipGroup,convertStringWithCommasToRealArray(enumValues<Plastics>().joinToString { it.value }))
        setTagsToChips(binding.chemicalsChipGroup,convertStringWithCommasToRealArray(enumValues<Chemicals>().joinToString { it.value }))
        setTagsToChips(binding.energyChipGroup,convertStringWithCommasToRealArray(enumValues<EnergyFeedstocks>().joinToString { it.value }))

        arrayChipGroupsProducts=arrayOf(
            binding.plasticsChipGroup,
            binding.chemicalsChipGroup,
            binding.energyChipGroup
            )


        // All products chip
        binding.productschip0.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked) clearChipGroups(arrayChipGroupsProducts)
            else {
                if (!isThereACheckedChip(arrayChipGroupsProducts)) {
                    button.isChecked = true
                }
            }
        }


        //chip listeners
        setChipListeners(binding.plasticsChipGroup,binding.productschip0, arrayChipGroupsProducts)
        setChipListeners(binding.chemicalsChipGroup,binding.productschip0,arrayChipGroupsProducts)
        setChipListeners(binding.energyChipGroup,binding.productschip0,arrayChipGroupsProducts)

        // Clear buttons
        binding.clearAllPL.setOnClickListener { clearChipGroups(arrayOf(binding.plasticsChipGroup)) }
        binding.clearAllCH.setOnClickListener { clearChipGroups(arrayOf(binding.chemicalsChipGroup)) }
        binding.clearAllEN.setOnClickListener { clearChipGroups(arrayOf(binding.energyChipGroup)) }


        binding.resetfilterButtonProducts.setOnClickListener {
            clearChipGroups(arrayChipGroupsProducts)
            binding.productschip0.isChecked=true }

        binding.applyfilterButtonProducts.setOnClickListener {
            val tags=getCheckedProductsTags()
            filterActivityViewModel.setChosenProductsTags(tags)
            filterActivityViewModel.startNavigateFromProductsToCustomFragment()

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filterActivityViewModel.mychosenProductsTags.observe(this, Observer{
            setCheckedProductsChips(it)
        })

        filterActivityViewModel.navigateFromProductsToCustomFragment.observe(this, Observer{
            if(it){
                Log.i(MYTAGPr,"u navigate to custom fragment $it")
                filterActivityViewModel.navigateFromProductsToCustomFragmentFinished()
                findNavController().navigate(ProductsFragmentDirections.actionProductsFragmentToCustomFilterFragment())
                Log.i(MYTAGPr,"kraj navigate to custom fragment $it")
            }
        })

    }

    private fun getCheckedProductsTags(): ArrayList<String> {
        if (binding.productschip0.isChecked) return arrayListOf(AllProducts.ALL_PRODUCTS.value)
        else return getCheckedChipsInTheseChipGroups(arrayChipGroupsProducts)

    }


    private fun setCheckedProductsChips(productChips: ArrayList<String>?) {
        if(productChips!=null) {
            if (!productChips.contains(AllProducts.ALL_PRODUCTS.value)) {
                setCheckedChipsInTheseChipGroups(productChips, arrayChipGroupsProducts)
            }
        }
    }

}
