package com.example.countries.api.countries

import android.net.Uri
import android.util.JsonReader
import android.util.JsonToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
            JsonReader(InputStreamReader(connection.inputStream, Charsets.UTF_8)).use { reader ->
                reader.beginArray()
                val countries = generateSequence {
                    if (reader.peek() != JsonToken.END_ARRAY) {
                        Country.fromJson(reader)
                    } else null
                }.toList()
                reader.endArray()
                countries
            }
        } finally {
            connection.disconnect()
        }
    }
}