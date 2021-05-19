package io.beatpace.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController

import androidx.navigation.fragment.findNavController
import io.beatpace.R
import io.beatpace.api.MonitoringService
import io.beatpace.api.PaceTracker
import io.beatpace.api.ViewModel


class HomeFragment : Fragment() {

    private val viewModel: ViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (MonitoringService.isRunning) {
            findNavController().navigate(R.id.monitoringFragment)
        }

        view.findViewById<Button>(R.id.home_start_button).setOnClickListener(this::onStartClick)
        view.findViewById<Button>(R.id.home_playlists_button).setOnClickListener(this::onPlaylistsClick)
    }

    private fun onStartClick(view: View) {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToConfigurationFragment())
    }

    private fun onPlaylistsClick(view: View) {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToManagePlaylistsFragment())
    }
}