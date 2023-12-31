package com.example.playlistmaker.sharing.domain

class SharingInteractorImpl (private val externalNavigator: ExternalNavigator) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.sendShare()
    }

    override fun shareText(sharedText: String, sharedTitle: String) {
        externalNavigator.shareText(sharedText, sharedTitle)
    }

    override fun openTerms() {
        externalNavigator.sendOfer()
    }

    override fun openSupport() {
        externalNavigator.sendMail()
    }
}