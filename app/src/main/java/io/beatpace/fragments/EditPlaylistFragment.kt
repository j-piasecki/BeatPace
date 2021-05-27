package io.beatpace.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.beatpace.R
import io.beatpace.api.ViewModel
import io.beatpace.fragments.adapters.PlaylistModifierAdapter

class EditPlaylistFragment : Fragment() {

    private val viewModel: ViewModel by activityViewModels()
    private lateinit var adapter: PlaylistModifierAdapter

    private val args: EditPlaylistFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = PlaylistModifierAdapter(
            viewModel.getPlaylistManager().getPlaylistById(args.playlistId)!!,
            viewModel.getPlaylistManager(),
            viewModel.getSongs()
        )

        return inflater.inflate(R.layout.fragment_edit_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.edit_playlist_songs)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        view.findViewById<FloatingActionButton>(R.id.edit_playlist_add_song).setOnClickListener(this::onAddSongClick)

        if (viewModel.getPlaylistManager().getPlaylistById(args.playlistId)!!.getSize() > 0) {
            view.findViewById<TextView>(R.id.edit_playlist_empty_playlist).visibility = View.GONE
        }
    }

    private fun onAddSongClick(view: View) {
        findNavController().navigate(EditPlaylistFragmentDirections.actionEditPlaylistFragmentToNewSongFragment(args.playlistId))
    }
}