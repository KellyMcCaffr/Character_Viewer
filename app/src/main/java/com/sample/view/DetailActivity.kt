package com.sample.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.sample.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val detailItem = intent?.extras?.getSerializable(getString(R.string.character_item_extra_key))
        Log.e("54445","Here is character detail item retrieved: " + detailItem)
    }
}