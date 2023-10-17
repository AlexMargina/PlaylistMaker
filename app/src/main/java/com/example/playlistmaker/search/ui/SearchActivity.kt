package com.example.playlistmaker.search.ui

//
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.SearchState
import com.example.playlistmaker.search.domain.TrackModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchActivity : AppCompatActivity() {

        private val searchedSong = ArrayList<TrackModel>()
        private val clickedSong = ArrayList<TrackModel>()
        private val searchMusicAdapter = SearchMusicAdapter(searchedSong) { trackClickListener(it) }
        private val clickedMusicAdapter = SearchMusicAdapter(clickedSong) { trackClickListener(it) }
        private val handler = Handler(Looper.getMainLooper())
        private var searchText = ""
        private var clickAllowed = true
        private lateinit var binding: ActivitySearchBinding
        //private lateinit var viewModel: SearchViewModel
        private val viewModel by viewModel<SearchViewModel>()

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

//             viewModel = ViewModelProvider(
//                this, SearchViewModel.getViewModelFactory()
//            )[SearchViewModel::class.java]

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
                    binding.groupSearched.isVisible = true
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
                binding.groupClicked.isVisible = false
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
                imageCrash.isVisible = false
                inetProblem.isVisible = false
                iconClearSearch.isVisible = false
                groupClicked.isVisible = true
                recyclerViewClicked.isVisible = true
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
                    binding.iconClearSearch.isVisible = false
                    if (s.isNullOrEmpty()) {
                        binding.iconClearSearch.isVisible = false
                    } else {
                        binding.iconClearSearch.isVisible = true
                    }
                    binding.groupClicked.isVisible = false
                    searchText = s.toString()
                    viewModel.searchDebounce(searchText, false)
                }

                override fun afterTextChanged(s: Editable?) {}
            }

            provideTextWatcher(textWatcher())

        }        // КОНЕЦ  fun onCreate(savedInstanceState: Bundle?)


        fun provideTextWatcher(textWatcher: TextWatcher) {
            binding.inputSearchText.apply {
                addTextChangedListener(textWatcher)
                setText(searchText)
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus && this.text.isEmpty())
                        viewModel.getTracksHistory()
                    else
                        binding.groupClicked.isVisible = false
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
            if (savedInstanceState.containsKey(SEARCH_STRING)) {
                val searchText = savedInstanceState.getString(SEARCH_STRING)
                binding.inputSearchText.setText(searchText)
            }
        }

        private fun trackClickListener(track: TrackModel) {
            if (isClickAllowed()) {
                viewModel.addTrackToHistory(track, this)
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

                Log.d ("MAALMI", state.toString())
                when (state) {
                    is SearchState.Content -> {
                        Log.d ("MAALMI", "Выполняем Content")
                        searchedSong.clear()
                        searchedSong.addAll(state.tracks as ArrayList<TrackModel>)
                        groupClicked.isVisible = false
                        groupProgress.isVisible = false
                        groupSearched.isVisible = true
                        recyclerViewSearch.isVisible = true
                        imageCrash.isVisible = false
                        inetProblem.isVisible = false
                        searchMusicAdapter.notifyDataSetChanged()
                    }

                    is SearchState.Error -> {
                        Log.d ("MAALMI", "Выполняем Error")
                        groupProgress.isVisible = false
                        groupSearched.isVisible = true
                        recyclerViewSearch.isVisible = false
                        imageCrash.isVisible = false
                        inetProblem.isVisible = true
                    }

                    is SearchState.Empty -> {
                        Log.d ("MAALMI", "Выполняем Empty")
                        groupProgress.isVisible = false
                        groupSearched.isVisible = true
                        recyclerViewSearch.isVisible = false
                        inetProblem.isVisible = false
                        imageCrash.isVisible = true
                        updateButton.isVisible = false
                    }

                    is SearchState.Loading -> {
                        Log.d ("MAALMI", "Выполняем Loading")
                        groupClicked.isVisible = false
                        groupSearched.isVisible = false
                        progressBar.isVisible = true
                        groupProgress.isVisible = true

                    }

                    is SearchState.ContentHistoryList -> {
                        Log.d ("MAALMI", "Выполняем ContentHistoryList")
                        groupClicked.isVisible = true
                        groupProgress.isVisible = false
                        groupSearched.isVisible = false
                        recyclerViewClicked.isVisible = true
                        clickedSong.clear()
                        clickedSong.addAll(state.historyList)
                        clickedMusicAdapter.notifyDataSetChanged()
                    }

                    is SearchState.EmptyHistoryList -> {
                        Log.d ("MAALMI", "Выполняем EmptyHistoryList")
                        groupClicked.isVisible = false
                    }
                }
            }
        }
    }

















