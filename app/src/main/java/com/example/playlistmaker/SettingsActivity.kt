package com.example.playlistmaker

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setings)

        // Элементы экрана
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        val backOffImage = findViewById<ImageView>(R.id.back_off)
        val imageButtonShare = findViewById<ImageView>(R.id.imageButton_share)
        val imageButtonSupport = findViewById<ImageView>(R.id.imageButton_support)
        val frameLayoutOfer = findViewById<FrameLayout>(R.id.frameLayout_ofer)

        // изменение темы приложения
        val sharedPrefs = getSharedPreferences(MUSIC_MAKER_PREFERENCES, Application.MODE_PRIVATE)

        themeSwitcher.setChecked (sharedPrefs.getString (DARK_THEME_ENABLED, "false").toBoolean())

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
             sharedPrefs.edit()
                 .putString(DARK_THEME_ENABLED, checked.toString())
                 .apply()
            (applicationContext as App).switchTheme(checked)
        }

        //нажатие на стрелку НАЗАД
        backOffImage.setOnClickListener {
             finish()
        }

        //нажатие на пиктограмму ПОДЕЛИТЬСЯ
        val sendText = this.getText(R.string.extra_send)
        imageButtonShare.setOnClickListener {
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
        frameLayoutOfer.setOnClickListener {
            val oferUrl = this.getText(R.string.ofer_url)
            val oferIntent = Intent(Intent.ACTION_VIEW, Uri.parse(oferUrl as String?))
            startActivity(oferIntent)
        }
    }
}