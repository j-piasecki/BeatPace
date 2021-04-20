package io.beatpace.api.data.structures

data class DataConfig(private var selectedPlaylistId: Int, private var selectedPace: Double) {

    fun setPace() {}

    fun setPlaylistId(playlistId: Int) {}

    fun getSelectedPace(): Double {
        TODO("Not yet implemented")
    }

    fun getSelectedPlaylistId(): Int {
        TODO("Not yet implemented")
    }

    companion object {
        fun loadSavedData(): DataConfig {
            TODO("Not yet implemented")
        }
    }
}