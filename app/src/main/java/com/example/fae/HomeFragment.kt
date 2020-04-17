package com.example.fae

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exampleList=generateDummyList(500)
        recycler_view.adapter=ExampleAdapter(exampleList)
        recycler_view.layoutManager=LinearLayoutManager(activity)
    }
    private fun generateDummyList(size: Int): List<ExampleItem>{
        val list=ArrayList<ExampleItem>()
        for (i in 0 until size){
            val drawable = when (i%3){
                0 -> R.drawable.ic_settings_input_antenna_black_24dp
                1 -> R.drawable.ic_subway_black_24dp
                else -> R.drawable.ic_thumb_up_black_24dp
            }
            val item=ExampleItem(drawable, "Item $i","Line 2")
            list+=item
        }
        return list
    }


}
