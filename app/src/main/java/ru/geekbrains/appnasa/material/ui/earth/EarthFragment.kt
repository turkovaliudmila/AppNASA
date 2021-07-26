package ru.geekbrains.appnasa.material.ui.earth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.geekbrains.appnasa.R
import ru.geekbrains.appnasa.databinding.EarthFragmentBinding
import ru.geekbrains.appnasa.material.util.showSnackBar

class EarthFragment : Fragment() {
    private var _binding: EarthFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PictureOfTheEarthViewModel by lazy { ViewModelProvider(this).get(PictureOfTheEarthViewModel::class.java) }
    private val adapter: EarthRecyclerAdapter by lazy { EarthRecyclerAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = EarthFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.recyclerView.adapter = adapter
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
                    binding.main.visibility = View.VISIBLE
                    binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                    adapter.setData(serverResponseData)
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

}