package com.example.fae

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.example_number.view.*

/**
 * A simple [Fragment] subclass.
 */
class PhoneFragment : Fragment() {
    protected lateinit var rootView: View
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateComponent()
    }

    private fun onCreateComponent() {
        adapter = ListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_phone, container, false);
        initView()
        return rootView
    }

    private fun initView() {
        setUpAdapter()
        initializeRecyclerView()
        setUpDummyData()
    }

    private fun setUpAdapter() {
        adapter.setOnItemClickListener(onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(position: Int, view: View?) {
                val number = adapter.getItem(position)
                val intent =
                    Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(number?.number)))
                startActivity(intent)
            }
        })
    }


    private fun setUpDummyData() {
        val list: ArrayList<ExampleNumber> = ArrayList()
        list.add(ExampleNumber("606798166", R.drawable.ic_local_phone_black_24dp))
        list.add(ExampleNumber("User 2", R.drawable.ic_local_phone_black_24dp))
        list.add(ExampleNumber("User 3", R.drawable.ic_local_phone_black_24dp))
        list.add(ExampleNumber("User 4", R.drawable.ic_local_phone_black_24dp))
        list.add(ExampleNumber("User 5", R.drawable.ic_local_phone_black_24dp))
        list.add(ExampleNumber("User 6", R.drawable.ic_local_phone_black_24dp))
        list.add(ExampleNumber("User 7", R.drawable.ic_local_phone_black_24dp))
        list.add(ExampleNumber("User 8", R.drawable.ic_local_phone_black_24dp))
        list.add(ExampleNumber("User 9", R.drawable.ic_local_phone_black_24dp))
        adapter.addItems(list)
    }

    private fun initializeRecyclerView() {
        recyclerView = rootView.findViewById(R.id.phone_recycler)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View?)
    }


    abstract class BaseRecyclerViewAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var list: ArrayList<T>? = ArrayList<T>()
        protected var itemClickListener: OnItemClickListener? = null

        fun addItems(items: ArrayList<T>) {
            this.list?.addAll(items)
            reload()
        }

        fun clear() {
            this.list?.clear()
            reload()
        }

        fun getItem(position: Int): T? {
            return this.list?.get(position)
        }

        fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
            this.itemClickListener = onItemClickListener
        }

        override fun getItemCount(): Int = list!!.size

        private fun reload() {
            Handler(Looper.getMainLooper()).post { notifyDataSetChanged() }
        }
    }


    class ListAdapter : BaseRecyclerViewAdapter<ExampleNumber>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return MyViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.example_number, parent, false)
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val myHolder = holder as? MyViewHolder
            myHolder?.setUpView(ExampleNumber = getItem(position))
        }

        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

            private val imageView: ImageView = view.image_view
            private val textView: TextView = view.text_view

            init {
                view.setOnClickListener(this)
            }

            fun setUpView(ExampleNumber: ExampleNumber?) {
                ExampleNumber?.resId?.let { imageView.setImageResource(it) }
                textView.text = ExampleNumber?.number
            }

            override fun onClick(v: View?) {
                itemClickListener?.onItemClick(adapterPosition, v)
            }
        }
    }
}
