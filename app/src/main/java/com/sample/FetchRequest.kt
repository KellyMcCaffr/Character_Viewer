package com.sample

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList

object FetchRequest {

    fun fetchCountries(
        url: String,
        context: Context
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO){

                val inputStream:InputStream

                val connection:HttpURLConnection = URL(url).openConnection() as HttpURLConnection

                connection.connect()

                inputStream = connection.inputStream

                if(inputStream != null) {
                    val countryItems: List<CharacterItem> = getItemListFromStream(inputStream)
                    Log.e("435345","Here is country items list: " + countryItems)
                    //(context as MainActivity).setViewsToCountryList(countryItems)
                }
            }
        }
    }

    private fun getItemListFromStream(
        json: InputStream
    ): List<CharacterItem> {
        val result: ArrayList<CharacterItem> = ArrayList()
        try {
            val outputStream = ByteArrayOutputStream()
            val buffer = ByteArray(8000)
            var length = 0
            while (json.read(buffer).also { length = it } != -1) {
                outputStream.write(buffer, 0, length)
            }
            val jsonObject = JSONObject(outputStream.toString("UTF-8"))
            val jsonArray = jsonObject.get("RelatedTopics") as JSONArray
            Log.e("34234","Here is JSON array length: " + jsonArray.length())
            val gson = Gson()
            var c = 0
            while (c < jsonArray.length()) {
                val jsonElement = JsonParser().parse(jsonArray.get(c).toString())
                Log.e("34234","Here is JSON element from array: " + jsonElement)
                val countryItem = gson.fromJson(jsonElement, CharacterItem::class.java)
                result.add(countryItem)
                c++
            }
        } catch (e: JsonParseException) {
            Log.e("454545","JSON parse exception occurred: " + e)
        }

        return result.toList()
    }
}