package io.beatpace.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.beatpace.R

class ConfigurationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_configuration, container, false)
    }

    private fun onPaceChangeClick() {}

    private fun onPlaylistChangeClick() {}

    private fun onStartClick() {}

    private fun onPaceText() {}

    private fun setPlaylistText() {}
}