package com.example.countries.ui

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.R
import com.example.countries.api.CountriesApi
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val api = CountriesApi(
        Uri.parse("https://gist.githubusercontent.com/peymano-wmt/32dcb892b06648910ddd40406e37fdab/raw/db25946fd77c5873b0303b858e861ce724e0dcd0/")
    )
    private lateinit var adapter: CountriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = CountriesAdapter()
        findViewById<RecyclerView>(R.id.country_list).let { countryList ->
            countryList.layoutManager = LinearLayoutManager(this)
            countryList.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            adapter.countries = api.getCountries()
        }
    }
}
