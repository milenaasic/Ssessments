package com.ssessments.newsapp.news_list_home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssessments.newsapp.database.NewsItem
import com.ssessments.newsapp.databinding.NewsItemLayoutRecviewBinding

class NewsAdapter(val clickListener: NewsItemClickListener):RecyclerView.Adapter<NewsAdapter.MyViewHolder>() {

    var dataList= listOf<NewsItem>()
    set(value) {
        field=value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(clickListener,dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    class MyViewHolder private constructor(val binding: NewsItemLayoutRecviewBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(clickListener: NewsItemClickListener,item:NewsItem){
            binding.singleNewsItem=item
            binding.clickListener=clickListener
            binding.executePendingBindings()

        }
        companion object{
            fun from(parent: ViewGroup): MyViewHolder {
                val inflater=LayoutInflater.from(parent.context)
                val binding = NewsItemLayoutRecviewBinding.inflate(inflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }
}

class NewsItemClickListener(val clickListener:(newsId:Int)->Unit ){
    fun onClick(item:NewsItem)=clickListener(item.newsID)
}

