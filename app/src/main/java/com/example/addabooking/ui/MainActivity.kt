package com.example.addabooking.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.addabooking.Utils.AppConstants
import com.example.addabooking.R
import com.example.addabooking.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupScreen()
    }

    private fun setupScreen(){
        binding.clubhouseCard.facilityName.text = getString(R.string.clubhouse)
        binding.clubhouseCard.image.setImageDrawable(ContextCompat.getDrawable(baseContext,
            R.drawable.ic_clubhouse
        ))
        binding.tennisCard.facilityName.text = getString(R.string.tennis_court)
        binding.tennisCard.image.setImageDrawable(ContextCompat.getDrawable(baseContext,
            R.drawable.ic_tennis
        ))

        binding.clubhouseCard.container.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            intent.putExtra(AppConstants.FACILITY_TYPE, AppConstants.CLUB_HOUSE)
            startActivity(intent)
        }

        binding.tennisCard.container.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            intent.putExtra(AppConstants.FACILITY_TYPE, AppConstants.TENNIS_COURT)
            startActivity(intent)
        }

    }
}
