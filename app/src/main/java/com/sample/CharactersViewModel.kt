package com.sample

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.ArrayList

class CharactersViewModel {

    fun loadCharacterData(
        callback: MainActivity.Callback,
        compositeDisposable: CompositeDisposable,
        context: Context
    ) {
        val mInterface = EndpointsImpl()
        compositeDisposable.add(
            mInterface.getCharacterList()
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        Log.e("454545","Here is json object returned for check:: " + it)
                        val result: ArrayList<CharacterItem> = ArrayList()
                        try {
                            val jsonArray = it.get(context.getString(R.string.character_response_related_topics)) as JsonArray
                            val gson = Gson()
                            var c = 0
                            while (c < jsonArray.size()) {
                                val jsonElement = JsonParser().parse(jsonArray.get(c).toString())
                                Log.e("34234","Here is JSON element from array: " + jsonElement)
                                val countryItem = gson.fromJson(jsonElement, CharacterItem::class.java)
                                result.add(countryItem)
                                c++
                            }
                        } catch (e: JsonParseException) {
                            Log.e("435534","Json parse exception occurred: " + e)
                        }
                        callback.onCharacterListLoaded(result)
                    },
                    {
                        Log.e("434435","Load characters failed with error: " + it)
                    }
                )
        )
    }
}