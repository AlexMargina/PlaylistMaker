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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.ClickedMusicAdapter
import com.example.playlistmaker.search.SearchMusicAdapter
import com.example.playlistmaker.search.ui.SearchState.History
import com.example.playlistmaker.search.ui.SearchState.Tracks
import com.example.playlistmaker.sharing.domain.Track
import com.example.playlistmaker.sharing.domain.makeArrayList


class SearchActivity : AppCompatActivity() {

    private val viewModel by viewModels<SearchViewModel> {
        SearchViewModel.getViewModelFactory(
            "123"
        )
    }
    private lateinit var binding: ActivitySearchBinding
    private var searchedSong = mutableListOf<Track>() // песни найденные через iTunesApi
    private var clickedTrack = arrayListOf<Track>() // песни сохраненные по клику
    private val searchMusicAdapter = SearchMusicAdapter()
    private val clickedMusicAdapter = ClickedMusicAdapter()
    private var isClickAllowed = true
    private var clearText = false
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private lateinit var onHistoryTrackClickDebounce: (Track) -> Unit
    private lateinit var trackSearchDebounce: (String) -> Unit


    companion object {
        const val SEARCH_STRING = "SEARCH_STRING"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

        const val NOT_FOUND = "Ничего не нашлось"
        const val NO_CONNECTION =
            "Проблемы со связью\n\nЗагрузка не удалась. Проверьте подключение к интернету"
        private const val CLICK_DEBOUNCE_DELAY = 1000L

    }

    /*       Основная функции при создании активити поиска:                                           */
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getLoadingLiveData()  //.observe(this) { //isLoading ->  changeProgressBarVisibility(isLoading)         }

        viewModel.state.observe(this) { state ->
            when (state) {
                SearchState.Empty -> showEmptyResult()
                is SearchState.Error -> showTracksError()
                is History -> {showClickedSong(state.tracks, state.clearText)
                                clickedTrack= state.tracks as ArrayList<Track>
                                clearText = state.clearText}
                SearchState.Loading -> showLoading()
                is Tracks -> {showSearchSong (state.tracks)
                                searchedSong = state.tracks as MutableList<Track>
                    Log.d ("MAALMI", state.tracks[0].trackName)}
            }
            Log.d ("Maalmi", "Изменения статуса во ViewModel ${state.toString()}")
        }


        //нажатие на стрелку НАЗАД
        binding.backOffSearch.setOnClickListener {
            finish()
        }

        // при получении фокуса показать историю просмотренных песен
        binding.inputSearchText.setOnFocusChangeListener { view, hasFocus ->
            viewModel.searchGetFocus(hasFocus, binding.inputSearchText.text.toString())
            showClickedSong(searchedSong, clearText)
        }


        // при нажатии на крестик очистки поля поиска:
        binding.iconClearSearch.setOnClickListener {
            binding.inputSearchText.setText("")
            binding.imageCrash.visibility = View.GONE
            binding.inetProblem.visibility = View.GONE
            viewModel.clearSearchText() //searchedSong.clear()
            binding.recyclerViewSearch.adapter?.notifyDataSetChanged()
            binding.recyclerViewClicked.adapter?.notifyDataSetChanged()
        }


        // Создаем функцию поиска в отдельном потоке с задержкой 2 с
        // БЫЛО   val searchRunnable = Runnable { searchSongByText() }
        fun searchDebounce() {
            val searchRunnable = Runnable { loadTracks() }
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
            binding.inputSearchText.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }


