package com.example.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_STRING = "SEARCH_STRING"
    }

    /* Основная функции при создании активити поиска:
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //нажатие на стрелку НАЗАД
        val backOffImage = findViewById<ImageView>(R.id.back_off_search)
        backOffImage.setOnClickListener {
            finish()
        }

        // при нажатии на крестик очистки поля поиска:
        val clearButton = findViewById<ImageView>(R.id.icon_clear_search)
        val inputSearchText = findViewById<EditText>(R.id.inputSearchText)
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

        /*     Формирование списка найденных песен в recyclerViewSearch
        */

        val recyclerViewSearch = findViewById<RecyclerView>(R.id.recyclerViewSearch)
        val songList = makeArrayList ()

        recyclerViewSearch.layoutManager = LinearLayoutManager (this)
        recyclerViewSearch.adapter = SearchMusicAdapter(songList)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // запоминание текста посиковой строки inputSearchText в переменную
        val inputSearchText = findViewById<EditText>(R.id.inputSearchText)
        outState.putString(SEARCH_STRING, inputSearchText.text.toString())
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



