package io.beatpace.fragments

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import io.beatpace.R

class EndFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_end, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.end_ok_button).setOnClickListener(this::onOkClick)
        disableBackButton()
    }

    private fun disableBackButton() {
        view?.isFocusableInTouchMode = true
        view?.requestFocus()
        view?.setOnKeyListener {v, keyCode, event ->
            keyCode == KeyEvent.KEYCODE_BACK
        }
    }

    fun showStats() {}

    fun onOkClick(view: View) {
        findNavController().navigate(EndFragmentDirections.actionEndFragmentToHomeFragment())
    }

}