package io.beatpace.api

import com.google.android.exoplayer2.ui.StyledPlayerView
import io.beatpace.api.data.structures.Playlist
import io.beatpace.exceptions.NegativePaceException
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.util.*

class MonitoringServiceTest {

    private lateinit var mockedMusicController: MusicController
    private lateinit var mockedPaceTracker: PaceTracker
    private lateinit var service: MonitoringService

    @Before
    fun setUp() {
        mockedMusicController = mock(MusicController::class.java)
        mockedPaceTracker = mock(PaceTracker::class.java)
        service = buildService(mockedMusicController, mockedPaceTracker)
    }

    private fun buildService(
        musicController: MusicController,
        paceTracker: PaceTracker
    ): MonitoringService {
        val service = MonitoringService()
        service.musicController = musicController
        service.paceTracker = paceTracker
        return service
    }

    @Test
    fun startMonitoring_RunMusicController_Running() {
        val playlist = Playlist("SomePlaylist", 1, LinkedList())
        val pace = 0.0

        service.startMonitoring(playlist, pace)

        verify(mockedMusicController, times(1)).startPlaying(playlist, pace)
    }

    @Test
    fun startMonitoring_RunPaceTracker_Running() {
        val playlist = Playlist("SomePlaylist", 1, LinkedList())
        val pace = 0.0

        service.startMonitoring(playlist, pace)

        verify(mockedPaceTracker, times(1)).startTracking()
    }

    @Test
    fun stopMonitoring_StopMusicController_Stopped() {
        service.stopMonitoring()

        verify(mockedMusicController, times(1)).stopPlaying()
    }

    @Test
    fun stopMonitoring_StopPaceTracker_Stopped() {
        service.stopMonitoring()

        verify(mockedPaceTracker, times(1)).stopTracking()
    }

    @Test
    fun attachPlayerToView_CallPlayerMusicAttachPlayerToViewMethod_Called() {
        val view = mock(StyledPlayerView::class.java)

        service.attachPlayerToView(view)

        verify(mockedMusicController, times(1)).attachToView(view)
    }

    @Test(expected = NegativePaceException::class)
    fun startMonitoring_NegativePace_ThrownNegativePaceException() {
        val mockedPlaylist = mock(Playlist::class.java)
        val pace = -1.0

        service.startMonitoring(mockedPlaylist, pace)
    }
}