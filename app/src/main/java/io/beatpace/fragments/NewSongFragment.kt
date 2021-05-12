package io.beatpace.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.beatpace.R
import io.beatpace.api.ViewModel
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
    }

    private fun onConfirmClick(view: View) {
        val selectedSongs = adapter.getSelectedSongs()
    }
}