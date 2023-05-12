package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

/*        val text_clicable = findViewById<TextView>(R.id.playlist_maker)
        val textClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity,
                    "Программа поиска музыки и составления плэйлистов", Toast.LENGTH_SHORT).show()
            }
        }
        text_clicable.setOnClickListener(textClickListener)*/

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
// вызов тоста на экран
        /*  buton_setting.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на кнопку НАСТРОЙКИ!", Toast.LENGTH_SHORT).show()
        }*/
//вызов экрана SettingsActivity
        butonSetting.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
    }
}