package com.example.fae

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_home.*
import net.theluckycoder.expandablecardview.ExpandableCardView
import androidx.appcompat.widget.SearchView
import java.util.*

/**
 * Home/Encyclopedia class with FirestoreAdapter and Filter
 */
class HomeFragment : Fragment() {
    val titles = ArrayList<String>()
    val descriptions = ArrayList<String>()
    var titlesFiltered = ArrayList<String>()
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
        rootRef.collection("zdarzenia").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (item in task.result!!) {
                    val title = item.data["text1"].toString().replace("\\n","\n")
                    val description = item.data["text2"].toString().replace("\\n","\n")
                    titles.add(title)
                    descriptions.add(description)
                }
                titlesFiltered=titles
            }
            adapter?.startListening()
        }




        item_search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                    adapter!!.filter.filter(newText)
                return false
            }

        })
    }


    override fun onStop() {
        super.onStop()
        if (adapter != null) {
            adapter!!.stopListening()
        }
    }

    private inner class ExampleViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {
        internal fun setText() {
            val card = view.findViewById<ExpandableCardView>(R.id.card)
                card.cardTitle = titlesFiltered[position]
                card.cardDescription = descriptions[position]

        }
    }

    private inner class ItemFirestoreRecyclerAdapter(options: FirestoreRecyclerOptions<ExampleItem>) :
        FirestoreRecyclerAdapter<ExampleItem, ExampleViewHolder>(options), Filterable {


        override fun onBindViewHolder(
            ExampleViewHolder: ExampleViewHolder,
            position: Int,
            ExampleItem: ExampleItem
        ) {
                ExampleViewHolder.setText()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.example_item, parent, false)
            return ExampleViewHolder(view)
        }

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val charSearch = constraint.toString()
                    titlesFiltered = if (charSearch.isEmpty()) {
                        titles
                    } else {
                        val resultList = ArrayList<String>()
                        for (row in titles) {
                            if (row.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                                resultList.add(row)
                            }
                        }
                        resultList
                    }
                    val filterResults = FilterResults()
                    filterResults.values = titlesFiltered
                    return filterResults
                }

                @Suppress("UNCHECKED_CAST")
                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    titlesFiltered = results?.values as ArrayList<String>
                    notifyDataSetChanged()
                }

            }

        }

        override fun getItemCount(): Int {
            return titlesFiltered.size
        }
    }

}