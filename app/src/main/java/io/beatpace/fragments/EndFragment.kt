package io.beatpace.fragments

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import io.beatpace.R

class EndFragment : Fragment() {

    private val args: EndFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_end, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showStats()
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

    fun showStats() {
        val averageSpeed = args.runDistance / args.runDistance
        view?.findViewById<TextView>(R.id.averageSpeedText)?.text = "Average speed: $averageSpeed"
        view?.findViewById<TextView>(R.id.distanceText)?.text = "Distance: ${args.runDistance}"
        view?.findViewById<TextView>(R.id.timeText)?.text = "Time: ${args.runDuration}"
    }

    fun onOkClick(view: View) {
        findNavController().navigate(EndFragmentDirections.actionEndFragmentToHomeFragment())
    }

}