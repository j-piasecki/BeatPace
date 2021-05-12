package io.beatpace.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.beatpace.R
import io.beatpace.api.PlaylistManager
import io.beatpace.api.data.structures.Playlist

class PlaylistPickerAdapter(
    private val playlistManager: PlaylistManager,
    private val clickCallback: (Int) -> Unit
) : ListAdapter<Playlist, PlaylistPickerAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Playlist>() {
    override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return oldItem.Id == newItem.Id
    }

    override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return oldItem.Id == newItem.Id && oldItem.name == newItem.name
    }
}) {

    private var selectedId = -1

    init {
        submitList(playlistManager.getAllPlaylists())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_playlist_pick, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    fun setSelectedPlaylist(id: Int) {
        val previousSelected = selectedId
        selectedId = id

        val newPosition = currentList.indexOfFirst { it.Id == id }

        if (newPosition >= 0)
            notifyItemChanged(newPosition)

        val oldPosition = currentList.indexOfFirst { it.Id == previousSelected }

        if (oldPosition >= 0)
            notifyItemChanged(oldPosition)
    }

    fun getSelectedPlaylistId(): Int {
        return selectedId
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(position: Int) {
            val playlist = getItem(position)

            view.findViewById<TextView>(R.id.row_playlist_name).apply {
                isSelected = playlist.Id == selectedId
                text = playlist.name
            }

            view.setOnClickListener {
                clickCallback.invoke(playlist.Id)
            }
        }
    }
}