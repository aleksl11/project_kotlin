package com.example.projektv10

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projektv10.data.AppDatabase
import com.example.projektv10.entities.Expense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ExpensesActivity: AppCompatActivity() {
    lateinit var appDb:AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses)

        val categories = resources.getStringArray(R.array.Categories)
        val spinner : Spinner = findViewById(R.id.spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spinner.adapter = adapter

        val addButton: Button = findViewById(R.id.b1)
        appDb = AppDatabase.getDatabase(this)
        addButton.setOnClickListener{
            writeData(appDb)
        }
        val fetch: Button = findViewById(R.id.b2)
        fetch.setOnClickListener{
            val intent = Intent(this@ExpensesActivity, FetchExpensesActivity::class.java)
            startActivity(intent)
        }

    }

    private fun writeData(appDb:AppDatabase){
        val descriptionField : EditText = findViewById(R.id.t1)
        val description : String = descriptionField.text.toString()
        val amountField : EditText = findViewById(R.id.t2)
        val amount : Double = if(amountField.text.toString().isNotEmpty()) {
            amountField.text.toString().toDouble()
        }else {
            0.00
        }
        val dateField : DatePicker = findViewById(R.id.datePicker1)
        val year : Int = dateField.year
        val month : Int = dateField.month+1
        val day : Int = dateField.dayOfMonth
        val date = LocalDateTime.of(year,month,day,0,0,0)

        val spinner : Spinner = findViewById(R.id.spinner)
        val category = spinner.selectedItem.toString()

        if(description.isNotEmpty() && amount!=0.00){
            val expense = Expense(0,description,amount,date,category)
            GlobalScope.launch(Dispatchers.IO) {
                appDb.expenseDao().addExpense(expense)
            }
            Toast.makeText(this@ExpensesActivity, "Added a new expense", Toast.LENGTH_SHORT).show()
            amountField.text.clear()
            descriptionField.text.clear()
        }
        else{
            Toast.makeText(this@ExpensesActivity, "Wrong inputs", Toast.LENGTH_SHORT).show()
        }


    }
}