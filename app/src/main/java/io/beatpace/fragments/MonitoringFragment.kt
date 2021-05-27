package io.beatpace.fragments

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.exoplayer2.ui.StyledPlayerView
import io.beatpace.R
import io.beatpace.api.MonitoringService
import io.beatpace.api.ViewModel
import io.beatpace.api.data.structures.Playlist
import java.util.*

class MonitoringFragment: Fragment() {

    private var serviceBound = false
    private lateinit var service: MonitoringService
    private val serviceConnection = MonitoringServiceConnection()
    private lateinit var pacePresenter: TextView
    private val viewModel: ViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_monitoring, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disableBackButton()
        pacePresenter = view.findViewById(R.id.pace_presenter)
        startService()
        activity?.bindService(Intent(context, MonitoringService::class.java), serviceConnection, 0)
        view.findViewById<Button>(R.id.monitoring_finish_button).setOnClickListener(this::onFinishClick)
        view.findViewById<View>(R.id.bounce_button_background).animation = AnimationUtils.loadAnimation(requireContext(), R.anim.bounce)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (serviceBound) {
            activity?.unbindService(serviceConnection)
        }
    }

    private fun disableBackButton() {
        view?.isFocusableInTouchMode = true
        view?.requestFocus()
        view?.setOnKeyListener { v, keyCode, event ->
            keyCode == KeyEvent.KEYCODE_BACK
        }
    }

    private fun onFinishClick(view: View) {
        val duration = if (serviceBound) service.getRunDuration() else 0L
        val distance = if (serviceBound) service.getRunDistance() else 0.0

        if (serviceBound) {
            service.stopMonitoring()
            service.stopSelf()
        }

        findNavController().navigate(MonitoringFragmentDirections.actionMonitoringFragmentToEndFragment(duration, distance.toFloat()))
    }

    private fun startService() {
        val servInt = Intent(context, MonitoringService::class.java)
        activity?.startService(servInt)
    }

    fun startMonitoring() {
        val playerView = view?.findViewById<StyledPlayerView>(R.id.player_view)
        service.attachPlayerToView(playerView!!)
        service.setPaceDisplayListener(this::showCurrentPace)

        val dataConfig = viewModel.getDataConfig()
        val playlist = viewModel.getPlaylistManager().getPlaylistById(dataConfig.getSelectedPlaylistId())
        val pace = dataConfig.getSelectedPace()
        service.startMonitoring(playlist!!, pace)
    }

    private fun showCurrentPace(pace: Double, maxPace: Double) {
        pacePresenter.text = "$pace / $maxPace"
    }

    inner class MonitoringServiceConnection: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            serviceBound = true

            val binder = service as MonitoringService.LocalBinder

            this@MonitoringFragment.service = binder.getService()
            this@MonitoringFragment.service.setPaceDisplayListener(this@MonitoringFragment::showCurrentPace)
            this@MonitoringFragment.startMonitoring()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            serviceBound = false
        }
    }

}