package com.ssessments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ssessments.data.NewsItem

class NewsAdapter(var list: List<NewsItem>):RecyclerView.Adapter<NewsAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view=LayoutInflater.from(parent.context).inflate(R.layout.news_item_layout_recview,parent,false)
        return MyViewHolder(view)
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
    holder.title.text=list[position].title
    holder.date.text=list[position].dateTime
    holder.tags.text=list[position].tags
    holder.userType.text=list[position].userType

    }

    override fun getItemCount(): Int {
        return list.size
    }


    class MyViewHolder(val view: View):RecyclerView.ViewHolder(view){

        val title:TextView=view.findViewById(R.id.newsTitle)
        val date:TextView=view.findViewById(R.id.newsdate)
        val tags:TextView=view.findViewById(R.id.tagovi)
        val userType:TextView=view.findViewById(R.id.userType)


    }
}