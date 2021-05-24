package io.beatpace.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.beatpace.R
import io.beatpace.api.PlaylistManager

class PlaylistExtendedPickerAdapter(
    private val playlistManager: PlaylistManager,
    private val clickCallback: ((Int) -> Unit)?,
    private val deleteClickCallback: ((Int) -> Unit)?
) : PlaylistPickerAdapter(playlistManager, clickCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistPickerAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_playlist_pick, parent, false))
    }

    inner class ViewHolder(private val view: View) : PlaylistPickerAdapter.ViewHolder(view) {
        override fun bind(position: Int) {
            val playlist = getItem(position)

            view.findViewById<TextView>(R.id.row_playlist_name).text = playlist.name
            view.findViewById<View>(R.id.row_playlist_layout).isSelected = playlist.Id == selectedId

            view.setOnClickListener {
                clickCallback?.invoke(playlist.Id)
            }

            view.findViewById<View>(R.id.row_playlist_delete).setOnClickListener {
                deleteClickCallback?.invoke(playlist.Id)

                submitList(playlistManager.getAllPlaylists())
            }
        }
    }
}