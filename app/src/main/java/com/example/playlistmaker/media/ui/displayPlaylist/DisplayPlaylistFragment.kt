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
import com.example.playlistmaker.media.ui.updatePlaylist.UpdatePlaylistFragment
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
    var actualPlaylist : Playlist? = null

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
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

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
                    findNavController().navigate(R.id.playlistFragment)
                }
            })
        binding.ivBack.setOnClickListener {
            findNavController().navigate(R.id.playlistFragment)
        }

        // Нажатие на кнопку ПОДЕЛИТЬСЯ
        binding.ibShare.setOnClickListener {
            var sharedPlaylist = getString(R.string.no_track_in_playlist)
            val titlePlaylist = ""
            sharedPlaylist = createPlaylist(actualPlaylist!!)
            viewModel.sharePlaylist(sharedPlaylist, titlePlaylist)
        }

        // Выбор меню ПОДЕЛИТЬСЯ
        binding.shareBottomSheet.setOnClickListener {

        }

        // Выбор меню РЕДАКТИРОВАТЬ ИНФОРМАЦИЮ
        binding.updatePlBottomSheet.setOnClickListener {
            findNavController().navigate(
                R.id.updatePlaylistFragment,
                UpdatePlaylistFragment.passArgs(
                    idPl,
                    binding.ivCoverPlaylist.toString(),  //imagePl
                    binding.tvNamePl.toString(), //namePl,
                    binding.tvDesciptPl.toString() //descriptPl
                )
            )
        }

        // Выбор меню УДАЛИТЬ ПЛЭЙЛИСТ
        binding.deletePlBottomSheet.setOnClickListener {

        }

    }

    private fun displayTracks (tracks : List<TrackModel>) {
        adapter.tracks.clear()
        adapter.tracks.addAll(tracks)
        adapter.notifyDataSetChanged()
    }

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

    private fun runPlayer(trackId: String) {
        findNavController().navigate(R.id.playerFragment)
    }

    private fun playlistTime (playlist: Playlist) : String {
        var result = 0L
        for (track in playlist.tracksPl) {
            result += track.trackTimeMillis
        }
        return SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(result)
    }

    private fun deleteTrackDialog(trackId: String) = MaterialAlertDialogBuilder(requireActivity())
        .setTitle(R.string.detete_track)
        .setMessage(R.string.really_delete_track)
        .setNeutralButton(getString(R.string.cancel_button)) { _, _ -> }
        .setPositiveButton(getString(R.string.delete_button)) { _, _ ->
            deleteTrackFromPlaylist(trackId, actualPlaylist!!.idPl )
    }

    private fun deleteTrackFromPlaylist(trackId: String, idPl : Int) {
        viewModel.deleteTrackFromPlaylist(trackId, idPl )
        viewModel.getPlaylistById(idPl)
    }

    private fun createPlaylist(playlist: Playlist) : String {
        var sharedPlaylist = playlist.namePl + "\n " +
             playlist.descriptPl + "\n " +
                playlist.countTracks.toString() + "\n "
            for ( i in 1 ..  (playlist.tracksPl.size+1)) {
                sharedPlaylist = sharedPlaylist + "$i. " + playlist.tracksPl[i].artistName + " - "
                        playlist.tracksPl[i].trackName + " - " + playlist.tracksPl[i].trackTimeMillis
            }
        return sharedPlaylist
    }

    companion object {
        fun passArgs(playlistId: Int): Bundle = bundleOf("PLAYLIST" to playlistId)
    }
}