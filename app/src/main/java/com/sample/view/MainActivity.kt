package com.sample.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.DaggerComponent
import com.sample.R
import com.sample.mvm.CharacterItem
import com.sample.mvm.CharactersViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: CharactersViewModel

    private val callback: Callback = Callback()
    private val compositeDisposable = CompositeDisposable()

    lateinit var adapter: CharacterAdapter
    lateinit var progressBar: ProgressBar
    lateinit var searchEditText: EditText
    lateinit var recyclerView: RecyclerView

    // Added to prevent multiple clicks launching multiple instances at the same time
    private var openedDetailScreenSinceLastResume = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DaggerComponent.builder().build().injectMainActivity(this)
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
            override fun afterTextChanged(s: Editable) {}

            @Override
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            @Override
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (context::adapter.isInitialized) {
                    refreshCharacterDataByFilter(s.toString())
                }
            }
        })
    }

    private fun createLoadCharactersRequest() {
        progressBar.visibility = View.VISIBLE
        compositeDisposable.add(
            viewModel.loadCharacterData(this)
                .subscribe(
                    {
                        runOnUiThread {
                            displayCharacterData(it)
                        }
                    },
                    {
                        runOnUiThread {
                            onCharacterListLoadError()
                        }
                    }
                )
        )
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
        adapter = CharacterAdapter(characterList, callback,
            ViewUtils.getDeviceIsTablet(this), this)
        recyclerView.adapter = adapter
        // Save for restore on rotate
        cachedCharacterList = characterList
    }

    fun onCharacterListLoadError() {
        if (progressBar != null) {
            progressBar.visibility = View.GONE
        }
    }

    fun refreshCharacterDataByFilter(
        filterText: String
    ) {
        val newList = ViewUtils.filterCharacterListByText(filterText, adapter.characterInfoListRaw)
        adapter.setFilteredCharacterList(newList)
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