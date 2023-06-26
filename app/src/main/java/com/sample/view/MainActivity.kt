package com.sample.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
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
    lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        compositeDisposable = CompositeDisposable()
        viewModel = CharactersViewModel()
        initViews()
        loadCharacterData()
    }

    private fun initViews() {
        progressBar = findViewById(R.id.progressBar)
        searchEditText = findViewById(R.id.searchEditText)
    }

    private fun loadCharacterData() {
        progressBar.visibility = View.VISIBLE
        viewModel.loadCharacterData(Callback(), compositeDisposable, this)
    }

    fun displayCharacterData(
        characterList: List<CharacterItem>
    ) {
        searchEditText.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
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