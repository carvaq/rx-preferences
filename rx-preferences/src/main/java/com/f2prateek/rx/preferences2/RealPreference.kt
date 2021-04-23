package com.f2prateek.rx.preferences2

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import androidx.annotation.CheckResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Consumer

internal class RealPreference<T>(
    private val preferences: SharedPreferences,
    private val key: String,
    private val defaultValue: T,
    private val adapter: Adapter<T>,
    keyChanges: Observable<String>
) : Preference<T> {
    /** Stores and retrieves instances of `T` in [SharedPreferences].  */
    internal interface Adapter<T> {
        /**
         * Retrieve the value for `key` from `preferences`, or `defaultValue`
         * if the preference is unset, or was set to `null`.
         */
        fun get(
            key: String,
            preferences: SharedPreferences,
            defaultValue: T
        ): T

        /**
         * Store non-null `value` for `key` in `editor`.
         *
         *
         * Note: Implementations **must not** call `commit()` or `apply()` on
         * `editor`.
         */

        fun set(key: String, value: T, editor: Editor)
    }

    private val values: Observable<T>

    init {
        values = keyChanges //
            .filter { changedKey -> key == changedKey } //
            .startWithItem("<init>") // Dummy value to trigger initial load.
            .map { s ->
                if (s == RxSharedPreferences.NULL_KEY_EMISSION) {
                    defaultValue
                } else {
                    get()
                }
            }
    }

    override fun key(): String {
        return key
    }

    override fun defaultValue(): T {
        return defaultValue
    }

    @Synchronized
    override fun get(): T {
        return adapter.get(key, preferences, defaultValue)
    }

    override fun set(value: T) {
        val editor = preferences.edit()
        adapter.set(key, value, editor)
        editor.apply()
    }

    override val isSet: Boolean
        get() = preferences.contains(key)

    @Synchronized
    override fun delete() {
        preferences.edit().remove(key).apply()
    }

    @CheckResult
    override fun asObservable(): Observable<T> {
        return values
    }

    @CheckResult
    override fun asConsumer(): Consumer<in T> {
        return Consumer { value -> set(value) }
    }

}