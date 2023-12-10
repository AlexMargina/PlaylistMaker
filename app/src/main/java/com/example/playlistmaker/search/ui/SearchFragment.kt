package com.example.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.SearchState
import com.example.playlistmaker.search.domain.TrackModel
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val searchedSong = ArrayList<TrackModel>()
    private val clickedSong = ArrayList<TrackModel>()
    private val searchMusicAdapter = SearchMusicAdapter(searchedSong) { trackClickListener(it) }
    private val clickedMusicAdapter = SearchMusicAdapter(clickedSong) { trackClickListener(it) }
    private lateinit var trackClickListener: (TrackModel) -> Unit
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchViewModel>()
    private var searchText = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.stateLiveData().observe(viewLifecycleOwner) {
            updateScreen(it)
        }


        // обработка нажатия на кнопку Done
        binding.inputSearchText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchDebounce(searchText)
                binding.groupSearched.isVisible = true
            }
            false
        }

        // обработка нажатия на кнопку Обновить
        binding.inetProblem.setOnClickListener {
            viewModel.searchDebounce(searchText)
        }

        // обработка нажатия на кнопку Очистить историю
        binding.clearHistory.setOnClickListener {
            viewModel.clearHistory()
            viewModel.getTracksHistory()
            binding.groupClicked.isVisible = false
            binding.recyclerViewClicked.adapter?.notifyDataSetChanged()
        }


        // при нажатии на крестик очистки поля поиска:

        binding.iconClearSearch.setOnClickListener {
            searchedSong.clear()
            viewModel.getTracksHistory()
            searchMusicAdapter.notifyDataSetChanged()
            binding.recyclerViewSearch.adapter?.notifyDataSetChanged()
            binding.recyclerViewClicked.adapter?.notifyDataSetChanged()
            binding.inputSearchText.setText("")
            binding.imageCrash.isVisible = false
            binding.inetProblem.isVisible = false
            binding.iconClearSearch.isVisible = false
            binding.groupClicked.isVisible = true
            binding.recyclerViewClicked.isVisible = true
        }


        //     Формирование списка найденных песен в recyclerViewSearch и в recyclerViewClicked    *//
        binding.recyclerViewSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSearch.adapter = searchMusicAdapter
        binding.recyclerViewClicked.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewClicked.adapter = clickedMusicAdapter


        // изменения текста в поле поиска. Привязка обьекта TextWatcher
        fun textWatcher() = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.iconClearSearch.isVisible = false
                binding.iconClearSearch.isVisible = ! s.isNullOrEmpty()
                binding.groupClicked.isVisible = false
                searchText = s.toString()
                viewModel.searchDebounce(searchText)
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        provideTextWatcher(textWatcher())


        trackClickListener = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope, false
        ) { track ->
            Log.d("MAALMI_SearchFrag", "1. Нажали на track=${track}")
            viewModel.addTrackToHistory(track)
            runPlayer(track.trackId.toString())
        }

        binding.inputSearchText.requestFocus()
        // КОНЕЦ  fun onViewCreated
    }


    fun provideTextWatcher(textWatcher: TextWatcher) {
        binding.inputSearchText.apply {
            addTextChangedListener(textWatcher)
            setText(searchText)
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus && this.text.isEmpty())
                    viewModel.getTracksHistory()
                else
                    binding.groupClicked.isVisible = false
            }
        }
    }


    private fun runPlayer(trackId: String) {
        val playerIntent = Intent(requireContext(), PlayerActivity::class.java)
        playerIntent.putExtra("trackId", trackId)
        startActivity(playerIntent)
    }


    private fun updateScreen(state: SearchState) {
        binding.apply {

            Log.d("MAALMI", state.toString())
            when (state) {
                is SearchState.Content -> {
                    Log.d("MAALMI", "Выполняем Content")
                    searchedSong.clear()
                    searchedSong.addAll(state.tracks as ArrayList<TrackModel>)
                    groupClicked.isVisible = false
                    groupProgress.isVisible = false
                    groupSearched.isVisible = true
                    recyclerViewSearch.isVisible = true
                    imageCrash.isVisible = false
                    inetProblem.isVisible = false
                    searchMusicAdapter.notifyDataSetChanged()
                }

                is SearchState.Error -> {
                    Log.d("MAALMI", "Выполняем Error")
                    groupProgress.isVisible = false
                    groupSearched.isVisible = true
                    recyclerViewSearch.isVisible = false
                    imageCrash.isVisible = false
                    inetProblem.isVisible = true
                }

                is SearchState.Empty -> {
                    Log.d("MAALMI", "Выполняем Empty")
                    groupProgress.isVisible = false
                    groupSearched.isVisible = true
                    recyclerViewSearch.isVisible = false
                    inetProblem.isVisible = false
                    imageCrash.isVisible = true
                    updateButton.isVisible = false
                }

                is SearchState.Loading -> {
                    Log.d("MAALMI", "Выполняем Loading")
                    groupClicked.isVisible = false
                    groupSearched.isVisible = false
                    progressBar.isVisible = true
                    groupProgress.isVisible = true

                }

                is SearchState.ContentHistory -> {
                    Log.d("MAALMI", "Выполняем ContentHistoryList")
                    groupClicked.isVisible = true
                    groupProgress.isVisible = false
                    groupSearched.isVisible = false
                    recyclerViewClicked.isVisible = true
                    clickedSong.clear()
                    clickedSong.addAll(state.tracks)
                    clickedMusicAdapter.notifyDataSetChanged()
                }

                is SearchState.EmptyHistoryList -> {
                    Log.d("MAALMI", "Выполняем EmptyHistoryList")
                    groupClicked.isVisible = false
                }

                else -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 300L
    }
}