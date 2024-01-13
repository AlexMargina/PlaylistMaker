package com.example.playlistmaker.sharing.domain

interface SharingInteractor {

    fun shareApp()
    fun shareText(sharedText : String, sharedTitle : String)
    fun openSupport()
    fun openTerms()
}