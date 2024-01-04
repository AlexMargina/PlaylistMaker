package com.example.playlistmaker.media.ui.displayPlaylist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentDisplayPlaylistBinding
import com.example.playlistmaker.media.domain.Playlist
import com.example.playlistmaker.search.domain.TrackModel
import com.example.playlistmaker.search.ui.SearchFragment.Companion.CLICK_DEBOUNCE_DELAY
import com.example.playlistmaker.search.ui.SearchMusicAdapter
import com.example.playlistmaker.utils.Converters
import com.example.playlistmaker.utils.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class DisplayPlaylistFragment : Fragment() {

    private var _binding: FragmentDisplayPlaylistBinding? = null
    private val binding get() = _binding !!
    private val viewModel by viewModel<DisplayPlaylistViewModel>()
    private lateinit var trackClickListener: (TrackModel) -> Unit
    private val adapter = SearchMusicAdapter(arrayListOf<TrackModel>(), {trackClickListener(it)})
    //var actualPlaylist : Playlist? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDisplayPlaylistBinding.inflate(inflater, container, false)
        return binding.root
        Log.d("MAALMI_DisplayPlaylistFragment", "playlistId = ${Bundle.CONTENTS_FILE_DESCRIPTOR}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerTracksOfPlaylist.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerTracksOfPlaylist.adapter = adapter
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet)
        val bottomSheetBehaviorMenu = BottomSheetBehavior.from(binding.bottomSheetMenu)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN

        // 1. Получить аргументы с предыдущего экрана (idPl с PlaylistFragment)
        val idPl = requireArguments().getInt("PLAYLIST")

        // 2. Получить плэйлист, соответсвующий idPl (из БД)
        viewModel.getPlaylistById(idPl)

        // 3. Передать трэки плейлиста в адаптер и отобразить плэйлист
        viewModel.playlistLiveData.observe(viewLifecycleOwner) { playlist ->
            adapter.tracks.clear()
            adapter.tracks.addAll(playlist.tracksPl)
            adapter.notifyDataSetChanged()
            actualPlaylist = playlist
            displayPlaylist(actualPlaylist!!)
            Log.d("MAALMI_DisplayPlaylistFragment", "!!Изменился playlist= $playlist")
        }

        // 4. Обработать нажатие на трэк плэйлиста
        trackClickListener = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope, false
        ) { track ->
            Log.d("MAALMI_SearchFrag", "1. Нажали на track=${track}")
            viewModel.addTrackToHistory(track)
            runPlayer(track.trackId.toString())
        }

        // 5. Обработать длинное нажатие на трэк плэйлиста
        adapter.onLongClickListener = { track ->
            deleteTrackDialog(track.trackId).show()
            true
        }

        // 6. Нажатие на кнопку НАЗАД
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.mediaFragment)
                }
            })
        binding.ivBack.setOnClickListener {
            findNavController().navigate(R.id.mediaFragment)
        }

        // Нажатие на кнопку МЕНЮ (...)
        binding.ibMenu.setOnClickListener {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_COLLAPSED
            displaybottomSheetBehaviorMenu ()
        }

        // Нажатие на кнопку ПОДЕЛИТЬСЯ
        binding.ibShare.setOnClickListener {
            shareDialog()
        }

        // Выбор меню ПОДЕЛИТЬСЯ
        binding.shareBottomSheet.setOnClickListener {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
            shareDialog()
        }

        // Выбор меню РЕДАКТИРОВАТЬ ИНФОРМАЦИЮ
        binding.updatePlBottomSheet.setOnClickListener {
            val bundle : Bundle = bundleOf()
            bundle.putInt("idPl", idPl)
            bundle.putString("namePl",actualPlaylist!!.namePl )
            bundle.putString("imagePl",actualPlaylist!!.imagePl )
            bundle.putString("descriptPl",actualPlaylist!!.descriptPl )
            findNavController().navigate(R.id.updatePlaylistFragment, bundle)
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
        }

        // Выбор меню УДАЛИТЬ ПЛЭЙЛИСТ
        binding.deletePlBottomSheet.setOnClickListener {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
            Log.d("MAALMI_DisplayPlaylistFragment", "Отправляем на удаление = ${actualPlaylist !!.idPl}")
            deletePlaylistDialog (actualPlaylist !!.idPl).show()
            Log.d("MAALMI_DisplayPlaylistFragment", "После возврата c удаление = ${actualPlaylist !!.idPl}")
        }

    }   //============================================================


    private fun displayPlaylist(playlist:Playlist) {
        binding.apply {
            tvNamePl.text = playlist.namePl
            tvDesciptPl.text = playlist.descriptPl
            tvPlaylistCount.text = Converters().convertCountToTextTracks (playlist.tracksPl.size)
            tvPlaylistTime.text = (playlistTime (playlist))
        }
        val radius = resources.getDimensionPixelSize(R.dimen.corner_radius)
        val coverPlaylist = playlist.imagePl
        Glide.with(binding.ivCoverPlaylist)
            .load(coverPlaylist)
            .transform(RoundedCorners(radius))
            .placeholder(R.drawable.media_placeholder)
            .into(binding.ivCoverPlaylist)
    }

    private fun displaybottomSheetBehaviorMenu (){
        val countTracks = Converters().convertCountToTextTracks (actualPlaylist!!.countTracks)
        val radius = resources.getDimensionPixelSize(R.dimen.corner_radius)
        binding.namePlBottomSheet.text = actualPlaylist!!.namePl
        binding.tracksBottomSheet.text = countTracks
        Glide.with(binding.ivImagePlBottomSheet)
            .load(actualPlaylist!!.imagePl)
            .transform(RoundedCorners(radius))
            .placeholder(R.drawable.media_placeholder)
            .into(binding.ivImagePlBottomSheet)
    }

    private fun shareDialog() {
        val titlePlaylist = ""
        if (actualPlaylist!!.tracksPl.size<1) {
            var sharedPlaylist = getString(R.string.no_track_in_playlist)
            MaterialAlertDialogBuilder(requireActivity())
                .setTitle(sharedPlaylist) // Заголовок диалога
                .setPositiveButton(R.string.cancel_button) { dialog, which ->    }
                .show()
        } else {
            viewModel.sharePlaylist(createPlaylist(actualPlaylist !!), titlePlaylist)
        }
    }

    private fun runPlayer(trackId: String) {
        findNavController().navigate(R.id.playerFragment)
    }

    private fun playlistTime (playlist: Playlist) : String {
        var result = 0L
        for (track in playlist.tracksPl) {
            result += track.trackTimeMillis
        }
        return Converters().convertMillisToTextMinutes(result)
    }

    private fun deletePlaylistDialog(idPl: Int) = MaterialAlertDialogBuilder(requireActivity())
        .setTitle(R.string.delete_playlist)
        .setMessage(R.string.really_delete_playlist)
        .setNeutralButton(getString(R.string.no)) { _, _ -> }
        .setPositiveButton(getString(R.string.yes)) { _, _ ->
            deletePlaylistById(idPl )
        }

    private fun deleteTrackDialog(trackId: String) = MaterialAlertDialogBuilder(requireActivity())
        .setTitle(R.string.delete_track)
        .setMessage(R.string.really_delete_track)
        .setNeutralButton(getString(R.string.cancel_button)) { _, _ -> }
        .setPositiveButton(getString(R.string.delete_button)) { _, _ ->
            deleteTrackFromPlaylist(trackId, actualPlaylist!!.idPl )
    }

    private fun deletePlaylistById(idPl : Int) {
        viewModel.deletePl(idPl)
        findNavController().navigateUp()
    }

    private fun deleteTrackFromPlaylist(trackId: String, idPl : Int) {
        viewModel.deleteTrackFromPlaylist(trackId, idPl )
        viewModel.getPlaylistById(idPl)
    }

    private fun createPlaylist(playlist: Playlist) : String {
        var sharedPlaylist = playlist.namePl + "\n " +
             playlist.descriptPl + "\n " +
                Converters().convertCountToTextTracks(playlist.countTracks) + "\n "
            for ( i in 1 ..  (playlist.tracksPl.size)) {
                sharedPlaylist = sharedPlaylist + "$i. " + playlist.tracksPl[i-1].artistName + " - " +
                        playlist.tracksPl[i-1].trackName + " - " +
                        SimpleDateFormat("mm:ss", Locale.getDefault())
                            .format (playlist.tracksPl[i-1].trackTimeMillis) + "\n"
            }
        return sharedPlaylist
    }

    companion object {
        fun passArgs(playlistId: Int): Bundle = bundleOf("PLAYLIST" to playlistId)
        var actualPlaylist : Playlist? = Playlist(0, "", "", "", arrayListOf(), 0, 0L)
    }
}