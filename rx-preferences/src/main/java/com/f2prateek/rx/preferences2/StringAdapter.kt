package com.f2prateek.rx.preferences2

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

internal object StringAdapter : RealPreference.Adapter<String> {
    override fun get(
        key: String, preferences: SharedPreferences,
        defaultValue: String
    ): String {
        return preferences.getString(key, defaultValue)!!
    }

    override fun set(
        key: String, value: String,
        editor: Editor
    ) {
        editor.putString(key, value)
    }

}