package com.sample.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.R
import com.sample.mvm.CharacterItem
import com.sample.mvm.CharactersViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainActivity : AppCompatActivity() {

    val callback: Callback = Callback()
    // Added to prevent multiple clicks launching multiple instances at the same time
    var openedDetailScreenSinceLastResume = false
    lateinit var compositeDisposable: CompositeDisposable
    lateinit var viewModel: CharactersViewModel
    lateinit var progressBar: ProgressBar
    lateinit var searchEditText: EditText
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: CharacterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        compositeDisposable = CompositeDisposable()
        viewModel = CharactersViewModel()
        initViews()
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
        adapter = CharacterAdapter(characterList, callback, ViewUtils.getDeviceIsTablet(this),
        this)
        recyclerView.adapter = adapter
        // Save for restore on rotate
        cachedCharacterList = characterList
    }

    fun onCharacterItemClicked(
        characterItem: CharacterItem
    ) {
        if (!openedDetailScreenSinceLastResume) {
            runOnUiThread {
                openedDetailScreenSinceLastResume = true
                ViewUtils.openCharacterDetailScreen(characterItem, this)
            }
        }
    }

    override fun onResume() {
        openedDetailScreenSinceLastResume = false
        super.onResume()
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
            val activity = (context as MainActivity)
            activity.runOnUiThread {
                activity.displayCharacterData(characterList)
            }
        }

        fun onCharacterItemClickedNonTablet(
            item: CharacterItem,
            context: Context
        ) {
            val activity = (context as MainActivity)
            activity.apply {
                runOnUiThread {
                    activity.onCharacterItemClicked(item)
                }
            }
        }
    }
}