package com.example.countries.api.countries

import android.util.JsonReader
import android.util.JsonToken
import java.io.IOException
import kotlin.jvm.Throws

data class Country(
    val name: String,
    val region: String,
    val capital: String,
    val code: String
) {
    companion object {
        @Throws(IOException::class)
        fun fromJson(reader: JsonReader): Country {
            val map = mutableMapOf<String, Any?>()
            reader.beginObject()
            while (reader.peek() != JsonToken.END_OBJECT) {
                val name = reader.nextName()
                when {
                    map.containsKey(name) -> {
                        throw IOException("Repeated JSON property name: $name")
                    }
                    name == "name" || name == "region" || name == "capital" || name == "code" -> {
                        map[name] = reader.nextString()
                    }
                    else -> reader.skipValue()
                }
            }
            reader.endObject()
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
