package ru.geekbrains.appnasa.material.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.geekbrains.appnasa.R
import ru.geekbrains.appnasa.material.ui.picture.PictureOfTheDayFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                    .commitNow()
        }
    }
}