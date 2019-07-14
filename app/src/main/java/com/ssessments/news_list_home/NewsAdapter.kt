package com.ssessments.news_list_home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssessments.R
import com.ssessments.data.NewsItem
import com.ssessments.databinding.NewsItemLayoutRecviewBinding

class NewsAdapter(val clickListener: NewsItemClickListener,val clickShareListener: NewsItemShareClickListener):RecyclerView.Adapter<NewsAdapter.MyViewHolder>() {

    var dataList= listOf<NewsItem>()
    set(value) {
        field=value
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder.from(parent)
    }




    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(clickListener,clickShareListener,dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    class MyViewHolder private constructor(val binding: NewsItemLayoutRecviewBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(clickListener: NewsItemClickListener,clickShareListener: NewsItemShareClickListener,item:NewsItem){
            binding.singleNewsItem=item
            binding.clickListener=clickListener
            binding.clickShareListener=clickShareListener
            binding.executePendingBindings()

        }
        companion object{
            fun from(parent: ViewGroup): MyViewHolder {
                val inflater=LayoutInflater.from(parent.context)
                val binding = NewsItemLayoutRecviewBinding.inflate(inflater, parent, false)
               // val binding:NewsItemLayoutRecviewBinding=DataBindingUtil.inflate(inflater,R.layout.news_item_layout_recview,parent,false)

                return MyViewHolder(binding)
            }
        }

    }
}

class NewsItemClickListener(val clickListener:(newsId:Long)->Unit ){
    fun onClick(item:NewsItem)=clickListener(item.id)
}

class NewsItemShareClickListener(val clickListener:(view:NewsItem)->Unit ){
    fun onClick(item:NewsItem)=clickListener(item)
}