package io.beatpace.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.beatpace.R

class MonitoringFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_monitoring, container, false)
    }

    private fun showCurrentPace(someDouble: Double) {}

    private fun onFinishClick() {}

}