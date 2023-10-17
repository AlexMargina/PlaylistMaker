package com.example.playlistmaker.sharing.domain


class SharingInteractorImpl (private val externalNavigator: ExternalNavigator) : SharingInteractor {

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