package com.sample.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.sample.R
import com.sample.mvm.CharacterItem

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val detailItem = intent?.extras?.getSerializable(getString(R.string.character_item_extra_key)) as CharacterItem
        setViews(detailItem)
    }

    private fun setViews(
        detailItem: CharacterItem
    ) {
        val possibleImageWrapper = findViewById<RelativeLayout>(R.id.possibleCharacterImageWrapper)
        val imageView: ImageView = findViewById(R.id.characterImageView)
        val titleView: TextView = findViewById(R.id.nameTextView)
        val descriptionView: TextView = findViewById(R.id.descriptionTextView)
        val characterImageView = findViewById<ImageView>(R.id.characterImageView)
        titleView.apply {
            text = detailItem.name
            visibility = View.VISIBLE
        }
        descriptionView.apply {
            text = detailItem.description
            visibility = View.VISIBLE
        }
        possibleImageWrapper.visibility = View.VISIBLE
        imageView.visibility = View.VISIBLE
        ViewUtils.displayCharacterImageFromUrlAndUpdateBounds(detailItem.imageWidth, detailItem.imageHeight,
            detailItem.imageUrl, characterImageView, this)
    }
}