package com.ssessments


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.ssessments.data.getNewsItemArray


class mainFragment : Fragment() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root=inflater.inflate(R.layout.fragment_main, container, false)

        recyclerView=root.findViewById(R.id.mainRecView)
        recyclerView.adapter=NewsAdapter(getNewsItemArray())
        recyclerView.addItemDecoration(DividerItemDecoration(activity,DividerItemDecoration.VERTICAL))

        return root
    }


    override fun onResume() {
        super.onResume()
        val activity:AppCompatActivity= activity as AppCompatActivity
        //activity.tagsLayout.visibility=View.VISIBLE
    }
}
