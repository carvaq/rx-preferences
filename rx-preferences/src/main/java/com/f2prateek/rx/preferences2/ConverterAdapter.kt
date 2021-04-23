package com.f2prateek.rx.preferences2

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

internal class ConverterAdapter<T>(private val converter: Preference.Converter<T>) :
    RealPreference.Adapter<T> {
    override fun get(
        key: String,
        preferences: SharedPreferences,
        defaultValue: T
    ): T {
        val serialized = preferences.getString(key, null) ?: return defaultValue
        return converter.deserialize(serialized)
    }

    override fun set(key: String, value: T, editor: Editor) {
        val serialized = converter.serialize(value)
        editor.putString(key, serialized)
    }
}