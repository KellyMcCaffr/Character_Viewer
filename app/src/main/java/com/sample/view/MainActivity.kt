package com.sample.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
    private var openedDetailScreenSinceLastResume = false
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
            createLoadCharactersRequest()
        } else {
            displayCharacterData(list)
        }
    }

    private fun initViews() {
        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.characterRecyclerView)
        searchEditText = findViewById(R.id.searchEditText)
        val context = this
        searchEditText.addTextChangedListener(object: TextWatcher {
            @Override
            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }

            @Override
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // TODO Auto-generated method stub
            }

            @Override
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.e("435433","Text has changed to: " + s.toString())
                if (context::adapter.isInitialized) {
                    refreshCharacterDataByFilter(s.toString())
                }
            }
        })
    }

    private fun createLoadCharactersRequest() {
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

    fun refreshCharacterDataByFilter(
        filterText: String
    ) {
        val newList = ViewUtils.filterCharacterListByText(filterText, adapter.characterInfoListRaw)
        adapter.filterCharacterList(newList)
    }

    fun onCharacterItemClicked(
        characterItem: CharacterItem
    ) {
        if (!openedDetailScreenSinceLastResume) {
            runOnUiThread {
                openedDetailScreenSinceLastResume = true
                Log.e("5656346","Opening character details 23")
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