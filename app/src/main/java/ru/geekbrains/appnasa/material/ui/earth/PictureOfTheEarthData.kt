package ru.geekbrains.appnasa.material.ui.earth

sealed class PictureOfTheEarthData {
    data class Success(val serverResponseData: List<PictureOfTheEarthItem>) : PictureOfTheEarthData()
    data class Error(val error: Throwable) : PictureOfTheEarthData()
    data class Loading(val progress: Int?) : PictureOfTheEarthData()
}
