package io.beatpace.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.beatpace.R
import io.beatpace.api.ViewModel
import io.beatpace.fragments.adapters.PlaylistPickerAdapter

class PlaylistSelectFragment : Fragment() {

    private val viewModel: ViewModel by activityViewModels()
    private lateinit var adapter: PlaylistPickerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = PlaylistPickerAdapter.Builder(viewModel.getPlaylistManager())
            .setDeleteButtonVisible(false)
            .setClickCallback(this::onPlaylistSelected)
            .build()

        adapter.setSelectedPlaylist(viewModel.getDataConfig().getSelectedPlaylistId())

        return inflater.inflate(R.layout.fragment_playlist_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.playlist_select_input)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        view.findViewById<FloatingActionButton>(R.id.playlist_select_confirm).setOnClickListener(this::onConfirmClick)
    }

    private fun onPlaylistSelected(selectedPlaylist: Int) {
        adapter.setSelectedPlaylist(selectedPlaylist)
    }

    private fun onConfirmClick(view: View) {
        viewModel.getDataConfig().setPlaylistId(adapter.getSelectedPlaylistId())

        findNavController().navigate(PlaylistSelectFragmentDirections.actionPlaylistSelectFragmentToConfigurationFragment2())
    }

}