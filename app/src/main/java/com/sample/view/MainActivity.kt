package com.sample.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.R
import com.sample.Utils
import com.sample.mvm.CharacterItem
import com.sample.mvm.CharactersViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainActivity : AppCompatActivity() {

    lateinit var compositeDisposable: CompositeDisposable
    lateinit var viewModel: CharactersViewModel
    lateinit var progressBar: ProgressBar
    lateinit var searchEditText: EditText
    lateinit var recyclerView: RecyclerView
    val callback: Callback = Callback()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        compositeDisposable = CompositeDisposable()
        viewModel = CharactersViewModel()
        initViews()
        Log.e("453345","Is device tablet?: " + Utils.getDeviceIsTablet(this))
        val list = cachedCharacterList
        if (list == null || list.isEmpty()) {
            loadCharacterData()
        } else {
            displayCharacterData(list)
        }
    }

    private fun initViews() {
        progressBar = findViewById(R.id.progressBar)
        searchEditText = findViewById(R.id.searchEditText)
        recyclerView = findViewById(R.id.characterRecyclerView)
    }

    private fun loadCharacterData() {
        progressBar.visibility = View.VISIBLE
        viewModel.loadCharacterData(callback, compositeDisposable, this)
    }

    fun displayCharacterData(
        characterList: List<CharacterItem>
    ) {
        searchEditText.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        val recyclerViewLayoutManager = LinearLayoutManager(this@MainActivity,
            LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = recyclerViewLayoutManager
        recyclerView.adapter = CharacterAdapter(characterList, callback, this)
        // Save for restore on rotate
        cachedCharacterList = characterList
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    companion object {
        var cachedCharacterList: List<CharacterItem>? = null
    }

    class Callback {

        fun onCharacterListLoaded(
            characterList: List<CharacterItem>,
            context: Context
        ) {
            (context as MainActivity).apply {
                runOnUiThread {
                    displayCharacterData(characterList)
                }
            }
        }

        fun onCharacterItemClicked(
            item: CharacterItem,
            context: Context
        ) {
            (context as MainActivity).apply {
                runOnUiThread {
                    val isTablet = Utils.getDeviceIsTablet(context)
                    if (!isTablet) {
                        Utils.openCharacterDetailScreen(context)
                    }
                }
            }
        }
    }
}