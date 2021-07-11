package ru.geekbrains.appnasa.material.ui.picture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.geekbrains.appnasa.R
import ru.geekbrains.appnasa.databinding.SettingsLayoutBinding

class SettingsFragment : Fragment() {

    private var _binding: SettingsLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = SettingsLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.RadioButtonMarsTheme.setOnClickListener {
            activity?.apply {
                setTheme(R.style.MarsTheme)
                recreate()
            }
        }

        binding.RadioButtonMoonTheme.setOnClickListener {
            activity?.apply {
                setTheme(R.style.MoonTheme)
                recreate()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}