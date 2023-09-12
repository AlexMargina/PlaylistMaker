package com.example.playlistmaker.setting.data

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import android.net.Uri
import android.os.Bundle


class MailSending : AppCompatActivity (){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setings)
    val mailIntent = Intent(Intent.ACTION_SENDTO)
    val extraText = getText(R.string.extra_text).toString()
    val extraMail = application.getText(R.string.extra_mail).toString()
    val extraSubject = getText(R.string.extra_subject).toString()
    mailIntent.data = Uri.parse("mailto:")
    mailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(extraMail))
    mailIntent.putExtra(Intent.EXTRA_TEXT, extraText)
    mailIntent.putExtra(Intent.EXTRA_SUBJECT,extraSubject)
    startActivity(mailIntent)
}}