package com.f2prateek.rx.preferences2

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

internal class EnumAdapter<T : Enum<T>>(private val enumClass: Class<T>) :
    RealPreference.Adapter<T> {
    override fun get(
        key: String,
        preferences: SharedPreferences,
        defaultValue: T
    ): T {
        val value = preferences.getString(key, null)
        return if (value.isNullOrBlank()) {
            defaultValue
        } else {
            java.lang.Enum.valueOf(enumClass, value)
        }
    }

    override fun set(key: String, value: T, editor: Editor) {
        editor.putString(key, value.name)
    }
}