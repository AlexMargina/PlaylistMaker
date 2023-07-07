package com.example.playlistmaker

class OnTrackClickListener : App() {



   companion object {

      fun onTrackClick(onClickSearchTrack: Track) {
         addClickedTrack (onClickSearchTrack)
      }

      fun addClickedTrack(onClickSearchTrack: Track) {

         if (clickedSearchSongs.contains(onClickSearchTrack)) {
            clickedSearchSongs.remove(onClickSearchTrack)
         } else if (clickedSearchSongs.size>=10) {
            clickedSearchSongs.removeAt(clickedSearchSongs.size-1)
         }
         clickedSearchSongs.add(0,onClickSearchTrack)
         App.writeClickedSearchSongs (clickedSearchSongs)
      }

   }



}