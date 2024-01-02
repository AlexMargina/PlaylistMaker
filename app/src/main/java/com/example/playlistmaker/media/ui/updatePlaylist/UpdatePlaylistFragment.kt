package com.example.playlistmaker.media.ui.updatePlaylist

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.media.domain.Playlist
import com.example.playlistmaker.media.ui.displayPlaylist.DisplayPlaylistFragment
import com.example.playlistmaker.media.ui.newPlaylist.NewPlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class UpdatePlaylistFragment : NewPlaylistFragment() {

     override val viewModel by viewModel<UpdatePlaylistViewModel>()

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
          super.onViewCreated(view, savedInstanceState)

          val idPl = requireArguments().getInt("idPl")
          val imagePl = requireArguments().getString("imagePl")
          val namePl = requireArguments().getString("namePl")
          val descriptPl = requireArguments().getString("descriptPl")

          viewModel.initialization()
          viewModel.update.observe(viewLifecycleOwner) { isUpdate ->
               if (isUpdate) findNavController().navigateUp()
          }

          viewModel.updateLiveData.observe(viewLifecycleOwner) { playlist ->
               if (playlist.namePl.isEmpty()) findNavController().navigateUp()
               fillFields (playlist)
          }



          binding.etNamePl.setOnFocusChangeListener { _, hasFocus ->
               binding.tvButtonNew.isEnabled = true
               if (hasFocus) binding.etNamePl.hint = "Название*"
               else  binding.etNamePl.hint = namePl
          }

          Log.d("MAALMI_UpdatePlaylistF", "Получено: idPl = $idPl \n namePl = ${namePl.toString()}  \n " +
                  "imagePl = $imagePl \n descriptPl = ${descriptPl} ")
     } //==================================================


     private fun fillFields (playlist : Playlist) {
          binding.tvNewPlaylist.text = "Редактировать"
          binding.tvButtonNew.setText("Сохранить")
          binding.etNamePl.hint = playlist.namePl
          binding.etDescriptPl.hint = playlist.descriptPl
          val radius = resources.getDimensionPixelSize(R.dimen.corner_radius)
          Glide.with(binding.ivCoverPlImage)
               .load(DisplayPlaylistFragment.actualPlaylist !!.imagePl)
               .transform(RoundedCorners(radius))
               .placeholder(R.drawable.media_placeholder)
               .into(binding.ivCoverPlImage)
     }


     companion object {
          // Первый вариант передачи данных плэйлиста из DisplayPlaylistFragment
          fun passArgs(idPl: Int, imagePl: String, namePl: String, descriptPl: String): Bundle =
               bundleOf("idPl" to idPl, "imagePl" to imagePl, "namePl" to namePl, "descriptPl" to descriptPl)
     }
}