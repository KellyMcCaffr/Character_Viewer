package com.sample.mvm

import android.content.Context
import com.google.gson.*
import com.sample.R
import com.sample.view.ViewUtils
import com.sample.view.MainActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.ArrayList
import javax.inject.Inject

class CharactersViewModel @Inject constructor() {

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
                        callback.onCharacterListLoadError(context)
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
        }
        catch (e: JsonParseException) { }
        catch (e: StringIndexOutOfBoundsException) { }
        callback.onCharacterListLoaded(result, context)
    }

    private fun convertResponseItem(
        item: ResponseItem,
        context: Context
    ): CharacterItem {
        val text = item.Text ?: ""
        val characterDescription = text.subSequence(text.indexOf('-') + 2, text.length).toString()
        val imageWidthHeight = ViewUtils.getImageWidthHeightFromIconObject(item.Icon, context)
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
}