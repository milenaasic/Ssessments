package com.ssessments.filter_fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssessments.database.FilterItem
import com.ssessments.database.PredefinedFilter
import com.ssessments.databinding.PredefinedFilterRecviewItemBinding


class PredefinedFiltersAdapter(val clickListener: PredefinedFilterItemClickListener)
    : RecyclerView.Adapter<PredefinedFiltersAdapter.MyViewHolder>() {


    var dataList= listOf<PredefinedFilter>()
        set(value) {
            field=value
            notifyDataSetChanged()

        }

    override fun getItemCount(): Int {
        return dataList.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{
        return MyViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: PredefinedFiltersAdapter.MyViewHolder, position: Int) {
        holder.bind(clickListener,dataList[position])
    }

    class MyViewHolder private constructor(val binding: PredefinedFilterRecviewItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: PredefinedFilterItemClickListener,item: PredefinedFilter){
            binding.singleFilterItem=item
            binding.myfilterclickListener=clickListener
            binding.executePendingBindings()

        }


        companion object{
            fun from(parent: ViewGroup): MyViewHolder {
                val inflater= LayoutInflater.from(parent.context)
                val binding = PredefinedFilterRecviewItemBinding.inflate(inflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

}

class PredefinedFilterItemClickListener(val clickListener:(id:Long)->Unit ){
    fun onClick(item: PredefinedFilter)=clickListener(item.ID)
}


