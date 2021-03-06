package tk.superl2.xwifi

import android.os.Build
import android.os.Bundle
import android.preference.PreferenceCategory
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.view.MenuItem
import android.widget.Toast

internal const val DEFAULT_QR_CODE_RESOLUTION = "300"
internal const val DEFAULT_CASE_SENSITIVITY = false
internal const val DEFAULT_SORTING_ORDER = true
internal const val DEFAULT_SHOW_OPEN_NETWORKS = true

class SettingsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)

        // Set theme
        if (intent.extras.getBoolean("xposed")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            setThemeFromSharedPrefs(PreferenceManager.getDefaultSharedPreferences(this))
        }

        super.onCreate(savedInstanceState)

        // Display the fragment as the main content. If the xposed boolean stored in the intent is true, show the xposed settings fragment.
        fragmentManager.beginTransaction().replace(android.R.id.content, if (intent.extras.getBoolean("xposed")) XposedSettingsFragment() else SettingsFragment()).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            if (item.itemId == android.R.id.home) {
                finish()
                true
            } else super.onOptionsItemSelected(item)
}

class SettingsFragment: PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences)

        if (Build.VERSION.SDK_INT >= 21) {
            findPreference("theme").setOnPreferenceChangeListener { _, newValue ->
                AppCompatDelegate.setDefaultNightMode(getThemeFromPreferenceString(newValue as String))
                Toast.makeText(activity.applicationContext, getString(R.string.theme_restart_message), Toast.LENGTH_SHORT).show()
                activity.recreate()
                true
            }
        } else {
            (findPreference("display") as PreferenceCategory).removePreference(findPreference("theme"))
        }
    }
}

class XposedSettingsFragment: PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.xposed_preferences)
    }
}