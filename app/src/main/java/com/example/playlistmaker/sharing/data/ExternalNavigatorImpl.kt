package com.example.playlistmaker.sharing.data

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.ExternalNavigator


class ExternalNavigatorImpl(private val application: Application) : ExternalNavigator {

    val sendText =  application.getText(R.string.extra_send).toString()
    val sendTitle =  application.getText(R.string.send_title).toString()
    val extraText = application.getText(R.string.extra_text).toString()
    val extraMail = application.getText(R.string.extra_mail).toString()
    val extraSubject = application.getText(R.string.extra_subject).toString()
    val oferUrl = application.getText(R.string.ofer_url).toString()

    override fun sendShare()  {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, sendText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, sendTitle)
         executeIntent(shareIntent)
    }

    override fun shareText(sharedText : String, sharedTitle : String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, sharedText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, sharedTitle)
        executeIntent(shareIntent)
    }

    override fun sendMail()  {

            val mailIntent = Intent(Intent.ACTION_SENDTO)
            mailIntent.data = Uri.parse("mailto:")
            mailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(extraMail))
            mailIntent.putExtra(Intent.EXTRA_TEXT, extraText)
            mailIntent.putExtra(Intent.EXTRA_SUBJECT,extraSubject)
            executeIntent(mailIntent)
    }

     override fun sendOfer() {

         val oferIntent = Intent(Intent.ACTION_VIEW, Uri.parse(oferUrl as String?))
         executeIntent(oferIntent)
    }

    fun executeIntent (intent: Intent) {
        try {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            application.startActivity(intent)
        }
        catch (ex: Exception) {
            Log.d ("MAALMI", ex.toString())
        }
    }
}