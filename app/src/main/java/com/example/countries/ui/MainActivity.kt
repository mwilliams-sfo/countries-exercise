package com.example.countries.ui

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.countries.api.CountriesApi
import com.example.countries.R
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val api = CountriesApi()

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val countries = api.getCountries()
            val country = countries.getOrNull(0) ?: return@launch
            findViewById<TextView>(R.id.country_name).text = String.format("%s, %s", country.name, country.region)
            findViewById<TextView>(R.id.country_capital).text = country.capital
            findViewById<TextView>(R.id.country_code).text = country.code
        }
    }
}
