package com.example.countries.api.countries

import android.net.Uri
import android.util.JsonReader
import android.util.JsonToken
import com.example.countries.api.JsonSerializer
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
            connection.addRequestProperty("Accept", "application/json; charset=utf-8")
            val responseCode = connection.responseCode
            when {
                responseCode == -1 -> throw IOException("Invalid response")
                responseCode != HttpURLConnection.HTTP_OK -> {
                    throw IOException("HTTP status: ${connection.responseMessage}")
                }
            }
            JsonReader(InputStreamReader(connection.inputStream, Charsets.UTF_8)).use { reader ->
                JsonSerializer().parseArray(reader) { Country.fromJson(it) }.also {
                    if (reader.peek() != JsonToken.END_DOCUMENT) {
                        throw IOException("Trailing data in response")
                    }
                }
            }
        } finally {
            connection.disconnect()
        }
    }
}