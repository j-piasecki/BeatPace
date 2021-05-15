package io.beatpace.fragments

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.google.android.exoplayer2.ui.StyledPlayerView
import io.beatpace.R
import io.beatpace.api.MonitoringService
import io.beatpace.api.data.structures.Playlist
import java.util.*

class MonitoringFragment : Fragment() {

    private var serviceBound = false
    private lateinit var service: MonitoringService
    private val serviceConnection = MonitoringServiceConnection()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_monitoring, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startService()
        activity?.bindService(Intent(context, MonitoringService::class.java), serviceConnection, 0)
        view.findViewById<Button>(R.id.monitoring_finish_button).setOnClickListener(this::onFinishClick)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (serviceBound) {
            activity?.unbindService(serviceConnection)
        }
    }

    private fun onFinishClick(view: View) {
        if (serviceBound) {
            service.stopMonitoring()
            service.stopSelf()
        }
        findNavController().navigate(MonitoringFragmentDirections.actionMonitoringFragmentToEndFragment())
    }

    private fun startService() {
        val servInt = Intent(context, MonitoringService::class.java)
        activity?.startService(servInt)
    }

    fun startMonitoring() {
        val playerView = view?.findViewById<StyledPlayerView>(R.id.player_view)
        service.attachPlayerToView(playerView!!)
        service.setPaceDisplayListener(this::showCurrentPace)

        val songs = ArrayList<Long>(listOf(1))
        val playlist = Playlist("New playlist", 1, songs)
        val pace = 10.0
        service.startMonitoring(playlist, pace)
    }

    private fun showCurrentPace(someDouble: Double) {
        TODO()
    }

    inner class MonitoringServiceConnection: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            serviceBound = true

            val binder = service as MonitoringService.LocalBinder

            this@MonitoringFragment.service = binder.getService()
            this@MonitoringFragment.startMonitoring()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            serviceBound = false
        }
    }

}