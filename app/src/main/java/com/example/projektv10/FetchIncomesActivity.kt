package com.example.projektv10

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.projektv10.adapter.IncomeAdapter
import com.example.projektv10.data.AppDatabase
import com.example.projektv10.entities.Income
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FetchIncomesActivity: AppCompatActivity() {
    private lateinit var appDb: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch_expenses)
        appDb = AppDatabase.getDatabase(this)
        readData(appDb)

    }


    private fun readData(appDb: AppDatabase){
        var incomes: MutableList<Income>
        GlobalScope.launch {
            incomes = appDb.incomeDao().readAllData()
            displayData(incomes)
        }
    }

    private fun displayData(incomes: MutableList<Income>){
        if(incomes.isNotEmpty()){
            val recyclerView = findViewById<RecyclerView>(R.id.recycle_view)
            recyclerView.adapter = IncomeAdapter(this, incomes)

            recyclerView.setHasFixedSize(true)
        }
    }
}