package ru.geekbrains.appnasa.material.ui.earth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import ru.geekbrains.appnasa.BuildConfig
import ru.geekbrains.appnasa.R
import ru.geekbrains.appnasa.databinding.EarthFragmentBinding
import ru.geekbrains.appnasa.material.util.showSnackBar
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EarthFragment : Fragment() {
    private var _binding: EarthFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PictureOfTheEarthViewModel by lazy { ViewModelProvider(this).get(PictureOfTheEarthViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = EarthFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getData()
                .observe(viewLifecycleOwner, Observer<PictureOfTheEarthData> { renderData(it) })
    }

    private fun renderData(data: PictureOfTheEarthData) {
        when (data) {
            is PictureOfTheEarthData.Success -> {
                val serverResponseData = data.serverResponseData
                if (serverResponseData.isNullOrEmpty()) {
                    binding.main.showSnackBar(
                            getString(R.string.error_link_is_empty),
                            getString(R.string.button_ok),
                            {  })
                } else {
                    val imageName = serverResponseData.first().image
                    val dateYesterday = getYesterdayDate()
                    val apiKey: String = BuildConfig.NASA_API_KEY
                    val url = "https://api.nasa.gov/EPIC/archive/natural/$dateYesterday/jpg/$imageName.jpg?api_key=$apiKey"
                    binding.imageViewEarth.load(url) {
                        lifecycle(this@EarthFragment)
                        error(R.drawable.ic_baseline_file_download_off_24)
                        placeholder(R.drawable.ic_baseline_file_download_24)
                    }
                    binding.main.visibility = View.VISIBLE
                    binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                }
            }
            is PictureOfTheEarthData.Loading -> {
                binding.main.visibility = View.GONE
                binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
            }
            is PictureOfTheEarthData.Error -> {
                binding.main.visibility = View.VISIBLE
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                binding.main.showSnackBar(
                        data.error.message.toString(),
                        getString(R.string.button_ok),
                        {  })
            }
        }
    }

    @SuppressLint("NewApi")
    private fun getYesterdayDate() : String {
        var dateYesterday = LocalDate.now().minusDays(1)
        val formattedYesterday = dateYesterday.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        return formattedYesterday.toString()
    }

}