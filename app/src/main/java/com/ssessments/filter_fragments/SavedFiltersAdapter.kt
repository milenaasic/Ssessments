package com.ssessments.filter_fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssessments.R
import com.ssessments.database.FilterItem
import com.ssessments.databinding.SavedFilterRecviewItemBinding

class SavedFiltersAdapter(val clickListener: FilterItemClickListener,val deleteClickListener: FilterItemDeleteClickListener)
    : RecyclerView.Adapter<SavedFiltersAdapter.MyViewHolder>() {

    /*var dataList= listOf<FilterItem>(FilterItem(),FilterItem(),FilterItem(),
        FilterItem(),FilterItem(),FilterItem(),FilterItem (),FilterItem(),FilterItem(),FilterItem(),FilterItem ())
        set(value) {
            field=value
            notifyDataSetChanged()

        }*/

    var dataList= listOf<FilterItem>()
        set(value) {
            field=value
            notifyDataSetChanged()

        }

    override fun getItemCount(): Int {
        return dataList.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        return MyViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(clickListener,deleteClickListener,dataList[position])
    }

    class MyViewHolder private constructor(val binding: SavedFilterRecviewItemBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: FilterItemClickListener,deleteClickListener: FilterItemDeleteClickListener,item:FilterItem){
            binding.singleFilterItem=item
            binding.myfilterclickListener=clickListener
            binding.myDeleteClickListener=deleteClickListener
            binding.executePendingBindings()

        }


        companion object{
            fun from(parent: ViewGroup): MyViewHolder {
                val inflater= LayoutInflater.from(parent.context)
                val binding = SavedFilterRecviewItemBinding.inflate(inflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

}

class FilterItemClickListener(val clickListener:(id:Long)->Unit ){
    fun onClick(item:FilterItem)=clickListener(item.ID)
}

class FilterItemDeleteClickListener(val deleteClickListener:(filterId:Long)->Unit){
    fun onClick(item:FilterItem)=deleteClickListener(item.ID)
}
