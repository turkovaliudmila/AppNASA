package ru.geekbrains.appnasa.material.ui.earth

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.geekbrains.appnasa.BuildConfig
import ru.geekbrains.appnasa.material.ui.picture.PictureOfTheDayData
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PictureOfTheEarthViewModel (
        private val liveDataForViewToObserve: MutableLiveData<PictureOfTheEarthData> = MutableLiveData(),
        private val retrofitImpl: POERetrofitImpl = POERetrofitImpl()
) :
        ViewModel() {

    fun getData(): LiveData<PictureOfTheEarthData> {
        sendServerRequest()
        return liveDataForViewToObserve
    }

    private fun sendServerRequest() {
        liveDataForViewToObserve.value = PictureOfTheEarthData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            //PictureOfTheDayData.Error(Throwable("API key is empty"))
        } else {
            val yesterday = getYesterdayDate()
            retrofitImpl.getRetrofitImpl().getPictureOfTheEarth(apiKey, yesterday).enqueue(object : Callback<Array<PictureOfEarth>> {
                override fun onResponse(
                        call: Call<Array<PictureOfEarth>>,
                        response: Response<Array<PictureOfEarth>>
                ) {
                    if (response.isSuccessful && response.body() != null && (response.body() is Array<PictureOfEarth>)) {
                        val countItems = response.body()?.size?.minus(1)
                        val resultArray: ArrayList<PictureOfTheEarthItem> = ArrayList()
                        val apiKey: String = BuildConfig.NASA_API_KEY
                        for (i in 1..countItems!!) {
                            val imageName = response.body()!!.get(i).image
                            val url = "https://api.nasa.gov/EPIC/archive/natural/$yesterday/jpg/$imageName.jpg?api_key=$apiKey"
                            resultArray.add(PictureOfTheEarthItem(url))
                        }
                        liveDataForViewToObserve.value =
                                PictureOfTheEarthData.Success(resultArray.toList())
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            liveDataForViewToObserve.value =
                                    PictureOfTheEarthData.Error(Throwable("Unidentified error"))
                        } else {
                            liveDataForViewToObserve.value =
                                    PictureOfTheEarthData.Error(Throwable(message))
                        }
                    }
                }

                override fun onFailure(call: Call<Array<PictureOfEarth>>, t: Throwable) {
                    liveDataForViewToObserve.value = PictureOfTheEarthData.Error(t)
                }
            })
        }
    }


    @SuppressLint("NewApi")
    private fun getYesterdayDate() : String {
        var dateYesterday = LocalDate.now().minusDays(1)
        val formattedYesterday = dateYesterday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        return formattedYesterday.toString()
    }
}