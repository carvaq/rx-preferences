package com.f2prateek.rx.preferences2

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Build.VERSION_CODES
import androidx.annotation.CheckResult
import androidx.annotation.RequiresApi
import io.reactivex.rxjava3.core.Observable

/** A factory for reactive [Preference] objects.  */
class RxSharedPreferences private constructor(private val preferences: SharedPreferences) {
    private val keyChanges: Observable<String> = Observable.create<String> { emitter ->
        val listener = OnSharedPreferenceChangeListener { _, key ->
            if (key == null) {
                emitter.onNext(NULL_KEY_EMISSION)
            } else {
                emitter.onNext(key)
            }
        }
        emitter.setCancellable { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
        preferences.registerOnSharedPreferenceChangeListener(listener)
    }.share()

    /** Create a boolean preference for `key`. Default is `false`.  */
    @CheckResult
    fun getBoolean(key: String): Preference<Boolean> {
        return getBoolean(key, DEFAULT_BOOLEAN)
    }

    /** Create a boolean preference for `key` with a default of `defaultValue`.  */
    @CheckResult
    fun getBoolean(key: String, defaultValue: Boolean): Preference<Boolean> {
        return RealPreference(
            preferences,
            key,
            defaultValue,
            BooleanAdapter,
            keyChanges
        )
    }

    /** Create an enum preference for `key` with a default of `defaultValue`.  */
    @CheckResult
    fun <T : Enum<T>> getEnum(
        key: String, defaultValue: T,
        enumClass: Class<T>
    ): Preference<T> {
        return RealPreference(preferences, key, defaultValue, EnumAdapter(enumClass), keyChanges)
    }

    /** Create a float preference for `key`. Default is `0`.  */
    @CheckResult
    fun getFloat(key: String): Preference<Float> {
        return getFloat(key, DEFAULT_FLOAT)
    }

    /** Create a float preference for `key` with a default of `defaultValue`.  */
    @CheckResult
    fun getFloat(key: String, defaultValue: Float): Preference<Float> {
        return RealPreference(
            preferences,
            key,
            defaultValue,
            FloatAdapter,
            keyChanges
        )
    }

    /** Create an integer preference for `key`. Default is `0`.  */
    @CheckResult
    fun getInteger(key: String): Preference<Int> {
        return getInteger(key, DEFAULT_INTEGER)
    }

    /** Create an integer preference for `key` with a default of `defaultValue`.  */
    @CheckResult
    fun getInteger(key: String, defaultValue: Int): Preference<Int> {
        return RealPreference(
            preferences,
            key,
            defaultValue,
            IntegerAdapter,
            keyChanges
        )
    }

    /** Create a long preference for `key`. Default is `0`.  */
    @CheckResult
    fun getLong(key: String): Preference<Long> {
        return getLong(key, DEFAULT_LONG)
    }

    /** Create a long preference for `key` with a default of `defaultValue`.  */
    @CheckResult
    fun getLong(key: String, defaultValue: Long): Preference<Long> {
        return RealPreference(
            preferences,
            key,
            defaultValue,
            LongAdapter,
            keyChanges
        )
    }

    /**
     * Create a preference for type `T` for `key` with a default of `defaultValue`.
     */
    @CheckResult
    fun <T> getObject(
        key: String,
        defaultValue: T, converter: Preference.Converter<T>
    ): Preference<T> {
        return RealPreference(
            preferences, key, defaultValue,
            ConverterAdapter(converter), keyChanges
        )
    }

    /** Create a string preference for `key`. Default is `""`.  */
    @CheckResult
    fun getString(key: String): Preference<String> {
        return getString(key, DEFAULT_STRING)
    }

    /** Create a string preference for `key` with a default of `defaultValue`.  */
    @CheckResult
    fun getString(key: String, defaultValue: String): Preference<String> {
        return RealPreference(
            preferences,
            key,
            defaultValue,
            StringAdapter,
            keyChanges
        )
    }

    /**
     * Create a string set preference for `key`. Default is an empty set. Note that returned set
     * value will always be unmodifiable.
     */
    @RequiresApi(VERSION_CODES.HONEYCOMB)
    @CheckResult
    fun getStringSet(key: String): Preference<Set<String>> {
        return getStringSet(key, emptySet())
    }

    /** Create a string set preference for `key` with a default of `defaultValue`.  */
    @RequiresApi(VERSION_CODES.HONEYCOMB)
    @CheckResult
    fun getStringSet(
        key: String,
        defaultValue: Set<String>
    ): Preference<Set<String>> {
        return RealPreference(
            preferences,
            key,
            defaultValue,
            StringSetAdapter,
            keyChanges
        )
    }

    fun clear() {
        preferences.edit().clear().apply()
    }

    companion object {
        private const val DEFAULT_FLOAT = 0f
        private const val DEFAULT_INTEGER = 0
        private const val DEFAULT_BOOLEAN = false
        private const val DEFAULT_LONG = 0L
        private const val DEFAULT_STRING = ""
        const val NULL_KEY_EMISSION = "null_key_emission"

        /** Create an instance of [RxSharedPreferences] for `preferences`.  */
        @JvmStatic
        @CheckResult
        fun create(preferences: SharedPreferences): RxSharedPreferences {
            return RxSharedPreferences(preferences)
        }
    }

}