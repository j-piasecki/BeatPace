package io.beatpace.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import io.beatpace.R
import io.beatpace.api.ViewModel

class EditPlaylistFragment : Fragment() {

    private val viewModel: ViewModel by activityViewModels()

    val args: EditPlaylistFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_edit_playlist, container, false)
    }

    private fun onAddSongClick() {}

    private fun onDeletePlaylistClick() {}
}