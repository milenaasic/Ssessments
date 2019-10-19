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

    //TEXT SCALING
    var textSizeMultiplier=1f


    fun setTextSizeParameter(textMultiplier:Float){
        textSizeMultiplier=textMultiplier
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(clickListener,dataList[position],textSizeMultiplier)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    class MyViewHolder private constructor(val binding: NewsItemLayoutRecviewBinding):RecyclerView.ViewHolder(binding.root){

        private val defaultTextSizeTitle =16f
        private val defaultTextSizeAccessDateTags=12f

        fun bind(clickListener: NewsItemClickListener,item:NewsItem,textSizeMultiplier:Float){
            binding.singleNewsItem=item
            binding.clickListener=clickListener
            binding.newsTitle.textSize=defaultTextSizeTitle.times(textSizeMultiplier)
            binding.tagovi.textSize=defaultTextSizeAccessDateTags.times(textSizeMultiplier)
            binding.newsdate.textSize=defaultTextSizeAccessDateTags.times(textSizeMultiplier)
            binding.userType.textSize=defaultTextSizeAccessDateTags.times(textSizeMultiplier)
            binding.executePendingBindings()

        }
        companion object{
            fun from(parent: ViewGroup): MyViewHolder {
                val inflater=LayoutInflater.from(parent.context)
                val binding = NewsItemLayoutRecviewBinding.inflate(inflater, parent, false)
                //textView.textSize=defaultFontSize.times(systemFontScale).times(chosenFontSize)
                return MyViewHolder(binding)
            }
        }

    }
}

class NewsItemClickListener(val clickListener:(newsId:Int)->Unit ){
    fun onClick(item:NewsItem)=clickListener(item.newsID)
}

