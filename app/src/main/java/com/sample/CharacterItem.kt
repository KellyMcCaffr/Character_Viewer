package com.sample

import android.graphics.drawable.Drawable

data class CharacterItem internal constructor(
    val name: String,
    val image: Drawable,
    val description: String
)