package com.sample.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.sample.R
import com.sample.mvm.CharacterItem
import com.sample.mvm.CharactersViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainActivity : AppCompatActivity() {
    lateinit var compositeDisposable: CompositeDisposable
    lateinit var viewModel: CharactersViewModel
    lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        compositeDisposable = CompositeDisposable()
        viewModel = CharactersViewModel()
        progressBar = findViewById(R.id.progressBar)
        loadCharacterData()
    }

    private fun loadCharacterData() {
        progressBar.visibility = View.VISIBLE
        viewModel.loadCharacterData(Callback(), compositeDisposable, this)
    }

    fun displayCharacterData(
        characterList: List<CharacterItem>
    ) {
        if (progressBar != null) {
            progressBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    class Callback {
        fun onCharacterListLoaded(
            characterList: List<CharacterItem>,
            context: Context
        ) {
            Log.e("435534","Here is character list length: " + characterList.size)
            for (item in characterList) {
                Log.e("45534435","Here is final character item: " + item)
            }
            (context as MainActivity).apply {
                runOnUiThread {
                    displayCharacterData(characterList)
                }
            }
        }
    }
}