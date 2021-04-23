package com.f2prateek.rx.preferences2

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

internal object LongAdapter : RealPreference.Adapter<Long> {
    override fun get(
        key: String,
        preferences: SharedPreferences,
        defaultValue: Long
    ): Long {
        return preferences.getLong(key, defaultValue)
    }

    override fun set(
        key: String,
        value: Long,
        editor: Editor
    ) {
        editor.putLong(key, value)
    }

}