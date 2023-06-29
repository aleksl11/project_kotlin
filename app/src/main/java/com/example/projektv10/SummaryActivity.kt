package com.example.projektv10

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.projektv10.data.AppDatabase
import com.example.projektv10.entities.Expense
import com.example.projektv10.entities.Income
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.time.LocalDateTime


class SummaryActivity: AppCompatActivity() {
    lateinit var appDb: AppDatabase
    lateinit var pieChart: PieChart
    lateinit var expenses: MutableList<Expense>
    lateinit var incomes: MutableList<Income>
    lateinit var modifiedExpenses: MutableList<Expense>
    lateinit var modifiedIncomes: MutableList<Income>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)
        appDb = AppDatabase.getDatabase(this)

        readData(appDb)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked
            modifiedExpenses = mutableListOf()
            modifiedIncomes = mutableListOf()
            val today : LocalDateTime = LocalDateTime.now()
            // Check which radio button was clicked
            when (view.getId()) {
                R.id.month ->
                    if (checked) {
                        expenses.forEach {
                            if (it.date.month == today.month && it.date.year == today.year) {
                                modifiedExpenses.add(it)
                            }
                        }
                        incomes.forEach {
                            if (it.date.month == today.month && it.date.year == today.year) {
                                modifiedIncomes.add(it)
                            }
                        }
                        displayData(modifiedExpenses,modifiedIncomes)
                    }
                R.id.year ->
                    if (checked) {
                        expenses.forEach {
                            if (it.date.year == today.year) {
                                modifiedExpenses.add(it)
                            }
                        }
                        incomes.forEach {
                            if (it.date.year == today.year) {
                                modifiedIncomes.add(it)
                            }
                        }
                        displayData(modifiedExpenses,modifiedIncomes)
                    }
                R.id.all ->
                    if (checked) {
                        displayData(expenses,incomes)
                    }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun readData(appDb: AppDatabase){
        GlobalScope.launch {
            expenses = appDb.expenseDao().readAllData()
            incomes = appDb.incomeDao().readAllData()
            displayData(expenses, incomes)
        }
    }

    private fun displayData(expenses: MutableList<Expense>, incomes: MutableList<Income>){
        val summary: TextView = findViewById(R.id.summaryText)
        var text = ""
        var sumExpense = 0.0
        var sumIncome = 0.0
        var sumBills = 0.0
        var sumGroceries = 0.0
        var sumTransport = 0.0
        var sumEntertainment = 0.0
        var sumOther = 0.0
        if(expenses.isNotEmpty()){
            expenses.forEach{
                sumExpense+=it.amount
            }
            text += "Total amount spent: ${sumExpense.toBigDecimal().setScale(2, RoundingMode.UP)} PLN\n"

        }
        else{
            text += "Total amount spent: 0.00 PLN\n"
        }
        if(incomes.isNotEmpty()){
            incomes.forEach{
                sumIncome+=it.amount
            }
            text += "Total amount gained: ${sumIncome.toBigDecimal().setScale(2, RoundingMode.UP)} PLN\n"
        }
        else{
            text += "Total amount gained: 0.00 PLN\n"
        }
        if(expenses.isNotEmpty()){
            expenses.forEach{
                if(it.category == "Bills")
                    sumBills+=it.amount
            }
        }
        if(expenses.isNotEmpty()){
            expenses.forEach{
                if(it.category == "Groceries")
                    sumGroceries+=it.amount
            }
        }
        if(expenses.isNotEmpty()){
            expenses.forEach{
                if(it.category == "Transport")
                    sumTransport+=it.amount
            }
        }
        if(expenses.isNotEmpty()){
            expenses.forEach{
                if(it.category == "Entertainment")
                    sumEntertainment+=it.amount
            }
        }
        if(expenses.isNotEmpty()){
            expenses.forEach{
                if(it.category == "Other")
                    sumOther+=it.amount
            }
        }
        text += "Bilance: ${(sumIncome - sumExpense).toBigDecimal().setScale(2, RoundingMode.UP)} PLN\n\n"
        if(expenses.isNotEmpty()) {
            val billsPercent =
                ((sumBills / sumExpense) * 100).toBigDecimal().setScale(2, RoundingMode.UP)
                    .toDouble().toString()
            val groceriesPercent =
                ((sumGroceries / sumExpense) * 100).toBigDecimal().setScale(2, RoundingMode.UP)
                    .toDouble().toString()
            val transportPercent =
                ((sumTransport / sumExpense) * 100).toBigDecimal().setScale(2, RoundingMode.UP)
                    .toDouble().toString()
            val entertainmentPercent =
                ((sumEntertainment / sumExpense) * 100).toBigDecimal().setScale(2, RoundingMode.UP)
                    .toDouble().toString()
            val otherPercent =
                ((sumOther / sumExpense) * 100).toBigDecimal().setScale(2, RoundingMode.UP)
                    .toDouble().toString()
            text += "Spent on bills: ${sumBills.toBigDecimal().setScale(2, RoundingMode.UP)} PLN (${billsPercent}%)\n"
            text += "Spent on groceries: ${sumGroceries.toBigDecimal().setScale(2, RoundingMode.UP)} PLN (${groceriesPercent}%)\n"
            text += "Spent on transport: ${sumTransport.toBigDecimal().setScale(2, RoundingMode.UP)} PLN (${transportPercent}%)\n"
            text += "Spent on entertainment: ${sumEntertainment.toBigDecimal().setScale(2, RoundingMode.UP)} PLN (${entertainmentPercent}%)\n"
            text += "Spent on other things: ${sumOther.toBigDecimal().setScale(2, RoundingMode.UP)} PLN (${otherPercent}%)\n"
        }
        summary.text = text;

        //chart
        if(expenses.isNotEmpty()) {
            pieChart = findViewById(R.id.pieChart)

            // on below line we are setting user percent value,
            // setting description as enabled and offset for pie chart
            pieChart.setUsePercentValues(true)
            pieChart.getDescription().setEnabled(false)
            pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

            // on below line we are setting drag for our pie chart
            pieChart.setDragDecelerationFrictionCoef(0.95f)

            // on below line we are setting hole
            // and hole color for pie chart
            pieChart.setDrawHoleEnabled(false)
            //pieChart.setHoleColor(Color.TRANSPARENT)

            // on below line we are setting circle color and alpha
            pieChart.setTransparentCircleColor(Color.TRANSPARENT)
            pieChart.setTransparentCircleAlpha(110)

            // on  below line we are setting hole radius
            pieChart.setHoleRadius(58f)
            pieChart.setTransparentCircleRadius(61f)

            // on below line we are setting center text
            pieChart.setDrawCenterText(true)

            // on below line we are setting
            // rotation for our pie chart
            //pieChart.setRotationAngle(0f)

            // enable rotation of the pieChart by touch
            //pieChart.setRotationEnabled(true)
            //pieChart.setHighlightPerTapEnabled(true)

            // on below line we are setting animation for our pie chart
            //pieChart.animateY(1400, Easing.EaseInOutQuad)

            // on below line we are disabling our legend for pie chart
            pieChart.legend.isEnabled = true
            //pieChart.setEntryLabelColor(Color.BLACK)

            // on below line we are creating array list and
            // adding data to it to display in pie chart
            val entries: ArrayList<PieEntry> = ArrayList()
            entries.add(PieEntry((sumBills / sumExpense).toFloat(), "Bills"))
            entries.add(PieEntry((sumGroceries / sumExpense).toFloat(), "Groceries"))
            entries.add(PieEntry((sumTransport / sumExpense).toFloat(), "Transport"))
            entries.add(PieEntry((sumEntertainment / sumExpense).toFloat(), "Entertainment"))
            entries.add(PieEntry((sumOther / sumExpense).toFloat(), "Other"))

            // on below line we are setting pie data set
            val dataSet = PieDataSet(entries, "Mobile OS")

            // on below line we are setting icons.
            dataSet.setDrawIcons(false)
            dataSet.setDrawValues(false);
            pieChart.setDrawEntryLabels(false)

            // on below line we are setting slice for pie
            dataSet.sliceSpace = 1f
            dataSet.iconsOffset = MPPointF(0f, 40f)
            dataSet.selectionShift = 5f

            // add a lot of colors to list
            val colors: ArrayList<Int> = ArrayList()
            colors.add(resources.getColor(R.color.purple_500))
            colors.add(resources.getColor(R.color.yellow))
            colors.add(resources.getColor(R.color.red))
            colors.add(resources.getColor(R.color.teal_200))
            colors.add(resources.getColor(R.color.green))

            // on below line we are setting colors.
            dataSet.colors = colors

            // on below line we are setting pie data set
            val data = PieData(dataSet)
            data.setValueFormatter(PercentFormatter())
            data.setValueTextSize(15f)
            data.setValueTypeface(Typeface.DEFAULT_BOLD)
            //data.setValueTextColor(Color.BLACK)
            pieChart.setData(data)

            // undo all highlights
            pieChart.highlightValues(null)

            // loading chart
            pieChart.invalidate()
        }
    }
}