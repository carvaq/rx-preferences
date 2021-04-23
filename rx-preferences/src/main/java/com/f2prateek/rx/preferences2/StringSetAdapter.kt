package com.f2prateek.rx.preferences2

import android.annotation.TargetApi
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Build.VERSION_CODES
import java.util.*

@TargetApi(VERSION_CODES.HONEYCOMB)
internal object StringSetAdapter : RealPreference.Adapter<Set<String>> {
    override fun get(
        key: String,
        preferences: SharedPreferences, defaultValue: Set<String>
    ): Set<String> {
        return Collections.unmodifiableSet(preferences.getStringSet(key, defaultValue))
    }

    override fun set(
        key: String, value: Set<String>,
        editor: Editor
    ) {
        editor.putStringSet(key, value)
    }

}