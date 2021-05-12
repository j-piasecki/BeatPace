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

class ManagePlaylistsFragment : Fragment() {

    private val viewModel: ViewModel by activityViewModels()
    private lateinit var adapter: PlaylistPickerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = PlaylistPickerAdapter(viewModel.getPlaylistManager(), this::onPlaylistClick)

        return inflater.inflate(R.layout.fragment_manage_playlists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.manage_playlists_playlist_selection)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        view.findViewById<FloatingActionButton>(R.id.manage_playlist_add_playlist_button).setOnClickListener(this::onAddPlaylistClick)
    }

    private fun onPlaylistClick(playlistId: Int) {
        findNavController().navigate(ManagePlaylistsFragmentDirections.actionManagePlaylistsFragmentToEditPlaylistFragment(playlistId))
    }

    private fun onAddPlaylistClick(view: View) {
        TODO("Tworzenie nowych playlist")
    }
}