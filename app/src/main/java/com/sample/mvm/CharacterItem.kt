package com.sample.mvm

data class CharacterItem internal constructor(
    val name: String,
    val description: String,
    val imageWidth: Int,
    val imageHeight: Int,
    // For blank value, use default image
    val imageUrl: String?
)