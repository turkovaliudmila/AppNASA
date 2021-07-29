package ru.geekbrains.appnasa.material.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.main_activity.*
import ru.geekbrains.appnasa.R
import ru.geekbrains.appnasa.material.ui.api.ApiActivity
import ru.geekbrains.appnasa.material.ui.picture.PictureOfTheDayFragment

class MainActivity : AppCompatActivity() {

    private var isExpanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                    .commitNow()
        }
        setFAB()
    }

    private fun setFAB() {
        setInitialState()

        fab.setOnClickListener {
            if (isExpanded) {
                collapseFab()
            } else {
                expandFAB()
            }
        }
    }

    private fun setInitialState() {
        transparent_background.apply {
            alpha = 0f
        }
        container_option_info_about_planets.apply {
            alpha = 0f
            isClickable = false
        }
        container_option_settings.apply {
            alpha = 0f
            isClickable = false
        }

    }

    private fun expandFAB() {
        isExpanded = true
        ObjectAnimator.ofFloat(plus_imageview, "rotation", 0f, 225f).start()
        ObjectAnimator.ofFloat(container_option_settings, "translationY", -130f).start()
        ObjectAnimator.ofFloat(container_option_info_about_planets, "translationY", -250f).start()

        container_option_info_about_planets.animate()
                .alpha(1f)
                .setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        container_option_info_about_planets.isClickable = true
                        container_option_info_about_planets.setOnClickListener {
                            collapseFab()
                            startActivity(Intent(this@MainActivity, ApiActivity::class.java))
                        }
                    }
                })
        container_option_settings.animate()
                .alpha(1f)
                .setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        container_option_settings.isClickable = true
                        container_option_settings.setOnClickListener {
                            collapseFab()
                            supportFragmentManager.beginTransaction()
                                    .replace(R.id.container, SettingsFragment())
                                    .commitNow()
                        }
                    }
                })
        transparent_background.animate()
                .alpha(0.9f)
                .setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        transparent_background.isClickable = true
                    }
                })
    }

    private fun collapseFab() {
        isExpanded = false
        ObjectAnimator.ofFloat(plus_imageview, "rotation", 0f, -180f).start()
        ObjectAnimator.ofFloat(container_option_info_about_planets, "translationY", 0f).start()
        ObjectAnimator.ofFloat(container_option_settings, "translationY", 0f).start()

        container_option_info_about_planets.animate()
                .alpha(0f)
                .setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        container_option_info_about_planets.isClickable = false
                        container_option_info_about_planets.setOnClickListener(null)
                    }
                })
        container_option_settings.animate()
                .alpha(0f)
                .setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        container_option_settings.isClickable = false
                    }
                })
        transparent_background.animate()
                .alpha(0f)
                .setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        transparent_background.isClickable = false
                    }
                })
    }
}