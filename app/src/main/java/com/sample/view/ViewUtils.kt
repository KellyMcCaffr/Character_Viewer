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
import java.util.ArrayList

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
            diagonalinch >= Constants.TABLET_CHECK_DIAGONAL_NUM_INCHES
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
            setImageWidthHeight(imageWidth, imageHeight, imageView)
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
        imageView: View
    ) {
        val params = imageView.layoutParams
        params.width =  imageWidth
        params.height = imageHeight
        imageView.layoutParams = params
        imageView.visibility = View.VISIBLE
    }

    fun getImageWidthHeightFromIconObject(
        iconObject: HashMap<String, String>?,
        context: Context
    ): Pair<Int, Int> {
        val defaultWidth = (context.resources.getDimension(R.dimen.character_image_width_default_dp) / context.resources.displayMetrics.density).toInt()
        val defaultHeight =  (context.resources.getDimension(R.dimen.character_image_height_default_dp) / context.resources.displayMetrics.density).toInt()
        val widthKey = context.getString(R.string.response_key_icon_width)
        val heightKey = context.getString(R.string.response_key_icon_height)
        var imageWidth = if (iconObject == null || !iconObject.containsKey(widthKey) ||
            iconObject[widthKey] == null || iconObject[widthKey]?.length == 0
        ) {
            defaultWidth
        } else {
            Integer.parseInt(iconObject[widthKey] ?: defaultWidth.toString())
        }
        var imageHeight = if (iconObject == null || !iconObject.containsKey(heightKey) ||
            iconObject[heightKey] == null || iconObject[heightKey]?.length == 0
        ) {
            defaultHeight
        } else {
            Integer.parseInt(iconObject[heightKey] ?: defaultHeight.toString())
        }
        if (imageWidth > Constants.MAX_CHARACTER_IMAGE_WIDTH) {
            imageWidth = Constants.MAX_CHARACTER_IMAGE_WIDTH
        }
        if (imageHeight > Constants.MAX_CHARACTER_IMAGE_HEIGHT) {
            imageHeight = Constants.MAX_CHARACTER_IMAGE_HEIGHT
        }
        return Pair(imageWidth, imageHeight)
    }

    // Returns a list with character items whose names or descriptions contain the text
    fun filterCharacterListByText(
        searchText: String,
        unfilteredList: List<CharacterItem>
    ): List<CharacterItem> {
        val filteredList: ArrayList<CharacterItem> = arrayListOf()
        filteredList.addAll(unfilteredList.filter {it.name.contains(searchText)
            || it.description.contains(searchText)})
        return filteredList
    }
}