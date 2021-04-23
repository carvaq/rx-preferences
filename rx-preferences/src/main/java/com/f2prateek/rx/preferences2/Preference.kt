package com.f2prateek.rx.preferences2

import android.content.SharedPreferences
import androidx.annotation.CheckResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Consumer

/** A preference of type [T]. Instances can be created from [RxSharedPreferences].  */
interface Preference<T> {
    /**
     * Converts instances of `T` to be stored and retrieved as Strings in [ ].
     */
    interface Converter<T> {
        /**
         * Deserialize to an instance of `T`. The input is retrieved from [ ][SharedPreferences.getString].
         */
        fun deserialize(serialized: String): T

        /**
         * Serialize the `value` to a String. The result will be used with [ ][SharedPreferences.Editor.putString].
         */
        fun serialize(value: T): String
    }

    /** The key for which this preference will store and retrieve values.  */
    fun key(): String

    /** The value used if none is stored.  */
    fun defaultValue(): T

    /**
     * Retrieve the current value for this preference. Returns [.defaultValue] if no value is
     * set.
     */
    fun get(): T

    /**
     * Change this preference's stored value to `value`.
     */
    fun set(value: T)

    /** Returns true if this preference has a stored value.  */
    val isSet: Boolean

    /** Delete the stored value for this preference, if any.  */
    fun delete()

    /**
     * Observe changes to this preference. The current value or [.defaultValue] will be
     * emitted on first subscribe.
     */
    @CheckResult
    fun asObservable(): Observable<T>

    /**
     * An action which stores a new value for this preference.
     */
    @CheckResult
    fun asConsumer(): Consumer<in T>
}