package com.example.playlistmaker.presentation

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.App
import com.example.playlistmaker.domain.CLICKED_SEARCH_TRACK
import com.example.playlistmaker.domain.MUSIC_MAKER_PREFERENCES
import com.example.playlistmaker.R
import com.example.playlistmaker.data.SharedPrefsUtils
import com.example.playlistmaker.data.ITunesResponse
import com.example.playlistmaker.data.ITunesSearchApi
import com.example.playlistmaker.domain.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity(), SearchMusicAdapter.Listener {

    companion object {
        const val SEARCH_STRING = "SEARCH_STRING"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }



    private val searchSongs = mutableListOf<Track>() // песни найденные через iTunesApi
    private var clickedSearchSongs = arrayListOf<Track>() // песни сохраненные по клику
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesSearchApi::class.java)



    /*       Основная функции при создании активити поиска:                                           */
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Элементы экрана:
        val backOffImage = findViewById<ImageView>(R.id.back_off_search)  //стрелка НАЗАД
        val clearButton = findViewById<ImageView>(R.id.icon_clear_search)  // крестик очистки EditText
        val inputSearchText = findViewById<EditText>(R.id.inputSearchText)  // EditText поиска песен
        val recyclerViewSearch = findViewById<RecyclerView>(R.id.recyclerViewSearch) //найденные песни
        val noSongImage = findViewById<TextView>(R.id.image_crash)  // показ отсутствия песен
        val inetProblemImage =
            findViewById<TextView>(R.id.inet_problem)   // ImageView показа отсутствия интернета
        val groupClicked =
            findViewById<LinearLayout>(R.id.group_clicked)  // контейнер с сохраненными трэками
        val recyclerViewClicked =
            findViewById<RecyclerView>(R.id.recyclerViewClicked)   // Recycler сохраненных песен
        val groupSearched =
            findViewById<FrameLayout>(R.id.group_searched)     // контейнер с найденными трэками
        val clearHistory = findViewById<Button>(R.id.clear_history)  // кнопка Очистить историю
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val groupProgress = findViewById<FrameLayout>(R.id.group_progress)

        val sharedPrefsApp = getSharedPreferences(MUSIC_MAKER_PREFERENCES, Application.MODE_PRIVATE)
        val sharedPrefsUtils = SharedPrefsUtils(sharedPrefsApp)
        clickedSearchSongs = sharedPrefsUtils.readClickedSearchSongs(CLICKED_SEARCH_TRACK)

        // Функция выполнения ПОИСКОВОГО ЗАПРОСА
        fun searchSongByText() {

            groupProgress.visibility = View.VISIBLE
            groupClicked.visibility = View.GONE
            groupSearched.visibility = View.GONE
            iTunesService.searchSongApi(inputSearchText.text.toString()).enqueue(object :
                Callback<ITunesResponse> {

                override fun onResponse(
                    call: Call<ITunesResponse>,
                    response: Response<ITunesResponse>
                ) {
                    groupProgress.visibility = View.GONE
                    groupSearched.visibility = View.VISIBLE

                    searchSongs.clear()
                    if (response.code() == 200) {
                        recyclerViewSearch.adapter?.notifyDataSetChanged()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            searchSongs.addAll(response.body()?.results!!)
                            noSongImage.visibility = View.GONE
                            inetProblemImage.visibility = View.GONE
                        } else {
                            inetProblemImage.visibility = View.GONE
                            noSongImage.visibility = View.VISIBLE
                        }

                    } else {
                        noSongImage.visibility = View.GONE
                        inetProblemImage.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                    searchSongs.clear()
                    groupProgress.visibility = View.GONE
                    groupSearched.visibility = View.VISIBLE
                    noSongImage.visibility = View.GONE
                    inetProblemImage.visibility = View.VISIBLE
                }
            })
        }


        fun showGroupClickedSong() {
            if (clickedSearchSongs.size > 0) {
                groupSearched.visibility =
                    if (inputSearchText.hasFocus() && inputSearchText.text.isEmpty()) View.GONE else View.VISIBLE
                groupClicked.visibility =
                    if (inputSearchText.hasFocus() && inputSearchText.text.isEmpty()) View.VISIBLE else View.GONE
            } else {
                groupSearched.visibility = View.VISIBLE
                groupClicked.visibility = View.GONE
            }
        }

        //нажатие на стрелку НАЗАД
        backOffImage.setOnClickListener {
            finish()
        }

        // при нажатии на крестик очистки поля поиска:
        clearButton.setOnClickListener {
            inputSearchText.setText("")
            noSongImage.visibility = View.GONE
            inetProblemImage.visibility = View.GONE
            searchSongs.clear()
            recyclerViewSearch.adapter?.notifyDataSetChanged()
            recyclerViewClicked.adapter?.notifyDataSetChanged()
        }

        // Создаем функцию поиска в отдельном потоке с задержкой 2 с
         val searchRunnable = Runnable { searchSongByText() }
         fun searchDebounce() {
             inputSearchText.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }

        // Привязка обьекта TextWatcher
        inputSearchText.addTextChangedListener(object : TextWatcher {

            // если будут изменения текста в поле поиска, то крестик очистки появится, при удалении - станет невидимым
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isEmpty()) {
                    clearButton.visibility = View.GONE
                } else {
                    clearButton.visibility = View.VISIBLE
                    searchDebounce()
                }
                showGroupClickedSong()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {}
        })

        // при получении фокуса показать историю просмотренных песен
        inputSearchText.setOnFocusChangeListener { view, hasFocus -> showGroupClickedSong() }

        // обработка нажатия на кнопку Done
        inputSearchText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchSongByText()
                groupSearched.visibility = View.VISIBLE
            }
            false
        }

        // обработка нажатия на кнопку Обновить
        inetProblemImage.setOnClickListener {
            searchSongByText()
        }

        // обработка нажатия на кнопку Очистить историю
        clearHistory.setOnClickListener {
            clickedSearchSongs.clear()

            sharedPrefsUtils.writeClickedSearchSongs(CLICKED_SEARCH_TRACK, clickedSearchSongs)
            showGroupClickedSong()
            recyclerViewClicked.adapter?.notifyDataSetChanged()
        }

        /*     Формирование списка найденных песен в recyclerViewSearch                 */
        recyclerViewSearch.layoutManager = LinearLayoutManager(this)
        recyclerViewSearch.adapter = SearchMusicAdapter(searchSongs, this)
        /*     Формирование списка сохраненных (кликнутых) песен в recyclerViewClicked  */
        recyclerViewClicked.layoutManager = LinearLayoutManager(this)
        recyclerViewClicked.adapter = ClickedMusicAdapter(clickedSearchSongs, this)

        // КОНЕЦ  fun onCreate(savedInstanceState: Bundle?)
    }

    // нажатие на найденные песни в Recycler через SearchMusicAdapter
    @SuppressLint("SuspiciousIndentation")
    override fun onClickRecyclerItemView(clickedTrack: Track) {

        if (clickedSearchSongs.contains(clickedTrack)) {
            clickedSearchSongs.remove(clickedTrack)
        } else if (clickedSearchSongs.size >= 10) {
            clickedSearchSongs.removeAt(clickedSearchSongs.size - 1)
        }
        clickedSearchSongs.add(0, clickedTrack)
        val sharedPrefsApp = getSharedPreferences(MUSIC_MAKER_PREFERENCES, Application.MODE_PRIVATE)
        val sharedPrefsUtils = SharedPrefsUtils(sharedPrefsApp)

        sharedPrefsUtils.writeClickedSearchSongs(CLICKED_SEARCH_TRACK, clickedSearchSongs)

        App.activeTracks.add(0,clickedTrack)
        val displayIntent = Intent(this, MediaActivity::class.java)
        displayIntent.putExtra("trackId", clickedTrack.trackId)
        startActivity(displayIntent)
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








