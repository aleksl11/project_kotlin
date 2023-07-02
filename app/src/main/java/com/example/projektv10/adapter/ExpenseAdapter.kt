package com.example.projektv10.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room.databaseBuilder
import com.example.projektv10.R
import com.example.projektv10.data.AppDatabase
import com.example.projektv10.data.ExpenseDao
import com.example.projektv10.entities.Expense
import java.math.RoundingMode

/**
 * Adapter for the [RecyclerView] in [FetchExpensesActivity]. Displays [Expense] data object.
 */
class   ExpenseAdapter(
    private val context: Context,
    private var dataset: MutableList<Expense>,
) : RecyclerView.Adapter<ExpenseAdapter.ItemViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just an Affirmation object.
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_title)
        val delbtn: ImageButton = view.findViewById(R.id.delbtn)
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        val date = item.date.toString().substring(0,10)
        holder.textView.text = "${item.description}\n${item.amount.toBigDecimal().setScale(2, RoundingMode.UP)} PLN\n$date\nCategory: ${item.category}";

        holder.delbtn.setOnClickListener {
            val db = databaseBuilder(
                context,
                AppDatabase::class.java, "app_database"
            ).allowMainThreadQueries().build()
            val expenseDao: ExpenseDao = db.expenseDao()
            expenseDao.deleteById(dataset[position].id)
            dataset.removeAt(position)

            notifyDataSetChanged()
            db.close()
        }
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount() = dataset.size

}