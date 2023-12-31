package com.example.playlistmaker.sharing.domain

interface ExternalNavigator  {

      fun sendShare ()

      fun shareText(sharedText : String, sharedTitle : String)

      fun sendMail ()

      fun sendOfer ()
}