package com.example.projektv10.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Income(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val description: String,
    val amount: Double,
    val date: LocalDateTime
)