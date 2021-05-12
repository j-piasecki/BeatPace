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

class PlaylistModifierAdapter(
    private val playlist: Playlist,
    private val playlistManager: PlaylistManager,
    private val songs: Map<Long, String>
) : ListAdapter<Long, PlaylistModifierAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Long>() {
    override fun areItemsTheSame(oldItem: Long, newItem: Long): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Long, newItem: Long): Boolean {
        return oldItem == newItem
    }
}) {

    init {
        val songs = mutableListOf<Long>()

        for (i in 0 until playlist.getSize())
            songs.add(playlist.getSong(i))

        submitList(songs)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_song, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(position: Int) {
            val songId = getItem(position)

            view.findViewById<TextView>(R.id.row_song_name).apply {
                text = songs[songId]
            }
        }
    }
}