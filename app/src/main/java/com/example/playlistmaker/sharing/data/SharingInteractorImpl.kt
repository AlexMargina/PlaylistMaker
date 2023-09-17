package com.example.playlistmaker.sharing.data

import com.example.playlistmaker.sharing.domain.SharingInteractor

class SharingInteractorImpl(val externalNavigator: ExternalNavigatorImpl) : SharingInteractor {

    override fun shareApp(sendText:String, sendTitle: String) {
        externalNavigator.sendShare(sendText,sendTitle )
    }

    override fun openTerms(oferUrl:String) {
        externalNavigator.sendOfer(oferUrl)
    }

    override fun openSupport(extraText: String, extraMail: String, extraSubject: String) {
        externalNavigator.sendMail(extraText, extraMail, extraSubject)
    }
}