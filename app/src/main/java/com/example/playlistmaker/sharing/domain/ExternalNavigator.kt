package com.example.playlistmaker.sharing.domain

interface ExternalNavigator  {

      fun sendShare (sendText:String, sendTitle: String)

      fun sendMail (extraText: String, extraMail: String, extraSubject: String)

      fun sendOfer (oferUrl:String)
}