            // изменения текста в поле поиска. Привязка обьекта TextWatcher
        binding.inputSearchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchDebounce()
                binding.iconClearSearch.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                binding.groupClicked.visibility =
                    if (binding.inputSearchText.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
                binding.groupSearched.visibility =
                    if (binding.inputSearchText.hasFocus() && s?.isEmpty() == true) View.GONE else View.VISIBLE
                binding.recyclerViewSearch.visibility =
                    if (binding.inputSearchText.hasFocus() && s?.isEmpty() == true) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })



        // обработка нажатия на кнопку Done
        binding.inputSearchText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchDebounce()
                binding.groupSearched.visibility = View.VISIBLE
            }
            false
        }

        // обработка нажатия на кнопку Обновить
        binding.inetProblem.setOnClickListener {
            searchDebounce()
        }

        // обработка нажатия на кнопку Очистить историю
        binding.clearHistory.setOnClickListener {
            viewModel.clearHistory()
            showClickedSong(clickedTrack, true)
            binding.recyclerViewClicked.adapter?.notifyDataSetChanged()
        }


        // нажатие на найденные песни в Recycler через SearchMusicAdapter
        searchMusicAdapter.itemClickListener =
            { track ->
                if (clickDebounce()) {
                    viewModel.openTrack(track)
                    val openOtherActivity = OpenOtherActivity(this)
                    openOtherActivity.runPlayer(track.trackId)
                }
            }

        // нажатие на сохраненные песни в Recycler через ClickedMusicAdapter
        clickedMusicAdapter.itemClickListener =
            { track ->
                if (clickDebounce()) {
                    viewModel.openHistoryTrack(track)
                    val openOtherActivity = OpenOtherActivity(this)
                    openOtherActivity.runPlayer(track.trackId)
                }
            }

            /*     Формирование списка найденных песен в recyclerViewSearch                 */
            binding.recyclerViewSearch.layoutManager = LinearLayoutManager(this)
            binding.recyclerViewSearch.adapter = SearchMusicAdapter()
            /*     Формирование списка сохраненных (кликнутых) песен в recyclerViewClicked  */
            binding.recyclerViewClicked.layoutManager = LinearLayoutManager(this)
            binding.recyclerViewClicked.adapter = ClickedMusicAdapter()

            // КОНЕЦ  fun onCreate(savedInstanceState: Bundle?)
        }


    // показать песни из истории выбора
    private fun showClickedSong(historySongs: List<Track>, clearText: Boolean) {

            if (clearText) {
                binding.inputSearchText.setText("")
            }
            if (historySongs.size > 0) {
                clickedMusicAdapter.tracks.clear()
                clickedMusicAdapter.tracks.addAll(historySongs)
                clickedMusicAdapter.notifyDataSetChanged()

                binding.groupSearched.visibility =
                    if (binding.inputSearchText.hasFocus() && binding.inputSearchText.text.isEmpty()) View.GONE else View.VISIBLE
                binding.groupClicked.visibility =
                    if (binding.inputSearchText.hasFocus() && binding.inputSearchText.text.isEmpty()) View.VISIBLE else View.GONE
            } else {
                binding.groupSearched.visibility = View.VISIBLE
                binding.groupClicked.visibility = View.GONE
            }
    }

    private fun showSearchSong(tracks: List<Track>) {
        binding.recyclerViewSearch.adapter = SearchMusicAdapter()
        searchedSong = makeArrayList()
        searchMusicAdapter.tracks.clear()
        searchMusicAdapter.tracks.addAll(searchedSong)
        Log.d ("MAALMI", "${searchedSong[0].artistName} перед запуском Рециклера")
                searchMusicAdapter.notifyDataSetChanged()

        binding.recyclerViewSearch.visibility = View.VISIBLE
        binding.groupProgress.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.groupSearched.visibility = View.VISIBLE
        binding.imageCrash.visibility = View.GONE
        binding.inetProblem.visibility = View.GONE
    }


    private fun showTracksError() {
        binding.recyclerViewSearch.visibility = View.GONE
        binding.recyclerViewClicked.visibility = View.GONE
        binding.groupSearched.visibility = View.VISIBLE
        binding.groupClicked.visibility = View.GONE
        binding.groupProgress.visibility= View.GONE
        binding.inetProblem.visibility = View.GONE
        binding.imageCrash.visibility = View.VISIBLE
        binding.updateButton.visibility = View.VISIBLE
        binding.updateButton.setOnClickListener { loadTracks() }
    }

    private fun loadTracks() {
        viewModel.loadTracks(binding.inputSearchText.text.toString())
    }

    private fun showEmptyResult() {
        binding.recyclerViewSearch.visibility = View.GONE
        binding.recyclerViewClicked.visibility = View.GONE
        binding.groupSearched.visibility = View.VISIBLE
        binding.groupClicked.visibility = View.GONE
        binding.groupProgress.visibility= View.GONE
        binding.inetProblem.visibility = View.VISIBLE
        binding.imageCrash.visibility = View.GONE
        binding.updateButton.visibility = View.VISIBLE
        binding.updateButton.setOnClickListener { loadTracks() }
    }

    private fun showLoading() {
        searchMusicAdapter.tracks.clear()
        searchMusicAdapter.notifyDataSetChanged()

        binding.groupSearched.visibility = View.GONE
        binding.groupClicked.visibility = View.GONE
        binding.groupProgress.visibility= View.VISIBLE
        binding.inetProblem.visibility = View.GONE
        binding.imageCrash.visibility = View.GONE
        binding.updateButton.visibility = View.GONE
     }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
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
    }









