package io.beatpace.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.beatpace.R
import io.beatpace.api.ViewModel
import io.beatpace.api.data.structures.Playlist

class ConfigurationFragment : Fragment() {

    private val viewModel: ViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_configuration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.configuration_set_playlist).setOnClickListener(this::onPlaylistChangeClick)
        view.findViewById<Button>(R.id.configuration_set_speed).setOnClickListener(this::onPaceChangeClick)
        view.findViewById<FloatingActionButton>(R.id.configuration_start_button).setOnClickListener(this::onStartClick)

        setPaceText(viewModel.getDataConfig().getSelectedPace())
        setPlaylistText(viewModel.getDataConfig().getSelectedPlaylistId())
    }

    private fun onPaceChangeClick(view: View) {
        findNavController().navigate(ConfigurationFragmentDirections.actionConfigurationFragmentToPaceSelectFragment())
    }

    private fun onPlaylistChangeClick(view: View) {
        findNavController().navigate(ConfigurationFragmentDirections.actionConfigurationFragmentToPlaylistSelectFragment())
    }

    private fun onStartClick(view: View) {
        val selectedPlaylist = viewModel.getDataConfig().getSelectedPlaylistId()
        if (selectedPlaylist >= 0 && viewModel.getPlaylistManager().getPlaylistById(selectedPlaylist) != null) {
            findNavController().navigate(ConfigurationFragmentDirections.actionConfigurationFragmentToMonitoringFragment())
        } else {
            Toast.makeText(context, requireContext().getString(R.string.no_playlist_selected), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setPaceText(pace: Double) {
        view?.findViewById<TextView>(R.id.configuration_current_speed)?.text = pace.toString()
    }

    private fun setPlaylistText(playlistId: Int) {
        view?.findViewById<TextView>(R.id.configuration_current_playlist)?.text =
            if (playlistId < 0)
                requireContext().getString(R.string.no_playlist_selected)
            else
                viewModel.getPlaylistManager().getPlaylistById(playlistId)?.name
    }
}