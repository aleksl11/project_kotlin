package com.example.projektv10

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val summaryButton: Button = findViewById(R.id.summaryButton)
        val expensesButton: Button = findViewById(R.id.expensesButton)
        val incomesButton: Button = findViewById(R.id.incomesButton)

        summaryButton.setOnClickListener{
            val intent = Intent(this@MainActivity, SummaryActivity::class.java)
            startActivity(intent)
        }
        expensesButton.setOnClickListener{
            val intent = Intent(this@MainActivity, ExpensesActivity::class.java)
            startActivity(intent)
        }
        incomesButton.setOnClickListener{
            val intent = Intent(this@MainActivity, IncomesActivity::class.java)
            startActivity(intent)
        }


    }
}