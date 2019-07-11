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

class NewsAdapter:RecyclerView.Adapter<NewsAdapter.MyViewHolder>() {

    var dataList= listOf<NewsItem>()
    set(value) {
        field=value
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder.from(parent)
    }




    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    class MyViewHolder private constructor(val binding: NewsItemLayoutRecviewBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(item:NewsItem){
            binding.singleNewsItem=item
            binding.executePendingBindings()
            /*binding.newsTitle.text=item.title
            binding.newsdate.text=item.dateTime
            binding.tagovi.text=item.tags
            binding.userType.text=item.userType*/
        }
        companion object{
            fun from(parent: ViewGroup): MyViewHolder {
                val inflater=LayoutInflater.from(parent.context)
                val binding:NewsItemLayoutRecviewBinding=DataBindingUtil.inflate(inflater,R.layout.news_item_layout_recview,parent,false)

                return MyViewHolder(binding)
            }
        }

    }
}

class NewsItemClickListener(view:NewsAdapter.MyViewHolder)