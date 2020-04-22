package com.example.fae

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.example_item.*
import kotlinx.android.synthetic.main.example_item.view.*
import kotlinx.android.synthetic.main.example_item.view.card
import kotlinx.android.synthetic.main.fragment_home.*
import net.theluckycoder.expandablecardview.ExpandableCardView
import java.util.Locale.filter

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    private var adapter: ItemFirestoreRecyclerAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.layoutManager = LinearLayoutManager(activity)
        val rootRef = FirebaseFirestore.getInstance()
        val query = rootRef.collection("zdarzenia").orderBy("text1", Query.Direction.ASCENDING)
        val options =
            FirestoreRecyclerOptions.Builder<ExampleItem>().setQuery(query, ExampleItem::class.java)
                .build()
        adapter = ItemFirestoreRecyclerAdapter(options)
        recycler_view.adapter = adapter
    }


    private inner class ExampleViewHolder internal constructor(private val view: View) :
        RecyclerView.ViewHolder(view) {
        internal fun setText(text1: String, text2: String) {
            val card = view.findViewById<ExpandableCardView>(R.id.card)
            card!!.cardTitle = text1
            card.cardDescription = text2
        }
    }

    private inner class ItemFirestoreRecyclerAdapter internal constructor(options: FirestoreRecyclerOptions<ExampleItem>) :
        FirestoreRecyclerAdapter<ExampleItem, ExampleViewHolder>(options) {
        override fun onBindViewHolder(
            ExampleViewHolder: ExampleViewHolder,
            position: Int,
            ExampleItem: ExampleItem
        ) {
            ExampleViewHolder.setText(ExampleItem.text1, ExampleItem.text2)


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.example_item, parent, false)
            return ExampleViewHolder(view)
        }

    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        if (adapter != null) {
            adapter!!.stopListening()
        }
    }


}