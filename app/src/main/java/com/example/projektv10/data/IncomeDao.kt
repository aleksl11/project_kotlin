package com.example.projektv10.data

import androidx.room.*
import com.example.projektv10.entities.Income

@Dao
interface IncomeDao {
    @Query("SELECT * FROM Income")
    fun readAllData(): MutableList<Income>

    @Query("SELECT * FROM Income order by amount")
    fun getIncomesOrderedByAmount(): MutableList<Income>

    @Query("Select * from Income order by date")
    fun getIncomesOrderedByDate(): MutableList<Income>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addIncome(income: Income)

    @Query("DELETE FROM Income WHERE id = :id")
    fun deleteById(id: Int)

    @Delete
    fun deleteIncome(income: Income)
}
