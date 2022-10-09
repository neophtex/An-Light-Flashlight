package com.series.anlight.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.series.anlight.databinding.ActivityScreenBinding
import com.series.anlight.helper.PrefHelper


class ScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScreenBinding

    private lateinit var prefHelper : PrefHelper

    private var bright_display: Boolean = false
    private var birght_color: Int = 0

    private var brightnessBeforeChanged = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScreenBinding.inflate(layoutInflater)
        val view= binding.root
        setContentView(view)

        setSystemUIVisibility(true)

        prefHelper = PrefHelper(this)
        bright_display = prefHelper.bright_display
        birght_color = prefHelper.bright_color

        binding.screenLight.setBackgroundColor(birght_color)

        val params: WindowManager.LayoutParams = window.attributes

        // Save brightness to restore it later
        brightnessBeforeChanged = params.screenBrightness

        // Set screen brightness to maximum
        params.screenBrightness = 1f
        window.attributes = params

        supportActionBar?.hide()
        binding.colorButton.setOnClickListener {
            ColorPickerDialog
                .Builder(this)
                .setTitle("Pick Theme")
                .setColorShape(ColorShape.SQAURE)
                .setDefaultColor(birght_color)
                .setColorListener { color, colorHex ->
                    // Handle Color Selection, Save color to restore it later
                    prefHelper.bright_color = color

                    binding.screenLight.setBackgroundColor(color)

                }
                .show()
        }

    }

    private fun setSystemUIVisibility(hide: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val window = window.insetsController
            val windows = WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars()
            if (hide) window?.hide(windows) else window?.show(windows)
            // needed for hide, doesn't do anything in show
            window?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else { val view = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            window.decorView.systemUiVisibility = if (hide) view else view.inv()
        }
    }
}

