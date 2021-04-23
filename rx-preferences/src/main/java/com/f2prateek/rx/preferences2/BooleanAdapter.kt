package com.f2prateek.rx.preferences2

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

internal object BooleanAdapter : RealPreference.Adapter<Boolean> {
    override fun get(
        key: String,
        preferences: SharedPreferences,
        defaultValue: Boolean
    ): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    override fun set(
        key: String,
        value: Boolean,
        editor: Editor
    ) {
        editor.putBoolean(key, value)
    }
}