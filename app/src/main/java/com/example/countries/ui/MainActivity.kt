package com.example.countries.ui

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.R
import com.example.countries.api.CountriesApi

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val countriesApi by lazy {
        CountriesApi(
            Uri.parse("https://gist.githubusercontent.com/peymano-wmt/32dcb892b06648910ddd40406e37fdab/raw/db25946fd77c5873b0303b858e861ce724e0dcd0/")
        )
    }
    private val viewModel by viewModels<MainViewModel> {
        MainViewModelFactory(countriesApi)
    }
    private lateinit var adapter: CountriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = CountriesAdapter()
        findViewById<RecyclerView>(R.id.country_list).let { countryList ->
            countryList.layoutManager = LinearLayoutManager(this)
            countryList.adapter = adapter
        }
        viewModel.countries.observe(this) {
            adapter.countries = it
        }
        viewModel.error.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.countries.value?.isEmpty() != false) {
            viewModel.updateCountries()
        }
    }
}
