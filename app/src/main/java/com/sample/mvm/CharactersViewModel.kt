package com.sample.mvm

import android.content.Context
import android.util.Log
import com.google.gson.*
import com.sample.R
import com.sample.Utils
import com.sample.view.MainActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.ArrayList

class CharactersViewModel {

    fun loadCharacterData(
        callback: MainActivity.Callback,
        compositeDisposable: CompositeDisposable,
        context: Context
    ) {
        val mInterface = RequestApiImpl()
        compositeDisposable.add(
            mInterface.getCharacterList()
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        processCharacterData(it, callback, context)
                    },
                    {
                        Log.e("434435", "Load characters failed with error: " + it)
                    }
                )
        )
    }

    private fun processCharacterData(
        dataObject: JsonObject,
        callback: MainActivity.Callback,
        context: Context
    ) {
        val result: ArrayList<CharacterItem> = ArrayList()
        try {
            val jsonArray = dataObject.get(context.getString(R.string.response_key_related_topics)) as JsonArray
            Log.e("435534","Here is character json array size: " + jsonArray.size())
            val gson = Gson()
            var c = 0
            while (c < jsonArray.size()) {
                Log.e("435534","Added item at position: " + c)
                Log.e("435345","Here is name added: " + convertResponseItem(getCharacterItemAtPosition(jsonArray, c, gson), context).name)
                result.add(convertResponseItem(getCharacterItemAtPosition(jsonArray, c, gson), context))
                c++
            }
        } catch (e: JsonParseException) {
            Log.e("435534", "Json parse exception occurred: " + e)
        }  catch (e: StringIndexOutOfBoundsException) {
            Log.e("435534", "String out of bounds exception occurred: " + e)
        }
        callback.onCharacterListLoaded(result, context)
    }

    private fun convertResponseItem(
        item: ResponseItem,
        context: Context
    ): CharacterItem {
        val text = item.Text ?: ""
        val characterDescription = text.subSequence(text.indexOf('-') + 2, text.length).toString()
        val imageWidthHeight = Utils.getImageWidthHeight(item.Icon, context)
        val imageUrl = item.Icon?.get(context.getString(R.string.response_key_icon_url)) ?: ""
        return CharacterItem(getCharacterName(text), characterDescription, imageWidthHeight.first,
            imageWidthHeight.second, imageUrl)
    }

    private fun getCharacterName(
        text: String
    ): String {
        val characterName = text.subSequence(0, text.indexOf('-') - 1).toString()
        val indexOfParenthesisStart = characterName.indexOf('(')
        return if (indexOfParenthesisStart == -1 || indexOfParenthesisStart == 0) {
            characterName
        } else {
            characterName.subSequence(0, indexOfParenthesisStart - 1).toString()
        }
    }

    private fun getCharacterItemAtPosition(
        array: JsonArray,
        position: Int,
        gson: Gson
    ): ResponseItem {
        val jsonElement = JsonParser().parse(array.get(position).toString())
        return gson.fromJson(jsonElement, ResponseItem::class.java)
    }

    /*private fun getBitmapFromIconObject(
        iconObject: HashMap<String, String>?,
        firstUrl: String,
        context: Context
    ): Bitmap {
        val imageUrl = iconObject?.get(context.getString(R.string.response_key_icon_url)) ?: ""
        //Log.e("45354534", "Here is image url found: " + imageUrl)
        var bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.default_character_image)
        if (imageUrl.isNotEmpty()) {
            try {
                val connection: HttpURLConnection =
                    URL(firstUrl + imageUrl).openConnection() as HttpURLConnection
                connection.connect()
                val input: InputStream = connection.inputStream
                val unscaledBitmap = BitmapFactory.decodeStream(input)
                if (imageWidth > MAX_CHARACTER_IMAGE_WIDTH) {
                    imageWidth = MAX_CHARACTER_IMAGE_WIDTH
                }
                if (imageHeight > MAX_CHARACTER_IMAGE_HEIGHT) {
                    imageHeight = MAX_CHARACTER_IMAGE_HEIGHT
                }
                if (unscaledBitmap != null) {
                    bitmap = Bitmap.createScaledBitmap(
                        unscaledBitmap,
                        imageWidth,
                        imageHeight,
                        true
                    )
                }
            } catch (e: MalformedURLException) {
                Log.e("54453","Malformed url exception occurred for: " + imageUrl)
            }
        }
        return bitmap
    }*/
}