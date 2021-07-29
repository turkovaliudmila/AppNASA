package ru.geekbrains.appnasa.material.ui.earth

import com.google.gson.annotations.SerializedName

data class PictureOfEarth(
        @field:SerializedName("identifier") val identifier: String,
        @field:SerializedName("caption") val caption: String,
        @field:SerializedName("image") val image: String
)
