package com.ssessments.newsapp.filter_activity

import android.util.Log
import androidx.core.view.get
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.ssessments.newsapp.utilities.Markets
import com.ssessments.newsapp.utilities.convertStringWithCommasToArray

fun clearChipGroups(arrayChipGroup: Array<ChipGroup>){
    for(value in arrayChipGroup){
        value.clearCheck()
    }
}


fun setTagsToChips(chipGroup: ChipGroup,stringArray:Array<String>){

    if(chipGroup.childCount==stringArray.size){
            for(i in 0..chipGroup.childCount-1) {
                val chip=chipGroup.getChildAt(i) as Chip
                chip.tag=stringArray[i]
                chip.text=stringArray[i]
            }
    }else throw Exception("broj elemenata chip grupe i enuma nije isti") as Throwable
}



fun isThereACheckedChip(arrayChipGroup: Array<ChipGroup>):Boolean{

    for (value in arrayChipGroup){
        Log.i("ChipGropuFunc","value je $value")
        for (i in 0..value.childCount-1){
            val chip= value.getChildAt(i) as Chip
            Log.i("ChipGropuFunc","chip je je ${chip.text}")
            if(chip.isChecked) return true
            Log.i("ChipGropuFunc","chip grpu redni brok je $i")
        }
    }
    return false
}

fun setChipListeners(chipGroup: ChipGroup,allMarketsChip:Chip,array:Array<ChipGroup>) {

    for (i in 0..chipGroup.childCount - 1) {
        val chip = chipGroup.getChildAt(i) as Chip
        when {
            i == 0 -> chip.setOnCheckedChangeListener { button, isChecked ->
                if (isChecked) {
                    chipGroup.clearCheck()
                    chip.isChecked = true
                    allMarketsChip.isChecked = false
                } else if (!isThereACheckedChip(array)) allMarketsChip.isChecked = true
            }

            else -> chip.setOnCheckedChangeListener { button, isChecked ->
                if(isChecked){
                    val chip0=chipGroup.getChildAt(0)as Chip
                    chip0.isChecked=false
                    allMarketsChip.isChecked = false
                }else {
                    if(!isThereACheckedChip(array)) allMarketsChip.isChecked = true

                }

            }
        }

    }
}


fun getCheckedChipsInTheseChipGroups(chipGropuList:Array<ChipGroup>):ArrayList<String>{

    val checkedChipList=ArrayList<String>()

    for (value in chipGropuList){
        for(index in 0..value.childCount-1){
            val chip=value.getChildAt(index) as Chip
            if (chip.isChecked) checkedChipList.add(chip.tag as String)

        }
    }
    return checkedChipList

}

fun setCheckedChipsInTheseChipGroups(currentlySetMarketChips:ArrayList<String>,chipGropuList:Array<ChipGroup>){

    for (value in chipGropuList){
        myloop@
        for (index in 1..value.childCount-1){
            val chip=value.getChildAt(index) as Chip
                for (s in currentlySetMarketChips){
                        if(s.equals(chip.tag)){
                            chip.isChecked=true
                            continue@myloop

                        }
                }

        }
    }

}



