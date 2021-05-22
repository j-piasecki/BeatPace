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

class SongPickerAdapter(
    private val playlist: Playlist,
    private val playlistManager: PlaylistManager,
    private val songs: Map<Long, String>
) : ListAdapter<Long, SongPickerAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Long>() {
    override fun areItemsTheSame(oldItem: Long, newItem: Long): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Long, newItem: Long): Boolean {
        return oldItem == newItem
    }
}){

    private val selectedSongs = ArrayList<Long>()

    init {
        for (i in 0 until playlist.getSize())
            selectedSongs.add(playlist.getSong(i))
        submitList(songs.keys.toMutableList().apply { this.removeAll(selectedSongs) })
        selectedSongs.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_song_pick, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    fun getSelectedSongs(): List<Long> {
        return selectedSongs
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(position: Int) {
            val songId = getItem(position)

            view.findViewById<TextView>(R.id.row_song_pick_name).apply {
                text = songs[songId]
            }

            view.isSelected = selectedSongs.contains(songId)
            view.setOnClickListener {
                if (selectedSongs.contains(songId))
                    selectedSongs.remove(songId)
                else
                    selectedSongs.add(songId)

                notifyItemChanged(position)
            }
        }
    }
}