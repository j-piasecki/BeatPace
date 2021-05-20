package io.beatpace.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.beatpace.R
import io.beatpace.api.ViewModel
import kotlin.math.floor

class PaceSelectFragment : Fragment() {

    private val viewModel: ViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pace_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDisplayedPace(viewModel.getDataConfig().getSelectedPace())

        view.findViewById<FloatingActionButton>(R.id.pace_select_confirm_button).setOnClickListener(this::onConfirmClick)
    }

    private fun setDisplayedPace(pace: Double) {
        val integerPicker = view?.findViewById<NumberPicker>(R.id.pace_select_integer) ?: return
        val fractionPicker = view?.findViewById<NumberPicker>(R.id.pace_select_fraction) ?: return

        integerPicker.minValue = 0
        integerPicker.maxValue = 9

        fractionPicker.minValue = 0
        fractionPicker.maxValue = 9

        val integer = floor(pace).toInt()
        val fraction = floor((pace - integer) * 10).toInt()

        integerPicker.value = integer
        fractionPicker.value = fraction
    }

    private fun onConfirmClick(v: View) {
        val integerPicker = view?.findViewById<NumberPicker>(R.id.pace_select_integer) ?: return
        val fractionPicker = view?.findViewById<NumberPicker>(R.id.pace_select_fraction) ?: return

        val pace = integerPicker.value + fractionPicker.value * 0.1

        viewModel.getDataConfig().setPace(pace)

        findNavController().navigate(PaceSelectFragmentDirections.actionPaceSelectFragmentToConfigurationFragment())
    }

}