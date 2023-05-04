package com.example.addabooking.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.addabooking.Utils.AppConstants
import com.example.addabooking.R
import com.example.addabooking.Utils.DateUtil
import com.example.addabooking.data.BookingDetails
import com.example.addabooking.databinding.ActivityBookingBinding
import com.example.addabooking.viewmodel.BookingViewModel

class BookingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingBinding
    private lateinit var viewModel: BookingViewModel
    private var startTime: Int = 0
    private var endTime: Int = 0
    private var date = ""
    private var facilityType = ""
    private var totalCost = 0
    private var bookingDetailsList: List<BookingDetails> = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(BookingViewModel::class.java)
        setContentView(binding.root)

        if (intent.hasExtra(AppConstants.FACILITY_TYPE)) {
            val facility = intent.getStringExtra(AppConstants.FACILITY_TYPE)
            if (facility != null) {
                facilityType = facility
            }
        }
        viewModel.readAllBooking()
        subscribeObserver()
        setupTimeInputHint()
        setupTimeAdapter()
        binding.date.date.setOnClickListener {
            showDatePickerDialog(selectedDateBehavior)
        }
        binding.avilabilityButton.setOnClickListener {
            if (validInputs()) {
                checkAvailability()
            } else {
                binding.bookingAvailability.visibility = View.GONE
                binding.bookingDetails.root.visibility = View.GONE
            }
        }

        binding.bookingDetails.confirmButton.setOnClickListener {
            addBookingToDatabase()
            Toast.makeText(this, "Booking Confirmed", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun showBookingAvailabilityDetails() {
        binding.bookingAvailability.visibility = View.VISIBLE
        binding.bookingDetails.root.visibility = View.VISIBLE
        binding.bookingAvailability.text = getString(R.string.booking_availability)

        binding.bookingDetails.bookingFacilty.text =
            resources.getString(R.string.facility, facilityType)
        binding.bookingDetails.bookingDate.text = resources.getString(R.string.date, date)
        binding.bookingDetails.bookingTime.text =
            resources.getString(R.string.time, startTime.toString(), endTime.toString())
        binding.bookingDetails.bookingHour.text =
            resources.getString(R.string.hours, (endTime - startTime).toString())
        setupPrice()
        binding.bookingDetails.bookingPrice.text = getString(R.string.price, totalCost.toString())
        if (facilityType == AppConstants.TENNIS_COURT)
            binding.bookingDetails.bookingRate.text = getString(R.string.rate_tenniscourt)
        else
            binding.bookingDetails.bookingRate.text = getString(R.string.rate_clubhouse)
    }

    private fun setupPrice() {
        val hours = endTime - startTime
        if (facilityType == AppConstants.TENNIS_COURT) {
            totalCost = hours * AppConstants.TENNIS_COURT_RATE
        } else {
//
            if (startTime >= 10 && endTime <= 16)
                totalCost = hours * AppConstants.CLUB_HOUSE_MORNING_RATE
            else if (startTime > 16 && endTime <= 22)
                totalCost = hours * AppConstants.CLUB_HOUSE_EVENING_RATE
            else {
                val morningHour = 16 - startTime
                val eveningHour = hours - morningHour
                totalCost =
                    morningHour * AppConstants.CLUB_HOUSE_MORNING_RATE + eveningHour * AppConstants.CLUB_HOUSE_EVENING_RATE
            }
        }

    }

    private fun showDatePickerDialog(selectedDateBehaviour: (String) -> Unit) {
        DateUtil {
            selectedDateBehaviour.invoke(it)
        }.showDatePickerDialog(binding.root.context)
    }

    private val selectedDateBehavior: (String) -> Unit = {
        binding.date.date.setText(it.substringBefore("format"))
        date = it.substringAfter("format")
    }

    private fun setupTimeInputHint() {
        binding.timeTo.layout.hint = getString(R.string.time_to)
        binding.timeFrom.layout.hint = getString(R.string.time_from)

    }

    private fun setupTimeAdapter() {
        val timeArray = ArrayList<Int>()
        for (i in 10..22)
            timeArray.add(i)
        val timeArrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, timeArray)
        binding.timeFrom.time.setAdapter(timeArrayAdapter)
        binding.timeTo.time.setAdapter(timeArrayAdapter)
    }

    private fun validInputs(): Boolean {
        startTime = binding.timeFrom.time.text.toString().toInt()
        endTime = binding.timeTo.time.text.toString().toInt()
        return startTime != 0 && endTime != 0 && date != "" && isvalidTime()
    }

    private fun isvalidTime(): Boolean {
        return if (endTime <= startTime) {
            binding.error.visibility = View.VISIBLE
            false
        } else {
            binding.error.visibility = View.GONE
            true
        }
    }

    private fun subscribeObserver() {
        viewModel.readAllBooking.observe(this) {
            bookingDetailsList = it
            Log.e("List", it.toString())
            Log.e("List", bookingDetailsList.toString())
        }

    }

    private fun checkAvailability() {
        var doesBookingExist = false
        val bookingDetails = BookingDetails(
            0,
            facilityType,
            date,
            startTime,
            endTime,
            endTime - startTime,
            totalCost
        )
        for (booking in bookingDetailsList) {
            if (booking.facilityType == bookingDetails.facilityType && booking.date == bookingDetails.date && booking.startTime == bookingDetails.startTime && booking.endTime == bookingDetails.endTime) {
                doesBookingExist = true
                break
            }
        }
        if (doesBookingExist) {
            binding.bookingDetails.root.visibility = View.GONE
            binding.bookingAvailability.visibility = View.VISIBLE
            binding.bookingAvailability.text = getString(R.string.booking_failure)
        } else
            showBookingAvailabilityDetails()
    }

    private fun addBookingToDatabase() {
        viewModel.addBooking(
            BookingDetails(
                0,
                facilityType,
                date,
                startTime,
                endTime,
                endTime - startTime,
                totalCost
            )
        )
    }
}