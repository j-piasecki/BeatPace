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
import java.math.BigDecimal
import java.math.RoundingMode

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
        if (selectedPlaylist == -1 || viewModel.getPlaylistManager().getPlaylistById(selectedPlaylist) == null) {
            Toast.makeText(context, requireContext().getString(R.string.no_playlist_selected), Toast.LENGTH_SHORT).show()
        } else if (viewModel.getPlaylistManager().getPlaylistById(selectedPlaylist)?.getSize() == 0) {
            Toast.makeText(context, requireContext().getString(R.string.playlist_cannot_be_empty), Toast.LENGTH_SHORT).show()
        } else {
            findNavController().navigate(ConfigurationFragmentDirections.actionConfigurationFragmentToMonitoringFragment())
        }
    }

    private fun setPaceText(pace: Double) {
        view?.findViewById<TextView>(R.id.configuration_current_speed)?.text =
            BigDecimal.valueOf(pace).setScale(1, RoundingMode.HALF_UP).toString()
    }

    private fun setPlaylistText(playlistId: Int) {
        view?.findViewById<TextView>(R.id.configuration_current_playlist)?.text =
            if (playlistId < 0)
                requireContext().getString(R.string.no_playlist_selected)
            else
                viewModel.getPlaylistManager().getPlaylistById(playlistId)?.name
    }
}