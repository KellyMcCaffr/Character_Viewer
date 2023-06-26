package com.sample

import android.content.Context
import android.util.Log
import com.google.gson.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.ArrayList
import java.util.HashMap

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
            val gson = Gson()
            var c = 0
            while (c < jsonArray.size()) {
                result.add(convertResponseItem(getCharacterItemAtPosition(jsonArray, c, gson), context))
                c++
            }
        } catch (e: JsonParseException) {
            Log.e("435534", "Json parse exception occurred: " + e)
        } catch (e: StringIndexOutOfBoundsException) { }
        callback.onCharacterListLoaded(result)
    }

    private fun getCharacterItemAtPosition(
        array: JsonArray,
        position: Int,
        gson: Gson
    ): ResponseItem {
        //Log.e("453543","Item retrieved at position: " + position)
        //Log.e("453543","Array size is: " + array.size())
        val jsonElement = JsonParser().parse(array.get(position).toString())
        return gson.fromJson(jsonElement, ResponseItem::class.java)
    }

    private fun convertResponseItem(
        item: ResponseItem,
        context: Context
    ):  CharacterItem {
        val text = item.Text ?: ""
        val characterName = text.subSequence(0, text.indexOf('-') - 1).toString()
        val characterDescription = text.subSequence(text.indexOf('-') + 2, text.length).toString()
        //val characterImage = getBitmapFromIconObject(item.Icon, item.FirstURL ?: "", context)
        //Log.e("353545", "Here is character name retrieved: " + characterName)
        //Log.e("353545", "Here is character description retrieved: " + characterDescription)
        //return CharacterItem("", null, )
        val widthKey = context.getString(R.string.response_key_icon_width)
        val heightKey = context.getString(R.string.response_key_icon_height)
        val defaultWidthString = context.getString(R.string.character_image_width_default_value)
        val defaultHeightString = context.getString(R.string.character_image_height_default_value)
        val iconObject: HashMap<String, String>? = item.Icon
        val imageUrl = iconObject?.get(context.getString(R.string.response_key_icon_url)) ?: ""
        val imageWidth = if (iconObject == null || !iconObject.containsKey(widthKey) ||
            iconObject[widthKey] == null || iconObject[widthKey]?.length == 0
        ) {
            Integer.parseInt(defaultWidthString)
        } else {
            Integer.parseInt(iconObject[widthKey] ?: defaultWidthString)
        }
        val imageHeight = if (iconObject == null || !iconObject.containsKey(heightKey) ||
            iconObject[heightKey] == null || iconObject[heightKey]?.length == 0) {
            Integer.parseInt(defaultHeightString)
        } else {
            Integer.parseInt(iconObject[heightKey] ?: defaultHeightString)
        }
        return CharacterItem(characterName, characterDescription, imageWidth, imageHeight, imageUrl)
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