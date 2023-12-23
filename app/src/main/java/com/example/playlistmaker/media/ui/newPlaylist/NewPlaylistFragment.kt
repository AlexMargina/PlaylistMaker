package com.example.playlistmaker.media.ui.newPlaylist

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
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
import org.koin.androidx.viewmodel.ext.android.viewModel


class NewPlaylistFragment : Fragment() {

    private val viewModel by viewModel<NewPlaylistViewModel>()
    private val playlistViewModel by viewModel<PlaylistViewModel>()

    private lateinit var binding: FragmentNewPlaylistBinding

    private lateinit var completeDialog: MaterialAlertDialogBuilder

    private var loadUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Пользователь дал разрешение, можно продолжать работу
                viewModel.loadCover()
                //                binding.permissionGranted.visibility = View.VISIBLE
            } else {
                // Пользователь отказал в предоставлении разрешения
//                binding.permissionRequestFrame.visibility = View.VISIBLE
//                binding.permissionGranted.visibility = View.GONE
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

        viewModel.placeholderLiveData.observe(viewLifecycleOwner) { cover ->
            if (!cover) {
                binding.ivCoverPl .background = null
                //binding.ivCoverPl .visibility = View.GONE
            }
        }

        viewModel.pictureLiveData.observe(viewLifecycleOwner) { uri ->
            loadUri = uri
            binding.ivCoverPlImage.setImageURI(uri)
            binding.ivPicturePlus.visibility = View.GONE
        }

        viewModel.loadPickMedia(this)

        checkPermission()

        //1. Нажатие на область выбора обложки
        binding.ivCoverPl.setOnClickListener {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        // 2. сделать неактивной кнопку Создать
        if (binding.tvNamePl.text!!.isEmpty()) {
            binding.tvButtonNew.isEnabled = false
        }

        // 3. Нажатие на кнопку Создать
        binding.tvButtonNew.setOnClickListener {
            viewModel.insertPlaylist(
                Playlist(
                   idPl = 0,
                    namePl = binding.etNamePl.text.toString(),
                    descriptPl = binding.etDescriptPl.text.toString(),
                    imagePl = if (loadUri != null) {
                        loadUri.toString()
                    } else {
                        ""
                    }
                )
            )

            playlistViewModel.getPlaylist()

            Toast.makeText(requireActivity(), "Плейлист «${binding.etNamePl.text}» создан",
                Toast.LENGTH_SHORT
            ).show()
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
        binding.etNamePl.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) this.binding.etNamePl.setBackgroundResource (R.color.empty_element)
            else this.binding.etNamePl.setBackgroundResource(R.color.filled_element)
           //TODO  Замена рамки etNamePl если есть   text
            binding.tvButtonNew.isEnabled = ! text.isNullOrEmpty()
        }

        binding.etDescriptPl.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) this.binding.etDescriptPl.setBackgroundResource (R.color.empty_element)
            else this.binding.etDescriptPl.setBackgroundResource(R.color.filled_element)
        }
    }


    private fun reallyCompleteToExit() {
        if (binding.ivCoverPl .background == null
            || binding.etNamePl .text!!.isNotEmpty()
            || binding.etDescriptPl .text!!.isNotEmpty()
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
        if (permissionProvided == PackageManager.PERMISSION_GRANTED) {
//            binding.permissionRequestFrame.visibility = View.GONE
//            binding.permissionGranted.visibility = View.VISIBLE
        } else if (permissionProvided == PackageManager.PERMISSION_DENIED) {
//            binding.permissionRequestFrame.visibility = View.VISIBLE
//            binding.permissionGranted.visibility = View.GONE
        }
    }

}