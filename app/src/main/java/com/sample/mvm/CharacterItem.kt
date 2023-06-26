package com.sample.mvm

import java.io.Serializable

data class CharacterItem (
    val name: String,
    val description: String,
    val imageWidth: Int,
    val imageHeight: Int,
    // For blank value, use default image
    val imageUrl: String?
) : Serializable