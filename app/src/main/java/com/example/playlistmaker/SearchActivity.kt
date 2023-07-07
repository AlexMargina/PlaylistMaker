package com.example.playlistmaker

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.App.Companion.clickedSearchSongs
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val LOG_TAG = "maalmi_SearchActivity"

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_STRING = "SEARCH_STRING"
    }

    private val searchSongs = mutableListOf<Track>() // песни найденные через iTunesApi
    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesSearchApi::class.java)

 /*       Основная функции при создании активити поиска:                                           */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

     // Элементы экрана:
     val backOffImage = findViewById<ImageView>(R.id.back_off_search)  //нажатие на стрелку НАЗАД
     val clearButton = findViewById<ImageView>(R.id.icon_clear_search)  // крестик очистки EditText
     val inputSearchText = findViewById<EditText>(R.id.inputSearchText)  //  EditText поиска песен
     val recyclerViewSearch = findViewById<RecyclerView>(R.id.recyclerViewSearch)  // Recycler найденных песен
     val noSongImage = findViewById<TextView> (R.id.image_crash)        // ImageView показа отсутствия песен
     val inetProblemImage = findViewById<TextView> (R.id.inet_problem)   // ImageView показа отсутствия интернета
     val groupClicked = findViewById<LinearLayout>(R.id.group_clicked)  // контейнер с сохраненными трэками
     val recyclerViewClicked = findViewById<RecyclerView>(R.id.recyclerViewClicked)   // Recycler сохраненных песен
     val groupSearched = findViewById<FrameLayout>(R.id.group_searched)

     clickedSearchSongs = readClickedSearchSongs()

        // Функция выполнения ПОИСКОВОГО ЗАПРОСА
        fun searchSongByText() {
                iTunesService.searchSongApi(inputSearchText.text.toString()).enqueue(object : Callback<ITunesResponse> {

                override fun onResponse(call: Call<ITunesResponse>, response: Response<ITunesResponse>)
                {
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
                    noSongImage.visibility = View.GONE
                    inetProblemImage.visibility = View.VISIBLE
                }
            })
        }

     fun showGroupClickedSong () {
         if (clickedSearchSongs.size>0) {
             groupSearched.visibility = if (inputSearchText.hasFocus() && inputSearchText.text.isEmpty()) android.view.View.GONE else android.view.View.VISIBLE
             groupClicked.visibility = if (inputSearchText.hasFocus() && inputSearchText.text.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
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

        // Привязка обьекта TextWatcher
        inputSearchText.addTextChangedListener(object : TextWatcher {

        // если будут изменения текста в поле поиска, то крестик очистки появится, при удалении - станет невидимым
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isEmpty()) {
                    clearButton.visibility = View.GONE
                } else {
                    clearButton.visibility = View.VISIBLE
                }
                showGroupClickedSong()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {  }

            override fun afterTextChanged(s: Editable?) {   }
        })

     // при получении фокуса показать историю просмотренных песен
        inputSearchText.setOnFocusChangeListener { view, hasFocus -> showGroupClickedSong ()   }

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
             searchSongByText()    //применяем функцию поискового запроса
         }

         /*     Формирование списка найденных песен в recyclerViewSearch                 */
        recyclerViewSearch.layoutManager = LinearLayoutManager (this)
        recyclerViewSearch.adapter = SearchMusicAdapter(searchSongs)
         /*     Формирование списка сохраненных (кликнутых) песен в recyclerViewClicked  */
        recyclerViewClicked.layoutManager = LinearLayoutManager (this)
        recyclerViewClicked.adapter = ClickedMusicAdapter (clickedSearchSongs)


     // КОНЕЦ  fun onCreate(savedInstanceState: Bundle?)
    }

    // При  выходе с Активити сохраняем список просмотренных песен
    override fun onStop() {
        super.onStop()
        writeClickedSearchSongs(clickedSearchSongs)
    }



    //функция сохранения списка просмотренных песен
    fun writeClickedSearchSongs(clickedSearchSongs: ArrayList<Track>) {
        val sharedPrefsApp = getSharedPreferences(MUSIC_MAKER_PREFERENCES, Application.MODE_PRIVATE)
        val json = GsonBuilder().create()
        val jsonString = json.toJson(clickedSearchSongs)
        sharedPrefsApp.edit()
            .putString(CLICKED_SEARCH_TRACK, jsonString)
            .apply()
        Log.d(LOG_TAG, "Перед записью в файл: ${clickedSearchSongs.toString()}")
        Log.d(LOG_TAG, "Записано в файл $jsonString")
    }

    //функция чтения списка просмотренных песен
    fun readClickedSearchSongs() : ArrayList<Track> {
        val sharedPrefsApp = getSharedPreferences(MUSIC_MAKER_PREFERENCES, Application.MODE_PRIVATE)
        val jsonString = sharedPrefsApp.getString(CLICKED_SEARCH_TRACK, null)
        val json = GsonBuilder().create()
        clickedSearchSongs = json.fromJson(jsonString, object: TypeToken<ArrayList<Track>>() { }.type) ?: arrayListOf()

        Log.d(LOG_TAG, "Прочитано с файла: $jsonString")
        Log.d(LOG_TAG, "Десериализовано:   ${clickedSearchSongs.toString()}")
        Log.d(LOG_TAG, "1 песня. ID:   ${clickedSearchSongs[0].trackId}")
        Log.d(LOG_TAG, "1 песня. Artist:   ${clickedSearchSongs[0].artistName}")

        return clickedSearchSongs
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

    fun showListClickedSong () {

    }
}




