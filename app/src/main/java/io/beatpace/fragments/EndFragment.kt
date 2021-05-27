package io.beatpace.fragments

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import io.beatpace.R
import java.math.BigDecimal
import java.math.RoundingMode

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
        view?.setOnKeyListener { v, keyCode, event ->
            keyCode == KeyEvent.KEYCODE_BACK
        }
    }

    private fun showStats() {
        val durationInSeconds =
            BigDecimal.valueOf(args.runDuration / 1_000.0)
                .setScale(2, RoundingMode.HALF_UP)
        val minutes =
            durationInSeconds.divide(BigDecimal.valueOf(60.0), 0, RoundingMode.FLOOR)
        val seconds = durationInSeconds.subtract(
            minutes.multiply(BigDecimal.valueOf(60))
        ).setScale(0, RoundingMode.FLOOR);

        val distance =
            BigDecimal.valueOf(args.runDistance.toDouble())
                .setScale(2, RoundingMode.HALF_UP)

        val averageSpeed: BigDecimal = try {
            distance.divide(durationInSeconds, 2, RoundingMode.HALF_UP)
        } catch (ex: ArithmeticException) {
            BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
        }

        view?.findViewById<TextView>(R.id.averageSpeedText)?.text = "$averageSpeed m/s"
        view?.findViewById<TextView>(R.id.distanceText)?.text = "$distance meters"
        view?.findViewById<TextView>(R.id.timeText)?.text = "${minutes}m ${seconds}s"
    }

    private fun onOkClick(view: View) {
        findNavController().navigate(EndFragmentDirections.actionEndFragmentToHomeFragment())
    }

}