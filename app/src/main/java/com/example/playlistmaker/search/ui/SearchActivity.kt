package com.example.playlistmaker.search.ui

//
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.TrackSearchModel


class SearchActivity : AppCompatActivity() {

        private val searchedSong = ArrayList<TrackSearchModel>()
        private val clickedSong = ArrayList<TrackSearchModel>()
        private val searchMusicAdapter = SearchMusicAdapter(searchedSong) { trackClickListener(it) }
        private val clickedMusicAdapter = SearchMusicAdapter(clickedSong) { trackClickListener(it) }
        private val handler = Handler(Looper.getMainLooper())
        private var searchText = ""
        private var clickAllowed = true
        private lateinit var binding: ActivitySearchBinding
        private lateinit var viewModel: SearchViewModel

        companion object {
            private const val SEARCH_STRING = "SEARCH_STRING"
            private const val SEARCH_DEBOUNCE_DELAY = 1000L
        }

        /*       Основная функции при создании активити поиска:                                           */
        @SuppressLint("MissingInflatedId")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            Log.d("Maalmi", "onCreate")

            binding = ActivitySearchBinding.inflate(layoutInflater)
            setContentView(binding.root)

             viewModel = ViewModelProvider(
                this, SearchViewModel.getViewModelFactory()
            )[SearchViewModel::class.java]

            viewModel.stateLiveData().observe(this) {
                updateScreen(it)
                Log.d("Maalmi", "Изменения статуса во ViewModel ${this.toString()}")
            }


            //нажатие на стрелку НАЗАД
            binding.backOffSearch.setOnClickListener {
                finish()
            }

            // обработка нажатия на кнопку Done
            binding.inputSearchText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.searchDebounce(searchText, true)
                    binding.groupSearched.visibility = View.VISIBLE
                }
                false
            }

            // обработка нажатия на кнопку Обновить
            binding.inetProblem.setOnClickListener {
                viewModel.searchDebounce(searchText, true)
            }

            // обработка нажатия на кнопку Очистить историю
            binding.clearHistory.setOnClickListener {
                viewModel.clearHistory()
                viewModel.getTracksHistory()
                binding.groupClicked.visibility = View.GONE
                binding.recyclerViewClicked.adapter?.notifyDataSetChanged()
            }


            // при нажатии на крестик очистки поля поиска:
            binding.apply {
                iconClearSearch.setOnClickListener {
                searchedSong.clear()
                viewModel.getTracksHistory()
                searchMusicAdapter.notifyDataSetChanged()
                recyclerViewSearch.adapter?.notifyDataSetChanged()
                recyclerViewClicked.adapter?.notifyDataSetChanged()
                inputSearchText.setText("")
                imageCrash.visibility = View.GONE
                inetProblem.visibility = View.GONE
                iconClearSearch.visibility=View.GONE
                groupClicked.visibility=View.VISIBLE
                recyclerViewClicked.visibility = View.VISIBLE
                }
            }


            //     Формирование списка найденных песен в recyclerViewSearch                 *//
            binding.recyclerViewSearch.layoutManager = LinearLayoutManager(this)
            binding.recyclerViewSearch.adapter = searchMusicAdapter //SearchMusicAdapter()
            //     Формирование списка сохраненных (кликнутых) песен в recyclerViewClicked  *//
            binding.recyclerViewClicked.layoutManager = LinearLayoutManager(this)
            binding.recyclerViewClicked.adapter = clickedMusicAdapter //ClickedMusicAdapter()


            // изменения текста в поле поиска. Привязка обьекта TextWatcher
            fun textWatcher() = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    binding.iconClearSearch.visibility = View.GONE
                    if (s.isNullOrEmpty()) {
                        binding.iconClearSearch.visibility = View.GONE
                    } else {
                        binding.iconClearSearch.visibility = View.VISIBLE
                    }
                    binding.groupClicked.visibility = View.GONE
                    searchText = s.toString()
                    viewModel.searchDebounce(searchText, false)
                }

                override fun afterTextChanged(s: Editable?) {}
            }

            provideTextWatcher(textWatcher())

        }               // КОНЕЦ  fun onCreate(savedInstanceState: Bundle?)


        fun provideTextWatcher(textWatcher: TextWatcher) {
            binding.inputSearchText.apply {
                addTextChangedListener(textWatcher)
                setText(searchText)
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus && this.text.isEmpty())
                        viewModel.getTracksHistory()
                    else
                        binding.groupClicked.visibility = View.GONE
                }
            }
        }


        // запоминание текста поисковой строки inputSearchText в переменную
        @SuppressLint("SuspiciousIndentation")
        override fun onSaveInstanceState(outState: Bundle) {
            val inputSearchText = findViewById<EditText>(R.id.inputSearchText)
            outState.putString(SEARCH_STRING, inputSearchText.text.toString())
            super.onSaveInstanceState(outState)
        }

        //заполнение тектового поля из предыдущего запуска Активити
        @SuppressLint("SuspiciousIndentation")
        override fun onRestoreInstanceState(savedInstanceState: Bundle) {
            val inputSearchText = findViewById<EditText>(R.id.inputSearchText)
            if (savedInstanceState.containsKey(SEARCH_STRING)) {
                val searchText = savedInstanceState.getString(SEARCH_STRING)
                inputSearchText.setText(searchText)
            }
        }

        private fun trackClickListener(track: TrackSearchModel) {
            if (isClickAllowed()) {
                viewModel.addTrackToHistory(track)
                val openOtherActivity = OpenOtherActivity(this)
                openOtherActivity.runPlayer(track.trackId.toString())
            }
        }

        private fun isClickAllowed(): Boolean {
            val current = clickAllowed
            if (clickAllowed) {
                clickAllowed = false
                handler.postDelayed({ clickAllowed = true }, SEARCH_DEBOUNCE_DELAY)
            }
            return current
        }

        private fun updateScreen(state: SearchState) {
            binding.apply {
                when (state) {
                    is SearchState.Content -> {
                        searchedSong.clear()
                        searchedSong.addAll(state.tracks as ArrayList<TrackSearchModel>)
                        groupClicked.visibility = View.GONE
                        groupProgress.visibility = View.GONE
                        groupSearched.visibility = View.VISIBLE
                        searchMusicAdapter.notifyDataSetChanged()
                    }

                    is SearchState.Error -> {
                        groupProgress.visibility = View.GONE
                        groupSearched.visibility = View.GONE
                        binding.imageCrash.visibility = View.GONE
                        binding.inetProblem.visibility = View.VISIBLE
                    }

                    is SearchState.Empty -> {
                        groupProgress.visibility = View.GONE
                        inetProblem.visibility = View.GONE
                        imageCrash.visibility = View.VISIBLE
                        updateButton.visibility = View.VISIBLE
                    }

                    is SearchState.Loading -> {
                        groupClicked.visibility=View.GONE
                        groupSearched.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        groupProgress.visibility = View.VISIBLE

                    }

                    is SearchState.ContentHistoryList -> {
                        groupClicked.visibility = View.VISIBLE
                        groupProgress.visibility = View.GONE
                        groupSearched.visibility = View.GONE
                        recyclerViewClicked.visibility = View.VISIBLE
                        clickedSong.clear()
                        clickedSong.addAll(state.historyList)
                        clickedMusicAdapter.notifyDataSetChanged()
                    }

                    is SearchState.EmptyHistoryList -> {
                        groupClicked.visibility = View.GONE
                    }
                }
            }
        }
    }

















