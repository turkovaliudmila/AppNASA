package ru.geekbrains.appnasa.material.ui.earth

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface EarthAPI {
    @GET("EPIC/api/natural")
    fun getPictureOfTheEarth(@Query("api_key") apiKey: String, @Query("date") dateOfGetting: String): Call<Array<PictureOfEarth>>
}