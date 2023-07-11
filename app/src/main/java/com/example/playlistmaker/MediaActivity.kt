package com.example.playlistmaker

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Base64


class MediaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        val textDecode = findViewById<TextView>(R.id.textView1)
        textDecode.setText(Base64.getDecoder().decode("WWFuZGV4LkZpbnRlY2guQW5kcm9pZA==").decodeToString())

    }
}