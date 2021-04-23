package com.f2prateek.rx.preferences2

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

internal object FloatAdapter : RealPreference.Adapter<Float> {
    override fun get(
        key: String,
        preferences: SharedPreferences,
        defaultValue: Float
    ): Float {
        return preferences.getFloat(key, defaultValue)
    }

    override fun set(
        key: String,
        value: Float,
        editor: Editor
    ) {
        editor.putFloat(key, value)
    }
}