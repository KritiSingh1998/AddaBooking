package com.example.addabooking.data

import androidx.lifecycle.LiveData

class BookingRepository(private val bookingDao: BookingDao) {

    suspend fun readAllData(): List<BookingDetails>{
        return bookingDao.readAllData()
    }

   suspend fun addBooking(bookingDetails: BookingDetails) {
        bookingDao.addBooking(bookingDetails)
    }
}