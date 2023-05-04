package com.example.addabooking.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.addabooking.data.BookingDatabase
import com.example.addabooking.data.BookingDetails
import com.example.addabooking.data.BookingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookingViewModel(application: Application) : AndroidViewModel(application) {

    private val _readAllBooking : MutableLiveData<List<BookingDetails>> = MutableLiveData()
    val readAllBooking : LiveData<List<BookingDetails>> = _readAllBooking
    private val repository : BookingRepository

    init {
        val bookingDao = BookingDatabase.getDatabase(application).bookingDao()
        repository = BookingRepository(bookingDao)
    }

    fun readAllBooking(){
        viewModelScope.launch {
           _readAllBooking.value = repository.readAllData()
        }
    }

    fun addBooking(bookingDetails: BookingDetails){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addBooking(bookingDetails)
        }
    }
}