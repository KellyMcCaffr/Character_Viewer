package com.sample

import android.content.Context
import android.content.res.Configuration
import android.util.Pair
import java.util.HashMap

object Utils {

    fun getScreenIsPortrait(
        context: Context
    ): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    fun getImageWidthHeight(
        iconObject: HashMap<String, String>?,
        context: Context
    ): Pair<Int, Int> {
        val defaultWidthString = context.getString(R.string.character_image_width_default_value)
        val defaultHeightString = context.getString(R.string.character_image_height_default_value)
        val widthKey = context.getString(R.string.response_key_icon_width)
        val heightKey = context.getString(R.string.response_key_icon_height)
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
        return Pair(imageWidth, imageHeight)
    }

}