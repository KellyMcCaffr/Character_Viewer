package com.sample

import android.content.Context
import android.content.res.Configuration

class Utils {

    fun getScreenIsPortrait(
        context: Context
    ): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

}