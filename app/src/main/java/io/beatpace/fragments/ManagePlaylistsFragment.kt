package io.beatpace.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import io.beatpace.R
import io.beatpace.api.ViewModel
import io.beatpace.fragments.adapters.PlaylistExtendedPickerAdapter
import io.beatpace.fragments.adapters.PlaylistPickerAdapter

class ManagePlaylistsFragment : Fragment() {

    private val viewModel: ViewModel by activityViewModels()
    private lateinit var adapter: PlaylistPickerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = PlaylistPickerAdapter.Builder(viewModel.getPlaylistManager())
            .setDeleteButtonVisible(true)
            .setClickCallback(this::onPlaylistClick)
            .setDeleteClickCallback(this::onDeletePlaylist)
            .build()

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
        val view = layoutInflater.inflate(R.layout.dialog_new_playlist, null)
        val input = view.findViewById<EditText>(R.id.dialog_new_playlist_name)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .setCancelable(true)
            .setTitle(requireContext().getString(R.string.create_new_playlist))
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                val name = input.text.toString().trim()

                if (name.isNotEmpty()) {
                    val id = viewModel.getPlaylistManager().createPlaylist(name)

                    findNavController().navigate(ManagePlaylistsFragmentDirections.actionManagePlaylistsFragmentToEditPlaylistFragment(id))
                } else {
                    Toast.makeText(context, requireContext().getString(R.string.no_playlist_selected), Toast.LENGTH_SHORT).show()
                }
            }
            .create()

        dialog.show()
    }

    private fun onDeletePlaylist(playlistId: Int) {
        val deletedPlaylist = viewModel.getPlaylistManager().getPlaylistById(playlistId) ?: return
        viewModel.getPlaylistManager().deletePlaylistById(playlistId)

        Snackbar.make(requireView().findViewById(R.id.main_layout), requireContext().getString(R.string.playlist_deleted, deletedPlaylist.name), Snackbar.LENGTH_LONG)
            .setAction(requireContext().getString(R.string.undo)) {
                val id = viewModel.getPlaylistManager().createPlaylist(deletedPlaylist.name)

                for (i in 0 until deletedPlaylist.getSize()) {
                    viewModel.getPlaylistManager().addSongToPlaylist(id, deletedPlaylist.getSong(i))
                }

                adapter.submitList(viewModel.getPlaylistManager().getAllPlaylists())
            }.show()
    }
}