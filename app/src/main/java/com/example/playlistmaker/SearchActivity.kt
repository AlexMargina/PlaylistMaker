package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView


class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_STRING = "SEARCH_STRING"
        var searchValue=""
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

//нажатие на стрелку НАЗАД
        val backOffImage = findViewById<ImageView>(R.id.back_off_search)
        //вызов экрана MainActivity
        backOffImage.setOnClickListener {
            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
        }

        val inputSearchText = findViewById<EditText>(R.id.inputSearchText)

// при нажатии на крестик очистки поля поиска:
        val clearButton = findViewById<ImageView>(R.id.icon_clear_search)
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

                if (s.toString().trim().isEmpty() ) {
                    clearButton.setVisibility (View.GONE)
                } else {
                    clearButton.setVisibility (View.VISIBLE)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                searchValue= s.toString()
            }
        })

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        //заполнение тектового поля из предыдущего запуска Активити
        val inputSearchText = findViewById<EditText>(R.id.inputSearchText)

        if (savedInstanceState.containsKey(SEARCH_STRING)) {
            val searchText = savedInstanceState.getString(SEARCH_STRING)
            inputSearchText.setText(searchText)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val inputSearchText = findViewById<EditText>(R.id.inputSearchText)
        searchValue=inputSearchText.text.toString()
        outState.putString(SEARCH_STRING, searchValue)
    }
}
