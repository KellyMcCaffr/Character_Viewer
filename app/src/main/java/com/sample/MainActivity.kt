package com.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    lateinit var disposable: CompositeDisposable
    lateinit var viewModel: CharactersViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //FetchRequest.fetchCountries(BuildConfig.data_api, this)
        disposable = CompositeDisposable()
        viewModel = CharactersViewModel()
        requestCharacterList()
    }

    private fun requestCharacterList() {
        disposable.add(viewModel.loadCharacterData()
            .subscribe(
                {
                    Log.e("343534","Success: here is character item list: " + it)
                },
                {
                    Log.e("343534","Failed to retrieve character data with error: " + it)
                }
            )
        )
        /*FetchRequest.fetchCharacters(BuildConfig.DATA_API_URL, this)
            .subscribeOn(Schedulers.newThread())
            .subscribe (
                { charactersList ->
                    Log.e("454545","Success")
                    Log.e("454545","Here is list passed: " + charactersList)
                },
                {
                    Log.e("454545","RX call failed with throwable: " + it)
                }

            )*/
    }

    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }
}