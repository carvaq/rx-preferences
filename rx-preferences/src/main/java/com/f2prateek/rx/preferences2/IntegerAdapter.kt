package com.f2prateek.rx.preferences2

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

internal object IntegerAdapter : RealPreference.Adapter<Int> {
    override fun get(
        key: String, preferences: SharedPreferences,
        defaultValue: Int
    ): Int {
        return preferences.getInt(key, defaultValue)
    }

    override fun set(
        key: String, value: Int,
        editor: Editor
    ) {
        editor.putInt(key, value)
    }
}