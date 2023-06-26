package com.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainActivity : AppCompatActivity() {
    lateinit var compositeDisposable: CompositeDisposable
    lateinit var viewModel: CharactersViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        compositeDisposable = CompositeDisposable()
        viewModel = CharactersViewModel()
        viewModel.loadCharacterData(Callback(), compositeDisposable, this)
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    class Callback {
        fun onCharacterListLoaded(
            responseList: List<CharacterItem>
        ) {
            Log.e("435534","Here is character list passed to main activity: " + responseList)
        }
    }
}