package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MediaActivity : AppCompatActivity() {

    private lateinit var playerTrack: Track
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesSearchApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)


        //val text = (Base64.getDecoder().decode("WWFuZGV4LkZpbnRlY2guQW5kcm9pZA==").decodeToString())

        val trackId = getIntent().getIntExtra("trackId", 1)
        lateinit var playerTrack : Track

        // Элементы экрана:
        val backOffImage = findViewById<ImageView>(R.id.iv_back)  // стрелка НАЗАД
        val cover =  findViewById<ImageView>(R.id.iv_cover512)
        val title = findViewById<TextView>(R.id.tv_title)
        val  artist= findViewById<TextView>(R.id.tv_artist)
        val buttonAdd= findViewById<ImageView>(R.id.iv_add)
        val buttonPlay = findViewById<ImageView>(R.id.iv_play)
        val buttonLike = findViewById<ImageView>(R.id.iv_like)
        val playback = findViewById<TextView>(R.id.tv_playback_time)
        val durationTrack = findViewById<TextView>(R.id.tv_duration)
        val album = findViewById<TextView>(R.id.tv_album)
        val yearTrack = findViewById<TextView>(R.id.tv_year)
        val genre = findViewById<TextView>(R.id.tv_genre)
        val country = findViewById<TextView>(R.id.tv_country)

        title.setText(trackId.toString())

        backOffImage.setOnClickListener { finish() }

        fun searchSongByText2(searchText: String) {
            iTunesService.searchSongApi(searchText).enqueue(object :
                Callback<ITunesResponse> {

                override fun onResponse(call: Call<ITunesResponse>, response: Response<ITunesResponse>)
                {
                    title.visibility = View.VISIBLE
                    if (response.code() == 200) {
                        View.VISIBLE
                        if (response.body()?.results?.isNotEmpty() == true) {
                            playerTrack = response.body()?.results!![0]

                        } else {

                        }

                    } else {

                    }
                }

                override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {

                }
            })
        }
        searchSongByText2(trackId.toString())

        title.setText(playerTrack.trackName)
        artist.setText(playerTrack.artistName)
        country.setText(playerTrack.country)
        album.setText(playerTrack.collectionName)
        durationTrack.setText(playerTrack.artworkUrl100)




    // END of fun onCreate
    }
    // Функция выполнения ПОИСКОВОГО ЗАПРОСА


}


