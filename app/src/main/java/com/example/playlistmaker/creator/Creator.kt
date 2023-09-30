package com.example.playlistmaker.creator

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.player.data.MediaPlayerInteractorImpl
import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.MediaPlayerRepository
import com.example.playlistmaker.search.data.SearchRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.SearchInteractorImpl
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.sharing.data.SharedPrefsSearchDataStorage


object Creator {

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(provideMediaPlayerRepository(MediaPlayer()))
    }

//    fun provideSettingsInteractor(context: Context): SettingsInteractor {
//        return SettingsInteractorImpl(SettingsRepositoryImpl(provideDataStorage(context)))
//    }

//    fun provideSharingInteractor(context: Context): SharingInteractor {
//        return SharingIntercatorImpl(ExternalNavigatorImpl(context))
//    }

    fun provideSearchInteractor(context: Context): SearchInteractor {
        return SearchInteractorImpl(provideSearchRepository(context))
    }

//    private fun provideDataStorage(context: Context): SettingsDataStorage {
//        return SharedPrefSettingsDataStorage(context)
//    }

    private fun provideMediaPlayerRepository(mediaPlayer: MediaPlayer): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl(mediaPlayer)
    }

    private fun provideSearchRepository(context: Context): SearchRepository {
        return SearchRepositoryImpl(
            RetrofitNetworkClient(context),
            SharedPrefsSearchDataStorage(context),
        )
    }
}




/*


















    lateinit var application: Application


        private fun getHistorySharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(
                CLICKED_SEARCH_TRACK,
                AppCompatActivity.MODE_PRIVATE
            )
        }

        private fun getHistorySearchDataStore(context: Context): HistorySearchDataStore {
            return HistorySearchDataStoreImpl(getHistorySharedPreferences(context))
        }

        private fun getSearchRepository(): SearchRepository {
            return SearchRepositoryImpl(getITunesApi())
        }

        private fun getITunesApi(): ITunesSearchApi {

            return Retrofit.Builder()
                .baseUrl("https://itunes.apple.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ITunesSearchApi::class.java)
        }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(MediaPlayerRepositoryImpl(mediaPlayer))
    }
    }


*/




/*
1. В Creator создать lateinit var переменную: lateinit var application: Application
2. В Creator создать функцию registryApplication(application: Application), которая будет обновлять переменную application
3. В классе, который является наследником Application, должен был использоваться для обновления темы, в методе onCreate нужно зарегистрировать контекст приложения, то есть вызвать Creator.registryApplication(this)
4. В репозитории вместо Context использовать Application
*/
