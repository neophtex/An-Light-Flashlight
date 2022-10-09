package com.series.anlight.activities

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.series.anlight.MorseCode
import com.series.anlight.R
import com.series.anlight.Torch
import com.series.anlight.databinding.ActivityMorseBinding
import com.series.anlight.helper.PrefHelper

class MorseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMorseBinding

    private var morse: Boolean = false

    private var cameraManager: CameraManager? = null
    private var cameraId: String = ""

    private var torch: Torch = Torch()

    private var morseCode : String = "... --- ..."

    private var code: MorseCode = MorseCode()

    private lateinit var prefHelper: PrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_morse)

        binding = ActivityMorseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        init()
        morse_listener()

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun init(){
        supportActionBar?.hide()

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        cameraId = cameraManager!!.cameraIdList[0]

        prefHelper = PrefHelper(this)

        torch.flashLight(cameraManager, this, prefHelper)
    }

    private fun morse_listener(){
        binding.morseclick.setOnClickListener {
            if(!morse){
                morseCode = binding.morseText.text.toString()
                if(morseCode == ""){
                    return@setOnClickListener
                }

                torch.decodeToEnglist(code.decodeEnglish(morseCode), binding.morseclick)
                morse = true

                println(code.decode(morseCode))

                return@setOnClickListener
            }
            if(morse){
                torch.strobocancel()
                binding.morseclick.setColorFilter(this.getColor(R.color.flash_off))

                morse = false

                println(morse.toString())

                return@setOnClickListener
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(morse){
            torch.strobocancel()
            binding.morseclick.setColorFilter(this.getColor(R.color.flash_off))
        }
    }
}