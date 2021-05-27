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
import io.beatpace.api.data.structures.Playlist
import io.beatpace.fragments.adapters.SongPickerAdapter

class NewSongFragment : Fragment() {

    private val viewModel: ViewModel by activityViewModels()
    private lateinit var adapter: SongPickerAdapter

    private val args: NewSongFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = SongPickerAdapter(
            viewModel.getPlaylistManager().getPlaylistById(args.playlistId)!!,
            viewModel.getPlaylistManager(),
            viewModel.getSongs()
        )

        return inflater.inflate(R.layout.fragment_new_song, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.new_song_input)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        view.findViewById<FloatingActionButton>(R.id.new_song_confirm).setOnClickListener(this::onConfirmClick)

        if (adapter.getVisibleSongs().size > 0) {
            view.findViewById<TextView>(R.id.add_song_no_more_songs_text).visibility = View.GONE
        }
    }

    private fun onConfirmClick(view: View) {
        val playlistId = args.playlistId
        val playlist = viewModel.getPlaylistManager().getPlaylistById(playlistId)
        if (playlist != null) {
            val songsAlreadyInPlaylist = getSongsFromPlaylist(playlist)
            val selectedSongs = adapter.getSelectedSongs()
            val list = ArrayList(selectedSongs).apply {
                removeAll(songsAlreadyInPlaylist)
            }
            val playlistManager = viewModel.getPlaylistManager()

            list.forEach {
                playlistManager.addSongToPlaylist(playlistId, it)
            }
        }

        findNavController().navigateUp()
    }

    private fun getSongsFromPlaylist(playlist: Playlist): List<Long> {
        val songsList = ArrayList<Long>()
        for (i in 0 until playlist.getSize()) {
            val id = playlist.getSong(i)
            songsList.add(id)
        }

        return songsList
    }
}