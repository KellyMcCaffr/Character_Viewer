package com.sample.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.sample.R
import com.sample.mvm.CharacterItem
import java.util.Arrays
import java.util.Calendar

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
        ViewUtils.displayCharacterImageFromUrlAndAdjustBounds(detailItem.imageWidth, detailItem.imageHeight,
            detailItem.imageUrl, characterImageView, this)
        setClickBackPress()
    }

    private fun setClickBackPress() {
        val descriptionTextView = findViewById<View>(R.id.descriptionTextView)
        val cardView = findViewById<CardView>(R.id.outerCardView)
        val viewList = Arrays.asList(cardView, descriptionTextView)
        for (view in viewList) {
            view.setOnClickListener {
                super.onBackPressed()
            }
            var timePressStart = -1L
            view.setOnTouchListener(
                View.OnTouchListener { view, motionEvent ->
                    when (motionEvent.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if (timePressStart == -1L) {
                                timePressStart = Calendar.getInstance().timeInMillis
                            }
                            cardView.alpha = 0.6f
                        }
                        MotionEvent.ACTION_UP -> {
                            cardView.alpha = 1f
                            if ((Calendar.getInstance().timeInMillis - timePressStart) < 500) {
                                view.performClick()
                            }
                            timePressStart = -1L
                        }
                        MotionEvent.ACTION_CANCEL -> {
                            cardView.alpha = 1f
                            timePressStart = -1L
                        }
                    }
                    return@OnTouchListener true
                }
            )
        }
    }
}