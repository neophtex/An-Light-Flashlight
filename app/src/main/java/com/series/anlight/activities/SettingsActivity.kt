package com.series.anlight.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import com.series.anlight.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) // Press Back Icon
        {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    class SettingsFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            val context = preferenceManager.context
            val screen = preferenceManager.createPreferenceScreen(context)

            val about = getPreferenceCategory(context, R.string.settings_about)

            screen.addPreference(about)

            //val support = getPreference(context, R.string.about_support, R.drawable.ic_support)
            val rate = getPreference(context, R.string.about_rate,R.drawable.ic_rate)
            val github = getPreference(context, R.string.about_source, R.drawable.ic_source)
            val feedback = getPreference(context, R.string.about_feedback, R.drawable.ic_feedback)

            about.addPreference(github)
            about.addPreference(rate)
            about.addPreference(feedback)
            //about.addPreference(support)

            preferenceScreen = screen

            rate.setOnPreferenceClickListener {
                openLink(this.getString(R.string.setting_rate))
                return@setOnPreferenceClickListener true
            }
            github.setOnPreferenceClickListener {
                openLink(this.getString(R.string.setting_github))
                return@setOnPreferenceClickListener true
            }
            feedback.setOnPreferenceClickListener {
                feedback(this.getString(R.string.setting_feedback))
                return@setOnPreferenceClickListener true
            }
        }

        private fun openLink(link: String) {
            val uri = Uri.parse(link)
            val linkIntent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(linkIntent)
        }

        private fun feedback(mail: String) {
            val mailIntent = Intent(Intent.ACTION_SEND)
            mailIntent.type = "plain/text"
            mailIntent.type = "message/rfc822"
            mailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mail))
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.setting_feedback_title))
            startActivity(Intent.createChooser(mailIntent, "Send Email"))
        }

        private fun getPreferenceCategory(context: Context, title: Int): PreferenceCategory {
            val category = PreferenceCategory(context)
            category.setTitle(title)
            return category
        }

        private fun getPreference(context: Context, title: Int, icon: Int): Preference {
            val preference = Preference(context)
            preference.setTitle(title)
            preference.setIcon(icon)
            return preference
        }

    }

}