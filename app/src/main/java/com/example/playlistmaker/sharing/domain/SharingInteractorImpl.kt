package com.example.playlistmaker.sharing.domain

class SharingInteractorImpl (private val externalNavigator: ExternalNavigator) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.sendShare()
    }

    override fun openTerms() {
        externalNavigator.sendOfer()
    }

    override fun openSupport() {
        externalNavigator.sendMail()
    }
}