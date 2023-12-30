package com.example.playlistmaker.media.ui.updatePlaylist

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.media.ui.newPlaylist.NewPlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class UpdatePlaylistFragment : NewPlaylistFragment() {

     private val viewModel by viewModel<UpdatePlaylistViewModel>()

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
          super.onViewCreated(view, savedInstanceState)

          val idPl = requireArguments().getInt(IDPL)
          val imagePl = requireArguments().getString(IMAGEPL)
          val namePl = requireArguments().getString(NAMEPL)
          val descriptPl = requireArguments().getString(DESCRIPTPL)

          viewModel.update.observe(viewLifecycleOwner) { isUpdate ->
               if (isUpdate) findNavController().navigateUp()
          }
     }

     companion object {
          const val IDPL = "idPl"
          const val IMAGEPL = "imagePl"
          const val NAMEPL = "namePl"
          const val DESCRIPTPL = "descriptPl"

          fun passArgs(idPl: Int, imagePl: String, namePl: String, descriptPl: String): Bundle =
               bundleOf(IDPL to idPl, IMAGEPL to imagePl, NAMEPL to namePl, DESCRIPTPL to descriptPl)
     }
}