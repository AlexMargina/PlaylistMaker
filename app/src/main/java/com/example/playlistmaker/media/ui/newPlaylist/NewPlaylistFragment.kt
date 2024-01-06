package com.example.playlistmaker.media.ui.newPlaylist

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.media.domain.Playlist
import com.example.playlistmaker.media.ui.playlist.PlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel


open class NewPlaylistFragment : Fragment() {

    open val viewModel by viewModel<NewPlaylistViewModel>()
    private val playlistViewModel by viewModel<PlaylistViewModel>()
    lateinit var binding: FragmentNewPlaylistBinding
    private lateinit var completeDialog: MaterialAlertDialogBuilder
    var selectedUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Пользователь дал разрешение, можно продолжать работу
                viewModel.loadCover()
            } else {
                Toast.makeText(requireActivity(),
                   "Разрешение необходимо для выбора обложек плэйлистов",
                    Toast.LENGTH_SHORT ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNewPlaylistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.pictureLiveData.observe(viewLifecycleOwner) { uri ->
            if (uri==null) {
                binding.ivCoverPl .background = null
            } else {
                selectedUri = uri
                binding.ivCoverPlImage.setImageURI(uri)
                binding.ivPicturePlus.visibility = View.GONE
            }
        }

        viewModel.loadPickMedia(this)

        checkPermission()

        //1. Нажатие на область выбора обложки
        binding.ivCoverPl.setOnClickListener {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

         //2. сделать неактивной кнопку Создать
        if (binding.etNamePl.editText!!.text.isEmpty()) {
            binding.tvButtonNew.isEnabled = false
        }

        // 3. Нажатие на кнопку Создать
        binding.tvButtonNew.setOnClickListener {
            val imageFileNamePl = viewModel.imagePath() + "/" +
                    binding.etNamePl.editText!!.text.toString() + ".jpg"

            Log.d ("MAALMI_NewPlaylistF", "imageFileNamePl = ${imageFileNamePl} imagePath = ${viewModel.imagePath()}")
            Log.d ("MAALMI_NewPlaylistF", "ietNamePl = ${binding.ietNamePl.text} etNamePl=  ${binding.etNamePl.editText !!.text.toString()}" )
            Log.d ("MAALMI_NewPlaylistF", "ietDescriptPl = ${binding.ietDescriptPl.text} etDescriptPl=  ${binding.etDescriptPl.editText !!.text.toString()}" )
            viewModel.insertPlaylist(
                Playlist(
                   idPl = 0,
                    namePl = binding.ietNamePl.text.toString() ,
                    descriptPl = binding.ietDescriptPl.text.toString(),
                    imagePl = if (selectedUri != null) {
                        imageFileNamePl
                     } else {
                       ""
                    }
                )
            )


            playlistViewModel.getPlaylist()

            Toast.makeText(requireActivity(),
                "Плейлист ${binding.etNamePl.editText!!.text} создан",
                Toast.LENGTH_SHORT ).show()
            findNavController().navigateUp()
        }

        completeDialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(getString(R.string.title_dialog_new_playlist))
            .setMessage(getString(R.string.message_dialog_new_playlist))
            .setNeutralButton(getString(R.string.cancel_button)) { dialog, which -> }
            .setPositiveButton(getString(R.string.finish_button)) { dialog, which ->
                findNavController().navigateUp()
            }

        // 4. Нажатие на кнопку НАЗАД
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    reallyCompleteToExit()
                }
            })

        binding.ivBack.setOnClickListener {
            reallyCompleteToExit()
        }

         // 5. Изменения поля НАЗВАНИЕ
        binding.etNamePl.editText!!.doOnTextChanged { text, start, before, count ->
            text?.let { binding.etNamePl.changeColorOnTextChange(it) }
            binding.tvButtonNew.isEnabled = ! text.isNullOrEmpty()
        }

        // 6. Изменения поля ОПИСАНИЕ
        binding.etDescriptPl.editText!!.doOnTextChanged { text, start, before, count ->
            text?.let { binding.etDescriptPl.changeColorOnTextChange(it) }
        }
    }

    
    private fun reallyCompleteToExit() {
        if (binding.ivCoverPl .background == null
            || binding.etNamePl.editText!!.text!!.isNotEmpty()
            || binding.etDescriptPl.editText!!.text!!.isNotEmpty()
        ) {
            completeDialog.show()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun checkPermission() {
        val permissionProvided = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.CAMERA
        )
    }
}

private fun TextInputLayout.changeColorOnTextChange(text: CharSequence) {
    if (text.isEmpty()) this.changeStrokeColor(R.color.empty_element)
    else this.changeStrokeColor(R.color.filled_element)
}

private fun TextInputLayout.changeStrokeColor(colorStateListId: Int) {
    this.defaultHintTextColor = resources.getColorStateList(colorStateListId, null)
    this.setBoxStrokeColorStateList(resources.getColorStateList(colorStateListId, null))
}