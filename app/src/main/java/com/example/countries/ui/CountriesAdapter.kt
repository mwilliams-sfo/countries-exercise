package com.example.countries.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.R
import com.example.countries.api.Country
import com.google.android.material.textview.MaterialTextView

class CountriesAdapter : RecyclerView.Adapter<CountriesAdapter.ViewHolder>() {
    var countries: List<Country> = emptyList()
        set(value) {
            notifyItemRangeRemoved(0, field.size)
            field = value
            notifyItemRangeInserted(0, field.size)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_country, parent, false)
        )

    override fun getItemCount(): Int = countries.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country = countries[position]
        holder.run {
            countryName.text = String.format("%s, %s", country.name, country.region)
            countryCapital.text = country.capital
            countryCode.text = country.code
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val countryName: MaterialTextView = itemView.findViewById(R.id.country_name)
        val countryCapital: MaterialTextView = itemView.findViewById(R.id.country_capital)
        val countryCode: MaterialTextView = itemView.findViewById(R.id.country_code)
    }
}
