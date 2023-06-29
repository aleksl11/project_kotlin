package com.example.projektv10.data

import androidx.room.*
import com.example.projektv10.entities.Expense

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM Expense")
    fun readAllData(): MutableList<Expense>

    @Query("SELECT * FROM Expense order by amount")
    fun getExpensesOrderedByAmount(): MutableList<Expense>

    @Query("Select * from Expense order by date")
    fun getExpensesOrderedByDate(): MutableList<Expense>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addExpense(expense: Expense)

    @Query("DELETE FROM Expense WHERE id = :id")
    fun deleteById(id: Int)

    @Delete
    fun delete(expense: Expense)
}