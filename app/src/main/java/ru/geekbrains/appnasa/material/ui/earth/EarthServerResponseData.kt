package ru.geekbrains.appnasa.material.ui.earth

import com.google.gson.annotations.SerializedName

data class EarthServerResponseData(
        @field:SerializedName("picturesOfTheEarth") val picturesOfTheEarth: Array<PictureOfEarth>
)
