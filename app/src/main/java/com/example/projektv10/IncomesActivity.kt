package com.example.projektv10

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.projektv10.data.AppDatabase
import com.example.projektv10.entities.Income
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class IncomesActivity: AppCompatActivity() {
    private lateinit var appDb: AppDatabase

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incomes)

        val addButton: Button = findViewById(R.id.bi1);
        appDb = AppDatabase.getDatabase(this)
        addButton.setOnClickListener{
            writeData(appDb)
        }
        val fetch: Button = findViewById(R.id.bi2);
        fetch.setOnClickListener{
            val intent = Intent(this@IncomesActivity, FetchIncomesActivity::class.java)
            startActivity(intent)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun writeData(appDb:AppDatabase){
        val descriptionField : EditText = findViewById(R.id.ti1)
        val description : String = descriptionField.text.toString()
        val amountField : EditText = findViewById(R.id.ti2)
        val amount : Double = if(amountField.text.toString().isNotEmpty()) {
            amountField.text.toString().toDouble()
        }else {
            0.00
        }
        val dateField : DatePicker = findViewById(R.id.datePicker2)
        val year : Int = dateField.year
        val month : Int = dateField.month+1
        val day : Int = dateField.dayOfMonth
        val date = LocalDateTime.of(year,month,day,0,0,0)

        if(description.isNotEmpty() && amount!=0.00){
            val income = Income(0,description,amount,date)
            GlobalScope.launch(Dispatchers.IO) {
                appDb.incomeDao().addIncome(income)
            }
            Toast.makeText(this@IncomesActivity, "Added a new income", Toast.LENGTH_SHORT).show()
            amountField.text.clear()
            descriptionField.text.clear()
        }
        else{
            Toast.makeText(this@IncomesActivity, "Wrong inputs", Toast.LENGTH_SHORT).show()
        }
    }

}