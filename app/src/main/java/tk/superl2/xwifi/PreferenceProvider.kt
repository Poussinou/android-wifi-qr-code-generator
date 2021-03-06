package tk.superl2.xwifi

class PreferenceProvider: com.crossbowffs.remotepreferences.RemotePreferenceProvider("tk.superl2.xwifi.preferences", arrayOf("tk.superl2.xwifi_preferences")) {
    // Only allow the qr_code_resolution key to be read from
    override fun checkAccess(prefName: String, prefKey: String, write: Boolean): Boolean {
        if (write) return false
        return when (prefKey) {
            "theme" -> false
            "qr_code_resolution" -> true
            else -> false
        }
    }
}