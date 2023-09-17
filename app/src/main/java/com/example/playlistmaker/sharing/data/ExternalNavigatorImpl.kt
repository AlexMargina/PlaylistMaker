package com.example.playlistmaker.sharing.data

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log


class ExternalNavigatorImpl(private val application: Application) : ExternalNavigator {



    override fun sendShare(sendText:String, sendTitle: String)  {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, sendText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, sendTitle)
         executeIntent(shareIntent)
    }

    override fun sendMail(extraText: String, extraMail: String, extraSubject: String)  {

            val mailIntent = Intent(Intent.ACTION_SENDTO)
            mailIntent.data = Uri.parse("mailto:")
            mailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(extraMail))
            mailIntent.putExtra(Intent.EXTRA_TEXT, extraText)
            mailIntent.putExtra(Intent.EXTRA_SUBJECT,extraSubject)
            executeIntent(mailIntent)
    }

     override fun sendOfer(oferUrl:String) {

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