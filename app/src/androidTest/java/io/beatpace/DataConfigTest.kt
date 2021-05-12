package io.beatpace

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.beatpace.api.data.DataConfig
import io.beatpace.exceptions.NegativePaceException

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.BeforeClass
import java.lang.IllegalArgumentException

@RunWith(AndroidJUnit4::class)
class DataConfigTest {

    companion object {
        lateinit var dataConfig: DataConfig

        @BeforeClass
        @JvmStatic
        fun prepareDataConfig() {
            val appContext = InstrumentationRegistry.getInstrumentation().targetContext

            dataConfig = DataConfig.loadSavedData(appContext)
        }
    }

    @Test
    fun dataConfig_TestSetSelectedPlaylist_Updated() {
        dataConfig.setPlaylistId(2)

        assertEquals(2, dataConfig.getSelectedPlaylistId())
    }

    @Test
    fun dataConfig_TestSetSelectedPace_Updated() {
        dataConfig.setPace(5.0)

        assertEquals(5.0, dataConfig.getSelectedPace(), 1e-5)
    }

    @Test(expected = NegativePaceException::class)
    fun dataConfig_TestSetSelectedPaceNegative_ThrowsException() {
        dataConfig.setPace(-1.0)
    }
}