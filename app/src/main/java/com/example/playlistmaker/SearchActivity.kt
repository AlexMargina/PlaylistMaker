package com.example.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    private val searchSongs = mutableListOf<Track>() // песни найденные через iTunesApi
    private val songList = makeArrayList ()     // список, созданный временно для проверки Recycler

    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesSearchApi::class.java)



    companion object {
        const val SEARCH_STRING = "SEARCH_STRING"
    }

    /* Основная функции при создании активити поиска:
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)
        // Элементы экрана:
        val backOffImage = findViewById<ImageView>(R.id.back_off_search)  //нажатие на стрелку НАЗАД
        val clearButton = findViewById<ImageView>(R.id.icon_clear_search)  //
        val inputSearchText = findViewById<EditText>(R.id.inputSearchText)  //
        val recyclerViewSearch = findViewById<RecyclerView>(R.id.recyclerViewSearch)

        //нажатие на стрелку НАЗАД
        backOffImage.setOnClickListener {
            finish()
        }

        // при нажатии на крестик очистки поля поиска:
        clearButton.setOnClickListener {
            inputSearchText.setText("")
        }

        // Привязка обьекта TextWatcher
        inputSearchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

        // если будут изменения текста в поле поиска, то крестик очистки появится, при удалении - станет невидимым
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s.toString().trim().isEmpty()) {
                    clearButton.visibility = View.GONE
                } else {
                    clearButton.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                s.toString()
            }
        })

        // обработка нажатия на кнопку Done

        inputSearchText.setOnEditorActionListener { _, actionId, _ ->  if (actionId == EditorInfo.IME_ACTION_DONE) {
                // ВЫПОЛНЯЙТЕ ПОИСКОВЫЙ ЗАПРОС ЗДЕСЬ

                iTunesService.searchSongApi(inputSearchText.text.toString()).enqueue(object : Callback<ITunesResponse> {

                    override fun onResponse(call: Call<ITunesResponse>, response: Response<ITunesResponse>
                    ) {
                        if (response.code() == 200) {
                           // movies.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                searchSongs.addAll(response.body()?.results!!)
                                recyclerViewSearch.adapter?.notifyDataSetChanged()
                              }
                        } else {
                            Toast.makeText(this@SearchActivity, "Не найдено песен!", Toast.LENGTH_SHORT)
                                .show()
                            //переделать: (getString(R.string.something_went_wrong), response.code().toString())
                        }
                    }

                    override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                        // переделать: showMessage(getString(R.string.something_went_wrong), t.message.toString())
                    }

                })
                                }

                    if (inputSearchText.text.isNotEmpty()) {            }
            false
        }



        /*     Формирование списка найденных песен в recyclerViewSearch
        */
        recyclerViewSearch.layoutManager = LinearLayoutManager (this)
        recyclerViewSearch.adapter = SearchMusicAdapter(searchSongs)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        // запоминание текста посиковой строки inputSearchText в переменную
            val inputSearchText = findViewById<EditText>(R.id.inputSearchText)
            outState.putString(SEARCH_STRING, inputSearchText.text.toString())
        super.onSaveInstanceState(outState)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        //заполнение тектового поля из предыдущего запуска Активити
        val inputSearchText = findViewById<EditText>(R.id.inputSearchText)
        if (savedInstanceState.containsKey(SEARCH_STRING)) {
            val searchText = savedInstanceState.getString(SEARCH_STRING)
            inputSearchText.setText(searchText)
        }
    }
}




