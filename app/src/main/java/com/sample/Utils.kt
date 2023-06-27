package com.sample

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.util.Pair
import com.sample.mvm.CharacterItem
import com.sample.view.DetailActivity
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.HashMap

object Utils {

    fun openCharacterDetailScreen(
        item: CharacterItem,
        context: Context
    ) {
        val intent = Intent(context, DetailActivity::class.java)
        val itemKey = context.getString(R.string.character_item_extra_key)
        intent.putExtra(itemKey, item)
        context.startActivity(intent)
    }

    fun getDeviceIsTablet(
        context: Context
    ): Boolean {
        // Code copied from https://www.geeksforgeeks.org/how-to-detect-tablet-or-phone-in-android-programmatically/
        return (context.resources.configuration.screenLayout and
            Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE)
                && getDevice5Inch(context)

    }

    private fun getDevice5Inch(context: Context): Boolean {
        // Code copied from https://www.geeksforgeeks.org/how-to-detect-tablet-or-phone-in-android-programmatically/
        return try {
            val displayMetrics = context.resources.displayMetrics
            val yinch = displayMetrics.heightPixels / displayMetrics.ydpi
            val xinch = displayMetrics.widthPixels / displayMetrics.xdpi
            val diagonalinch = Math.sqrt((xinch * xinch + yinch * yinch).toDouble())
            diagonalinch >= Constants.TABLET_CHECK_DIAGONAL
        } catch (e: Exception) {
            false
        }
    }

    fun getBitmapFromIconObject(
        iconObject: HashMap<String, String>?,
        firstUrl: String,
        imageWidth: Int,
        imageHeight:Int,
        context: Context
    ): Bitmap {
        val imageUrl = iconObject?.get(context.getString(R.string.response_key_icon_url)) ?: ""
        var bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.default_character_image)
        var finalWidth = imageWidth
        var finalHeight = imageHeight
        if (imageUrl.isNotEmpty()) {
            try {
                val connection: HttpURLConnection =
                    URL(firstUrl + imageUrl).openConnection() as HttpURLConnection
                connection.connect()
                val input: InputStream = connection.inputStream
                val unscaledBitmap = BitmapFactory.decodeStream(input)
                if (imageWidth > Constants.MAX_CHARACTER_IMAGE_WIDTH) {
                    finalWidth = Constants.MAX_CHARACTER_IMAGE_WIDTH
                }
                if (imageHeight > Constants.MAX_CHARACTER_IMAGE_HEIGHT) {
                    finalHeight = Constants.MAX_CHARACTER_IMAGE_HEIGHT
                }
                if (unscaledBitmap != null) {
                    bitmap = Bitmap.createScaledBitmap(
                        unscaledBitmap,
                        finalWidth,
                        finalHeight,
                        true
                    )
                }
            } catch (e: MalformedURLException) {
                Log.e("54453","Malformed url exception occurred for: " + imageUrl)
            }
        }
        return bitmap
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