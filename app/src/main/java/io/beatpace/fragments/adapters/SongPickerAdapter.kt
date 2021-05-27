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

/**
 * Adapter that allows to show songs loaded from the device that are not in the playlist and makes it possible to select them
 */

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
    private lateinit var visibleSongs: MutableList<Long>

    //Get list of songs and remove those already in the playlist
    init {
        for (i in 0 until playlist.getSize())
            selectedSongs.add(playlist.getSong(i))

        visibleSongs = songs.keys.toMutableList().apply { this.removeAll(selectedSongs) }
        submitList(visibleSongs)

        selectedSongs.clear()
    }

    /**
     * Creates an android view and wraps it with a view holder to allow modification
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_song_pick, parent, false))
    }

    /**
     * Binds specific item from the list to specified view holder, displaying the content on the screen
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    fun getSelectedSongs(): List<Long> {
        return selectedSongs
    }

    fun getVisibleSongs(): List<Long> {
        return visibleSongs
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        /**
         * Display info about song at specified position
         */
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