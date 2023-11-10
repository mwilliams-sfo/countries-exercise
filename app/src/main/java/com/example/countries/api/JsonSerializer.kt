package com.example.countries.api

import android.util.JsonReader
import java.io.IOException

class JsonSerializer {
    @Throws(IOException::class)
    fun <T> parseArray(reader: JsonReader, parseValue: (JsonReader) -> T): List<T> {
        reader.beginArray()
        val result = sequence {
            while (reader.hasNext()) {
                yield(parseValue(reader))
            }
        }.toList()
        reader.endArray()
        return result
    }

    @Throws(IOException::class)
    fun <T> parseObject(reader: JsonReader, parsePair: (JsonReader) -> Pair<String, T>?): Map<String, T> {
        reader.beginObject()
        val result = sequence {
            while (reader.hasNext()) {
                parsePair(reader)?.let { yield(it) }
            }
        }.toMap()
        reader.endObject()
        return result
    }
}
