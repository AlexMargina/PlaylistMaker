package com.example.playlistmaker.sharing.domain

interface SharingInteractor {

    fun shareApp(sendText:String, sendTitle: String)
    fun openSupport(extraText: String, extraMail: String, extraSubject: String)
    fun openTerms(oferUrl:String)

}