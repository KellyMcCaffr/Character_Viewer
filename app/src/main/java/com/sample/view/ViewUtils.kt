package com.sample.view

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.Pair
import android.view.View
import android.widget.ImageView
import com.sample.Constants
import com.sample.R
import com.sample.mvm.CharacterItem
import com.squareup.picasso.Picasso
import java.util.HashMap

object ViewUtils {

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

    fun displayCharacterImageFromUrlAndAdjustBounds(
        imageWidth: Int,
        imageHeight: Int,
        imageUrl: String?,
        imageView: ImageView,
        context: Context?
    ) {
        if (context != null) {
            setImageWidthHeight(imageWidth, imageHeight, imageView, context)
            if (imageUrl!= null && imageUrl.isNotEmpty()) {
                val fullURL = context.getString(R.string.url_search_prefix) + imageUrl
                Picasso.get().load(fullURL).into(imageView)
            } else {
                Picasso.get()
                    .load(R.drawable.default_character_image)
                    .into(imageView)
            }
        }
    }

    private fun setImageWidthHeight(
        imageWidth: Int,
        imageHeight: Int,
        imageView: View,
        context: Context
    ) {
        val params = imageView.layoutParams
        params.width = if (imageWidth <= Constants.MAX_CHARACTER_IMAGE_WIDTH) {
            imageWidth
        } else {
            Integer.parseInt(context.getString(R.string.character_image_width_default_value))
        }
        params.height = if (imageWidth <= Constants.MAX_CHARACTER_IMAGE_HEIGHT) {
            imageHeight
        } else {
            Integer.parseInt(context.getString(R.string.character_image_height_default_value))
        }
        imageView.layoutParams = params
        imageView.visibility = View.VISIBLE
    }

    fun getImageWidthHeightFromIconObject(
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