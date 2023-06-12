package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setings)

        //нажатие на стрелку НАЗАД
        val backOffImage = findViewById<ImageView>(R.id.back_off)
        //вызов экрана MainActivity
        backOffImage.setOnClickListener {
             finish()
        }

//нажатие на пиктограмму ПОДЕЛИТЬСЯ
        val imageButton_share = findViewById<ImageView>(R.id.imageButton_share)
        val sendText = this.getText(R.string.extra_send)
        imageButton_share.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, sendText)
                type = "text/plain"
            }
            val  sendTitle= this.getText(R.string.send_title).toString()
            val shareIntent = Intent.createChooser(sendIntent, sendTitle)
            startActivity(shareIntent)
        }

//нажатие на пиктограмму НАПИСАТЬ в ПОДДЕРЖКУ
        val imageButtonSupport = findViewById<ImageView>(R.id.imageButton_support)
        imageButtonSupport.setOnClickListener {
            val mailIntent = Intent(Intent.ACTION_SENDTO)
            val extraText = this.getText(R.string.extra_text).toString()
            val extraMail = application.getText(R.string.extra_mail).toString()
            val extraSubject = this.getText(R.string.extra_subject).toString()
            mailIntent.data = Uri.parse("mailto:")
            mailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(extraMail))
            mailIntent.putExtra(Intent.EXTRA_TEXT, extraText)
            mailIntent.putExtra(Intent.EXTRA_SUBJECT,extraSubject)
            startActivity(mailIntent)
        }

//нажатие на фрэйм ПОЛЬЗОВАТЕЛЬСКОЕ СОГЛАШЕНИЕ - когда не попадается по пиктограмме > (слишком она маленькая)
        val frameLayout_ofer = findViewById<FrameLayout>(R.id.frameLayout_ofer)

        frameLayout_ofer.setOnClickListener {
            val oferUrl = this.getText(R.string.ofer_url)
            val oferIntent = Intent(Intent.ACTION_VIEW, Uri.parse(oferUrl as String?))
            startActivity(oferIntent)
        }

    }
}