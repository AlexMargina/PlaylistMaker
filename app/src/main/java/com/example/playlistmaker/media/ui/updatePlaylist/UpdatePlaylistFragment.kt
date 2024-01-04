package com.example.playlistmaker.media.ui.updatePlaylist

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
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

          // Третий вариант передачи данных плэйлиста из DisplayPlaylistFragment
          val idPl = getArguments()?.getInt("idPl")
          var imagePl = getArguments()?.getString("imagePl")
          var namePl = getArguments()?.getString("namePl")
          var descriptPl = getArguments()?.getString("descriptPl")
          Log.d("MAALMI_UpdatePlaylistF", "Получено из bundle: idPl = $idPl \n namePl = ${namePl.toString()}  \n " +
                  "imagePl = $imagePl \n descriptPl = ${descriptPl} ")

          viewModel.initialization() // Второй вариант передачи данных плэйлиста из DisplayPlaylistFragment
          viewModel.update.observe(viewLifecycleOwner) { isUpdate ->
               if (isUpdate) findNavController().navigateUp()
          }

          viewModel.updateLiveData.observe(viewLifecycleOwner) { playlist ->
               if (playlist.namePl.isEmpty()) findNavController().navigateUp()
               fillFields (playlist)
          }

           requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
               object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                         findNavController().navigateUp()
                    }
               })

          binding.ivBack.setOnClickListener {
               findNavController().navigateUp()
          }

         binding.etDescriptPl .setOnClickListener {
             binding.tvButtonNew.isEnabled = true
         }
          binding.ivCoverPlImage.setOnTouchListener { v, event ->
              when (event?.action) {
                  MotionEvent.ACTION_DOWN -> binding.tvButtonNew.isEnabled = true
              }
              v?.onTouchEvent(event) ?: true
          }

         binding.tvButtonNew.setOnClickListener {
              // запомнить старое название
              val oldNamePl = namePl
              //получить новое имя файла если есть изменения
              if (binding.etNamePl.editText!!.text.toString().isNotEmpty()) namePl = binding.etNamePl.editText!!.text.toString()
              if (selectedUri != null) {
                  //удалить старый файл deletePicture(oldNamePl)
                  if (oldNamePl != null) {
                      viewModel.deletePicture(oldNamePl)
                  }
                  // записать новый файл
                  viewModel.savePicture(selectedUri, namePl !!)
                    // новый путь к файлу
                  imagePl = viewModel.imagePath() + "/" + namePl + ".jpg"
              }     // если не выбрана новая обложка, то остается старый файл со старым названием

               Log.d("MAALMI_UpdatePlaylistF", "СОХРАНИТЬ: idPl = $idPl \n namePl = $namePl  \n " +
                       "imagePl = $imagePl \n descriptPl = ${descriptPl} \n selectedUri = ${selectedUri.toString()}")
              viewModel.updatePl(
                   idPl,
                   namePl = namePl ,
                   imagePl = imagePl,
                   descriptPl = binding.etDescriptPl.editText!!.text.toString())
               findNavController().navigateUp()
          }

          binding.etNamePl.setOnFocusChangeListener { _, hasFocus ->
               binding.tvButtonNew.isEnabled = true
               if (hasFocus) binding.etNamePl.hint = "Название*"
               else  binding.etNamePl.hint = namePl
          }

     } //==================================================


     private fun fillFields (playlist : Playlist) {
          binding.tvNewPlaylist.text = "Редактировать"
          binding.tvButtonNew.setText("Сохранить")
          binding.etNamePl.hint = playlist.namePl
          binding.etDescriptPl.hint = playlist.descriptPl
          binding.ivPicturePlus.isVisible = false
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