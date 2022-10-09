package com.series.anlight.helper

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.preference.PreferenceManager
import com.series.anlight.*

class PrefHelper(context: Context) {

    private val preference = PreferenceManager.getDefaultSharedPreferences(context)
    val editor: SharedPreferences.Editor =  preference.edit()

    var flash: Boolean
    get() = preference.getBoolean(FLASH, false)
    set(flash) = editor.putBoolean(FLASH, flash).apply()

    var stroboscope: Boolean
        get() = preference.getBoolean(STROBOSCOPE, false)
        set(stroboscope) = editor.putBoolean(STROBOSCOPE, stroboscope).apply()

    var sos: Boolean
        get() = preference.getBoolean(SOS, false)
        set(sos) = editor.putBoolean(SOS, sos).apply()

    var bright_display: Boolean
        get() = preference.getBoolean(BRIGHT_DISPLAY, false)
        set(bright_display) = editor.putBoolean(BRIGHT_DISPLAY, bright_display).apply()

    var bright_color: Int
        get() = preference.getInt(BRIGHT_COLOR, Color.WHITE)
        set(bright_color) = editor.putInt(BRIGHT_COLOR, bright_color).apply()

}