package com.sample

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import com.google.gson.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

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
                        processCharacterData(it, callback, context)
                    },
                    {
                        Log.e("434435","Load characters failed with error: " + it)
                    }
                )
        )
    }

    private fun processCharacterData(
        dataObject: JsonObject,
        callback: MainActivity.Callback,
        context: Context
    ) {
        val result: ArrayList<ResponseItem> = ArrayList()
        try {
            val jsonArray = dataObject.get(context.getString(R.string.character_response_related_topics)) as JsonArray
            val gson = Gson()
            var c = 0
            while (c < jsonArray.size()) {
                result.add(convertResponseItem(getCharacterItemAtPosition(jsonArray, c, gson)))
                c++
            }
        } catch (e: JsonParseException) {
            Log.e("435534","Json parse exception occurred: " + e)
        }
        callback.onCharacterListLoaded(result)
    }

    private fun getCharacterItemAtPosition(
        array: JsonArray,
        position: Int,
        gson: Gson
    ): ResponseItem {
        val jsonElement = JsonParser().parse(array.get(position).toString())
        return gson.fromJson(jsonElement, ResponseItem::class.java)
    }

    private fun convertResponseItem(
        item: ResponseItem
    ): ResponseItem {
        val text = item.Text ?: ""
        val characterName = text.subSequence(0, text.indexOf('-') - 1)
        val characterDescription = text.subSequence(text.indexOf('-') + 2, text.length)
        Log.e("353545","Here is character name retrieved: " + characterName)
        Log.e("353545","Here is character description retrieved: " + characterDescription)
        //return CharacterItem("", null, )
        return item
    }

    /*private fun getImageFromIconObject(
        iconObject: HashMap<String, String>
    ): Drawable {

    }*/
}






