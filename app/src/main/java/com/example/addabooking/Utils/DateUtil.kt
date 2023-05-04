package com.example.addabooking.Utils

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import java.util.Calendar

class DateUtil(private val onDateSelected: (String) -> Unit) : DatePickerDialog.OnDateSetListener {
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        onDateSelected.invoke("$dayOfMonth-$month-$year")
    }

    fun showDatePickerDialog(context: Context) {
        val datePickerDialog = DatePickerDialog(
            context,
            this,
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis
        datePickerDialog.show()
    }
}