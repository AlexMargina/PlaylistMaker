package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//нажатие на кнопку ПОИСК
        val butonSearch = findViewById<Button>(R.id.buton_search)
        butonSearch.setOnClickListener {
            val displayIntent = Intent(this, SearchActivity::class.java)
            startActivity(displayIntent)
       }
//нажатие на кнопку МЕДИА
        val butonMedia = findViewById<Button>(R.id.buton_media)
        butonMedia.setOnClickListener {
            val displayIntent = Intent(this, MediaActivity::class.java)
            startActivity(displayIntent)
         }

//нажатие на кнопку НАСТРОЙКИ
        val butonSetting = findViewById<Button>(R.id.buton_setting)

//вызов экрана SettingsActivity
        butonSetting.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
    }
}