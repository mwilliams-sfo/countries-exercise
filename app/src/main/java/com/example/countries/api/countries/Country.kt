package com.example.countries.api.countries

import android.util.JsonReader
import com.example.countries.api.JsonSerializer
import java.io.IOException

data class Country(
    val name: String,
    val region: String,
    val capital: String,
    val code: String
) {
    companion object {
        @Throws(IOException::class)
        fun fromJson(reader: JsonReader): Country {
            val map = JsonSerializer().parseObject(reader) {
                when (val name = reader.nextName()) {
                    "name", "region", "capital", "code" -> name to reader.nextString()
                    else -> {
                        reader.skipValue()
                        null
                    }
                }
            }
            return Country(
                name = validateProperty(map, "name"),
                region = validateProperty(map, "region"),
                capital = validateProperty(map, "capital"),
                code = validateProperty(map, "code")
            )
        }

        private fun <T> validateProperty(map: Map<String, Any?>, name: String): T {
            if (!map.containsKey(name)) {
                throw IOException("Missing property: $name")
            }
            @Suppress("UNCHECKED_CAST")
            return map[name] as T
        }
    }
}
