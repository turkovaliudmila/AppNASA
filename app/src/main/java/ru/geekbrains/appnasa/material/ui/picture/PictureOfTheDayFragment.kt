package ru.geekbrains.appnasa.material.ui.picture

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.google.android.material.bottomsheet.BottomSheetBehavior

import ru.geekbrains.appnasa.R
import ru.geekbrains.appnasa.databinding.MainFragmentBinding
import ru.geekbrains.appnasa.material.ui.MainActivity
import ru.geekbrains.appnasa.material.util.showSnackBar


class PictureOfTheDayFragment : Fragment() {
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val viewModel: PictureOfTheDayViewModel by lazy { ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getData()
            .observe(viewLifecycleOwner, Observer<PictureOfTheDayData> { renderData(it) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
        setBottomSheetBehavior(binding.bottomSheet.bottomSheetContainer)
        setBottomAppBar(view)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_settings -> {
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, SettingsFragment())?.addToBackStack(null)?.commit()
            }
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
        setHasOptionsMenu(true)
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                val title = serverResponseData.title
                val explanation = serverResponseData.explanation
                binding.main.visibility = View.VISIBLE
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                if (url.isNullOrEmpty()) {

                    binding.main.showSnackBar(
                        getString(R.string.error_link_is_empty),
                        getString(R.string.button_ok),
                        {  })
                } else {
                    binding.imageView.load(url) {
                        lifecycle(this@PictureOfTheDayFragment)
                        error(R.drawable.ic_baseline_file_download_off_24)
                        placeholder(R.drawable.ic_baseline_file_download_24)
                    }
                    binding.bottomSheet.bottomSheetDescription.text = explanation
                    binding.bottomSheet.bottomSheetDescriptionHeader.text = title
                }
            }
            is PictureOfTheDayData.Loading -> {
                binding.main.visibility = View.GONE
                binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
            }
            is PictureOfTheDayData.Error -> {
                binding.main.visibility = View.VISIBLE
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                binding.main.showSnackBar(
                    data.error.message.toString(),
                    getString(R.string.button_ok),
                    {  })
            }
        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }

    fun Fragment.replaceFragment(fragment: Fragment) {
        activity?.supportFragmentManager?.apply {
            beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack("Settings")
                .commitNow()
        }
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
    }
}