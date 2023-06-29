package com.example.projektv10

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.projektv10.adapter.ExpenseAdapter
import com.example.projektv10.data.AppDatabase
import com.example.projektv10.entities.Expense
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FetchExpensesActivity: AppCompatActivity() {
    private lateinit var appDb: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch_expenses)
        appDb = AppDatabase.getDatabase(this)
        readData(appDb)

    }


    private fun readData(appDb: AppDatabase){
        var expenses: MutableList<Expense>
        GlobalScope.launch {
            expenses = appDb.expenseDao().readAllData()
            displayData(expenses)
        }
    }

    private suspend fun displayData(expenses: MutableList<Expense>){
        if(expenses.isNotEmpty()){
            val recyclerView = findViewById<RecyclerView>(R.id.recycle_view)
            recyclerView.adapter = ExpenseAdapter(this, expenses)

            recyclerView.setHasFixedSize(true)
        }
    }
}