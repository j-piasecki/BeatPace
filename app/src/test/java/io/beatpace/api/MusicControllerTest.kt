package io.beatpace.api

import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import io.beatpace.api.data.structures.Playlist
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

class MusicControllerTest {

    private lateinit var mockedExoPlayer: SimpleExoPlayer
    private lateinit var musicController: MusicController

    @Before
    fun setUp() {
        mockedExoPlayer = mock(SimpleExoPlayer::class.java)
        musicController = MusicController(mockedExoPlayer)
        `when`(mockedExoPlayer.volume).thenCallRealMethod()
        doCallRealMethod().`when`(mockedExoPlayer).volume = anyFloat()
    }

    @Test
    fun attachToView_AssignExoPlayerToViewPlayer_ViewPlayerIsEqualToExoPlayer() {
        val mockedView = mock(StyledPlayerView::class.java)

        musicController.attachToView(mockedView)
        verify(mockedView, times(1)).player = mockedExoPlayer
    }

    @Test
    fun startPlaying_PrepareExoPlayer_ExoPlayerIsPrepared() {
        val mockedPlaylist = mock(Playlist::class.java)
        musicController.startPlaying(mockedPlaylist, 0.0)

        verify(mockedExoPlayer, times(1)).prepare()
    }

    @Test
    fun startPlaying_StartExoPlayer_ExoPlayerPlaysMusic() {
        val mockedPlaylist = mock(Playlist::class.java)
        musicController.startPlaying(mockedPlaylist, 0.0)

        verify(mockedExoPlayer, times(1)).play()
    }

    @Test
    fun stopPlaying_stopExoPlayer_ExoPlayerStopPlayingMusic() {
        musicController.stopPlaying()

        verify(mockedExoPlayer, times(1)).stop()
    }

    @Test
    fun stopPlaying_ReleaseExoPlayer_ExoPlayerIsReleased() {
        musicController.stopPlaying()

        verify(mockedExoPlayer, times(1)).release()
    }
}