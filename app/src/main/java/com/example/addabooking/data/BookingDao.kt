package com.example.addabooking.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BookingDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
   suspend fun addBooking(bookingDetails: BookingDetails)

   @Query("SELECT * FROM booking_table")
   suspend fun readAllData() : List<BookingDetails>
}