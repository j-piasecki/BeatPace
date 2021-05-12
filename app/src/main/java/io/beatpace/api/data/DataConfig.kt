package io.beatpace.api.data

import android.content.Context
import android.content.SharedPreferences
import io.beatpace.exceptions.NegativePaceException

class DataConfig private constructor(private val preferences: SharedPreferences) {

    private var selectedPlaylistId: Int = 0
    private var selectedPace: Double = 0.0

    init {
        selectedPace = preferences.getFloat(KEY_SELECTED_PACE, 0f).toDouble()
        selectedPlaylistId = preferences.getInt(KEY_SELECTED_PLAYLIST, -1)
    }

    fun setPace(pace: Double) {
        if (pace < 0)
            throw NegativePaceException("Selected pace cannot be negative, $pace given.")

        selectedPace = pace

        preferences.edit()
            .putFloat(KEY_SELECTED_PACE, selectedPace.toFloat())
            .apply()
    }

    fun setPlaylistId(playlistId: Int) {
        selectedPlaylistId = playlistId

        preferences.edit()
            .putInt(KEY_SELECTED_PLAYLIST, selectedPlaylistId)
            .apply()
    }

    fun getSelectedPace(): Double {
        return selectedPace
    }

    fun getSelectedPlaylistId(): Int {
        return selectedPlaylistId
    }

    companion object {
        private const val SHARED_PREFERENCES_NAME = "data_config_preferences"

        private const val KEY_SELECTED_PACE = "selected_pace"
        private const val KEY_SELECTED_PLAYLIST = "selected_playlist"

        fun loadSavedData(context: Context): DataConfig {
            return DataConfig(context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE))
        }
    }
}