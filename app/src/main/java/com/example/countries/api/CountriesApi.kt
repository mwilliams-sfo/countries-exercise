package com.example.countries.api

import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class CountriesApi(
    private val baseUri: Uri
) {
    @Throws(IOException::class)
    suspend fun getCountries(): List<Country> = withContext(Dispatchers.IO) {
        val url = URL(
            baseUri.buildUpon()
                .appendPath("countries.json")
                .build().toString()
        )
        val connection = url.openConnection() as HttpURLConnection
        try {
            connection.addRequestProperty("Accept", "application/json")
            val responseCode = connection.responseCode
            when {
                responseCode == -1 -> throw IOException("Invalid response")
                responseCode != HttpURLConnection.HTTP_OK -> {
                    throw IOException("HTTP status: ${connection.responseMessage}")
                }
            }
            val body = InputStreamReader(connection.inputStream, Charsets.UTF_8)
                .use { reader -> reader.readText() }
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
            connection.disconnect()
        }
    }
}