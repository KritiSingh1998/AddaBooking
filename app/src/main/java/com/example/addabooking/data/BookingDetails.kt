package com.example.addabooking.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "booking_table")
data class BookingDetails(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val facilityType: String,
    val date: String,
    val startTime: Int,
    val endTime: Int,
    val hours: Int,
    val price: Int
)
