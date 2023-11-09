package com.example.countries.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class CountriesApi {
    @Throws(IOException::class)
    suspend fun getCountries(): List<Country> = withContext(Dispatchers.IO) {
        val connection = URL("https://gist.githubusercontent.com/peymano-wmt/32dcb892b06648910ddd40406e37fdab/raw/db25946fd77c5873b0303b858e861ce724e0dcd0/countries.json")
            .openConnection()
            as HttpURLConnection
        try {
            connection.addRequestProperty("Accept", "application/json")
            when (connection.responseCode) {
                -1 -> throw IOException("Connection failed")
                HttpURLConnection.HTTP_OK -> Unit
                else -> throw IOException(connection.responseMessage)
            }
            val body = InputStreamReader(connection.inputStream, Charsets.UTF_8).use { reader ->
                reader.readText()
            }
            val bodyJson = JSONArray(body)
            (0 until bodyJson.length()).map { i ->
                val countryJson = bodyJson.getJSONObject(i)
                Country(
                    name = countryJson.getString("name"),
                    region = countryJson.getString("region"),
                    capital = countryJson.getString("capital"),
                    code = countryJson.getString("code")
                )
            }
        } finally {
            connection.errorStream?.close()
            connection.inputStream?.close()
        }
    }
}