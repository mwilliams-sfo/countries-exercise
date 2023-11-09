package com.example.countries.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.countries.R

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<TextView>(R.id.country_name).text = "Canada, NA"
        findViewById<TextView>(R.id.country_capital).text = "Ottawa"
        findViewById<TextView>(R.id.country_code).text = "CA"
    }
}
