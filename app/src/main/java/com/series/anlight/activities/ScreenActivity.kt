package com.series.anlight.activities

import android.R
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.series.anlight.databinding.ActivityScreenBinding


class ScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScreenBinding

    var flashlightEnabled : Boolean = false

    private var brightnessBeforeChanged = 0f

    private var mColor = "#FFFFFF"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScreenBinding.inflate(layoutInflater)
        val view= binding.root
        setContentView(view)

        setSystemUIVisibility(true)

        supportActionBar?.hide()
        binding.colorButton.setOnClickListener {
            ColorPickerDialog
                .Builder(this)        				// Pass Activity Instance
                .setTitle("Pick Theme")           	// Default "Choose Color"
                .setColorShape(ColorShape.SQAURE)   // Default ColorShape.CIRCLE
                .setDefaultColor(mColor)     // Pass Default Color
                .setColorListener { color, colorHex ->
                    // Handle Color Selection
                    binding.screenLight.setBackgroundColor(color)
                    println(color)
                }
                .show()
        }

        binding.screenLight.setOnClickListener {

            if (!flashlightEnabled) {
                enableFlashlight();
            } else {
                disableFlashlight();
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun enableFlashlight() {
        val params: WindowManager.LayoutParams = window.attributes

        // Save brightness to restore it later
        brightnessBeforeChanged = params.screenBrightness

        // Set screen brightness to maximum
        params.screenBrightness = 1f
        window.attributes = params

        flashlightEnabled = true

        binding.colorButton.isGone = true
    }

    @SuppressLint("SetTextI18n")
    private fun disableFlashlight() {
        val layout: WindowManager.LayoutParams = window.attributes
        layout.screenBrightness = brightnessBeforeChanged
        window.attributes = layout

        flashlightEnabled = false

        binding.colorButton.isVisible = true
    }

    private fun setSystemUIVisibility(hide: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val window = window.insetsController
            val windows = WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars()
            if (hide) window?.hide(windows) else window?.show(windows)
            // needed for hide, doesn't do anything in show
            window?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            val view = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            window.decorView.systemUiVisibility = if (hide) view else view.inv()
        }
    }
}

