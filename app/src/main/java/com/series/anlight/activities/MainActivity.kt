package com.series.anlight.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.get
import com.series.anlight.Torch
import com.series.anlight.R
import com.series.anlight.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding;

    private var flash: Boolean = false
    private var sos: Boolean = false
    private var stroboscope = false

    private var cameraManager: CameraManager? = null
    private var cameraId: String = ""

    private var torch: Torch = Torch()

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        val view = binding.root;
        setContentView(view)

        init()
        torch_listener()
        bottom_listener()

    }

    private fun init() {
        supportActionBar?.hide()

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraId = cameraManager!!.cameraIdList[0]

        torch.flashLight(cameraManager, this)

        binding.iconSettings.setOnClickListener {
            val morseIntent = Intent(this, SettingsActivity::class.java)
            startActivity(morseIntent)

            return@setOnClickListener
        }
    }

    private fun torch_listener() {
        binding.iconFlash.setOnClickListener {
            if (!flash) {
                torch.flashLightOn()

                binding.iconFlash.setImageDrawable(this.getDrawable(R.drawable.ic_flashlight_on))

                flash = true;

                return@setOnClickListener
            }
            if (flash) {
                if (sos) {
                    torch.strobocancel()

                    binding.iconFlash.setImageDrawable(this.getDrawable(R.drawable.ic_flashlight_off))
                    binding.iconSos.setTextColor(this.getColor(R.color.flash_off))

                    flash = false
                }
                if (stroboscope) {
                    torch.strobocancel()

                    binding.iconFlash.setImageDrawable(this.getDrawable(R.drawable.ic_flashlight_off))
                    binding.iconStroboscope.setColorFilter(this.getColor(R.color.flash_off))

                    flash = false
                }
                torch.flashLightOff()

                binding.iconFlash.setImageDrawable(this.getDrawable(R.drawable.ic_flashlight_off))

                flash = false
                sos = false
                stroboscope = false
            }

        }

        binding.iconSos.setOnClickListener {
            val morse = "... --- ..."

            if (!sos) {
                if (stroboscope) {
                    torch.strobocancel()

                    binding.iconStroboscope.setColorFilter(this.getColor(R.color.flash_off))

                    stroboscope = false
                }

                binding.iconSos.setTextColor(this.getColor(com.series.anlight.R.color.flash_on))
                torch.strobe(morse, binding.iconFlash)

                sos = true
                flash = true

                return@setOnClickListener
            }
            if (sos) {
                torch.strobocancel()

                binding.iconSos.setTextColor(this.getColor(R.color.flash_off))
                binding.iconFlash.setImageDrawable(this.getDrawable(R.drawable.ic_flashlight_off))

                sos = false
                flash = false

                return@setOnClickListener
            }

        }

        binding.iconStroboscope.setOnClickListener {
            if (!stroboscope) {
                if (sos) {
                    torch.strobocancel()

                    binding.iconFlash.setImageDrawable(this.getDrawable(R.drawable.ic_flashlight_off))
                    binding.iconSos.setTextColor(this.getColor(R.color.flash_off))

                    sos = false
                }
                torch.frequency(200L, binding.iconFlash)

                binding.iconStroboscope.setColorFilter(this.getColor(com.series.anlight.R.color.flash_on))

                stroboscope = true
                flash = true

                return@setOnClickListener
            }
            if (stroboscope) {
                torch.strobocancel()

                binding.iconStroboscope.setColorFilter(this.getColor(R.color.flash_off))
                binding.iconFlash.setImageDrawable(this.getDrawable(R.drawable.ic_flashlight_off))

                stroboscope = false
                flash = false

                return@setOnClickListener
            }

        }

        binding.iconScreen.setOnClickListener {
            val screenIntent = Intent(this, ScreenActivity::class.java)
            startActivity(screenIntent)

        }
    }

    private fun bottom_listener() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_morse -> {
                    if (flash) {
                        torch.flashLightOff()
                        binding.iconFlash.setImageDrawable(this.getDrawable(R.drawable.ic_flashlight_off))

                    }
                    if (sos || stroboscope) {
                        torch.strobocancel()
                        binding.iconStroboscope.setColorFilter(this.getColor(R.color.flash_off))
                        binding.iconSos.setTextColor(this.getColor(R.color.flash_off))
                    }

                    val morseIntent = Intent(this, MorseActivity::class.java)
                    startActivity(morseIntent)
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        torch.flashLightOff()
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavigation.menu.getItem(0).isChecked = true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        torch.flashLightOff()

        binding.iconFlash.setImageDrawable(this.getDrawable(R.drawable.ic_flashlight_off))

        flash = false
    }

}

