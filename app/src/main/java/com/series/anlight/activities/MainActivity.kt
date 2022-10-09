package com.series.anlight.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.series.anlight.MORSE
import com.series.anlight.R
import com.series.anlight.Torch
import com.series.anlight.databinding.ActivityMainBinding
import com.series.anlight.helper.PrefHelper
import com.series.anlight.widget.FlashWidgetUpdate


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding;

    private var cameraManager: CameraManager? = null
    private var cameraId: String = ""

    private var torch: Torch = Torch()

    private lateinit var prefHelper : PrefHelper

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
        prefHelper = PrefHelper(this)

        prefHelper.flash = false
        prefHelper.stroboscope = false
        prefHelper.sos = false

        torch.flashLight(cameraManager, this, prefHelper)

        binding.iconSettings.setOnClickListener {
            val morseIntent = Intent(this, SettingsActivity::class.java)
            startActivity(morseIntent)

            return@setOnClickListener
        }

    }

    private fun refresh(){
        //update icon state

        if(!prefHelper.flash) {
            binding.iconFlash.setImageDrawable(this.getDrawable(R.drawable.ic_flashlight_off))
        }
        if (prefHelper.flash) {
            binding.iconFlash.setImageDrawable(this.getDrawable(R.drawable.ic_flashlight_on))
        }
        if(!prefHelper.stroboscope){
            binding.iconStroboscope.setColorFilter(this.getColor(R.color.flash_off))
        }
        if(!prefHelper.sos){
            binding.iconSos.setTextColor(this.getColor(R.color.flash_off))
        }
    }


    private fun torch_listener() {

        binding.iconFlash.setOnClickListener {
            if (!prefHelper.flash) {
                if (prefHelper.sos) {

                    prefHelper.sos = false
                    torch.strobocancel()

                    binding.iconSos.setTextColor(this.getColor(R.color.flash_off))

                }
                if (prefHelper.stroboscope) {
                    prefHelper.stroboscope = false
                    torch.strobocancel()

                    binding.iconStroboscope.setColorFilter(this.getColor(R.color.flash_off))

                }
                binding.iconFlash.setImageDrawable(this.getDrawable(R.drawable.ic_flashlight_on))
                torch.flashLightOn()
                prefHelper.flash = true

                return@setOnClickListener
            }
            if (prefHelper.flash) {

                torch.flashLightOff()
                prefHelper.flash = false

                binding.iconFlash.setImageDrawable(this.getDrawable(R.drawable.ic_flashlight_off))

                return@setOnClickListener
            }
        }

        binding.iconSos.setOnClickListener {
            //val morse = "... --- ..."

            if (!prefHelper.sos) {

                if (prefHelper.stroboscope) {
                    torch.strobocancel()

                    binding.iconStroboscope.setColorFilter(this.getColor(R.color.flash_off))
                    prefHelper.stroboscope = false
                }

                if(prefHelper.flash){
                    prefHelper.flash = false
                }

                binding.iconSos.setTextColor(this.getColor(com.series.anlight.R.color.flash_on))
                binding.iconFlash.setImageDrawable(this.getDrawable(R.drawable.ic_flashlight_off))
                torch.sos(MORSE, binding.iconFlash)

                prefHelper.sos = true

                return@setOnClickListener
            }
            if (prefHelper.sos) {
                torch.strobocancel()

                binding.iconSos.setTextColor(this.getColor(R.color.flash_off))
                binding.iconFlash.setImageDrawable(this.getDrawable(R.drawable.ic_flashlight_off))

                prefHelper.sos = false

                return@setOnClickListener
            }

        }

        binding.iconStroboscope.setOnClickListener {

            if (!prefHelper.stroboscope) {

                if (prefHelper.sos) {
                    torch.strobocancel()

                    binding.iconFlash.setImageDrawable(this.getDrawable(R.drawable.ic_flashlight_off))
                    binding.iconSos.setTextColor(this.getColor(R.color.flash_off))

                    prefHelper.sos = false
                }

                if(prefHelper.flash){
                    prefHelper.flash = false
                }

                torch.strobe(200L, binding.iconFlash)

                binding.iconFlash.setImageDrawable(this.getDrawable(R.drawable.ic_flashlight_off))
                binding.iconStroboscope.setColorFilter(this.getColor(com.series.anlight.R.color.flash_on))

                prefHelper.stroboscope = true

                return@setOnClickListener
            }

            if (prefHelper.stroboscope) {
                torch.strobocancel()

                binding.iconStroboscope.setColorFilter(this.getColor(R.color.flash_off))
                binding.iconFlash.setImageDrawable(this.getDrawable(R.drawable.ic_flashlight_off))

                prefHelper.stroboscope = false

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
                    if (prefHelper.flash) {
                        torch.flashLightOff()
                        binding.iconFlash.setImageDrawable(this.getDrawable(R.drawable.ic_flashlight_off))

                    }
                    if (prefHelper.sos || prefHelper.stroboscope) {
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
    }

    override fun onResume() {
        refresh()
        super.onResume()
        binding.bottomNavigation.menu.getItem(0).isChecked = true
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onBackPressed() {

        torch.flashLightOff()
        prefHelper.flash = false

        super.onBackPressed()
    }
}

