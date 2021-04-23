package com.f2prateek.rx.preferences2

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class) //
@SuppressLint("NewApi", "ApplySharedPref") //
class PreferenceTest {
    @Rule
    val observerRule = RecordingObserver.Rule()
    private val pointConverter = PointPreferenceConverter()
    private lateinit var preferences: SharedPreferences
    private lateinit var rxPreferences: RxSharedPreferences

    @Before
    fun setUp() {
        preferences =
            PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext())
        preferences.edit().clear().commit()
        rxPreferences = RxSharedPreferences.create(preferences)
    }

    @Test
    fun key() {
        val preference = rxPreferences.getString("foo")
        Assertions.assertThat(preference.key()).isEqualTo("foo")
    }

    @Test
    fun defaultDefaultValue() {
        Assertions.assertThat(rxPreferences.getBoolean("foo1").defaultValue()).isFalse
        Assertions.assertThat(rxPreferences.getFloat("foo3").defaultValue()).isZero
        Assertions.assertThat(rxPreferences.getInteger("foo4").defaultValue()).isZero
        Assertions.assertThat(rxPreferences.getLong("foo5").defaultValue()).isZero
        Assertions.assertThat(rxPreferences.getString("foo6").defaultValue()).isEqualTo("")
        Assertions.assertThat(rxPreferences.getStringSet("foo7").defaultValue()).isEmpty()
    }

    @Test
    fun defaultValue() {
        Assertions.assertThat(rxPreferences.getBoolean("foo1", false).defaultValue())
            .isEqualTo(false)
        Assertions.assertThat(
            rxPreferences.getEnum("foo2", Roshambo.ROCK, Roshambo::class.java).defaultValue()
        ).isEqualTo(Roshambo.ROCK)
        Assertions.assertThat(rxPreferences.getFloat("foo3", 1f).defaultValue()).isEqualTo(1f)
        Assertions.assertThat(rxPreferences.getInteger("foo4", 1).defaultValue()).isEqualTo(1)
        Assertions.assertThat(rxPreferences.getLong("foo5", 1L).defaultValue()).isEqualTo(1L)
        Assertions.assertThat(rxPreferences.getString("foo6", "bar").defaultValue())
            .isEqualTo("bar")
        Assertions.assertThat(rxPreferences.getStringSet("foo7", setOf("bar")).defaultValue()) //
            .isEqualTo(setOf("bar"))
        Assertions.assertThat(
            rxPreferences.getObject("foo8", Point(1, 2), pointConverter).defaultValue()
        ) //
            .isEqualTo(Point(1, 2))
    }//

    //
    @Test
    fun withNoValueReturnsDefaultValue() {
        Assertions.assertThat(rxPreferences.getBoolean("foo1", false).get()).isEqualTo(false)
        Assertions.assertThat(
            rxPreferences.getEnum(
                "foo2",
                Roshambo.ROCK,
                Roshambo::class.java
            ).get()
        ).isEqualTo(Roshambo.ROCK)
        Assertions.assertThat(rxPreferences.getFloat("foo3", 1f).get()).isEqualTo(1f)
        Assertions.assertThat(rxPreferences.getInteger("foo4", 1).get()).isEqualTo(1)
        Assertions.assertThat(rxPreferences.getLong("foo5", 1L).get()).isEqualTo(1L)
        Assertions.assertThat(rxPreferences.getString("foo6", "bar").get()).isEqualTo("bar")
        Assertions.assertThat(rxPreferences.getStringSet("foo7", setOf("bar")).get()) //
            .isEqualTo(setOf("bar"))
        Assertions.assertThat(
            rxPreferences.getObject("foo8", Point(1, 2), pointConverter).get()
        ) //
            .isEqualTo(Point(1, 2))
    }

    @Test
    fun withStoredValue() {
        preferences.edit().putBoolean("foo1", false).commit()
        Assertions.assertThat(rxPreferences.getBoolean("foo1").get()).isEqualTo(false)
        preferences.edit().putString("foo2", "ROCK").commit()
        Assertions.assertThat(
            rxPreferences.getEnum(
                "foo2",
                Roshambo.PAPER,
                Roshambo::class.java
            ).get()
        ).isEqualTo(Roshambo.ROCK)
        preferences.edit().putFloat("foo3", 1f).commit()
        Assertions.assertThat(rxPreferences.getFloat("foo3").get()).isEqualTo(1f)
        preferences.edit().putInt("foo4", 1).commit()
        Assertions.assertThat(rxPreferences.getInteger("foo4").get()).isEqualTo(1)
        preferences.edit().putLong("foo5", 1L).commit()
        Assertions.assertThat(rxPreferences.getLong("foo5").get()).isEqualTo(1L)
        preferences.edit().putString("foo6", "bar").commit()
        Assertions.assertThat(rxPreferences.getString("foo6").get()).isEqualTo("bar")
        preferences.edit().putStringSet("foo7", setOf("bar")).commit()
        Assertions.assertThat(rxPreferences.getStringSet("foo7").get())
            .isEqualTo(setOf("bar"))
        preferences.edit().putString("foo8", "1,2").commit()
        Assertions.assertThat(
            rxPreferences.getObject("foo8", Point(2, 3), pointConverter).get()
        )
            .isEqualTo(Point(1, 2))
    }

    @Test
    fun set() {
        rxPreferences.getBoolean("foo1").set(false)
        Assertions.assertThat(preferences.getBoolean("foo1", true)).isFalse
        rxPreferences.getEnum("foo2", Roshambo.PAPER, Roshambo::class.java).set(Roshambo.ROCK)
        Assertions.assertThat(preferences.getString("foo2", null)).isEqualTo("ROCK")
        rxPreferences.getFloat("foo3").set(1f)
        Assertions.assertThat(preferences.getFloat("foo3", 0f)).isEqualTo(1f)
        rxPreferences.getInteger("foo4").set(1)
        Assertions.assertThat(preferences.getInt("foo4", 0)).isEqualTo(1)
        rxPreferences.getLong("foo5").set(1L)
        Assertions.assertThat(preferences.getLong("foo5", 0L)).isEqualTo(1L)
        rxPreferences.getString("foo6").set("bar")
        Assertions.assertThat(preferences.getString("foo6", null)).isEqualTo("bar")
        rxPreferences.getStringSet("foo7").set(setOf("bar"))
        Assertions.assertThat(preferences.getStringSet("foo7", null))
            .isEqualTo(setOf("bar"))
        rxPreferences.getObject("foo8", Point(2, 3), pointConverter).set(Point(1, 2))
        Assertions.assertThat(preferences.getString("foo8", null)).isEqualTo("1,2")
    }


    @Test
    fun isSet() {
        val preference = rxPreferences.getString("foo")
        Assertions.assertThat(preferences.contains("foo")).isFalse
        Assertions.assertThat(preference.isSet).isFalse
        preferences.edit().putString("foo", "2,3").commit()
        Assertions.assertThat(preference.isSet).isTrue
        preferences.edit().remove("foo").commit()
        Assertions.assertThat(preference.isSet).isFalse
    }

    @Test
    fun delete() {
        val preference = rxPreferences.getString("foo")
        preferences.edit().putBoolean("foo", true).commit()
        Assertions.assertThat(preferences.contains("foo")).isTrue
        preference.delete()
        Assertions.assertThat(preferences.contains("foo")).isFalse
    }


    @Test
    fun asObservable() {
        val preference = rxPreferences.getString("foo", "bar")
        val observer: RecordingObserver<String> = observerRule.create()
        preference.asObservable().subscribe(observer)
        observer.assertValue("bar")
        preferences.edit().putString("foo", "baz").commit()
        observer.assertValue("baz")
        preferences.edit().remove("foo").commit()
        observer.assertValue("bar")
    }

    @Ignore("Robolectric needs to be updated to support API 30")
    @Test
    fun asObservableWhenBackingPrefsCleared() {
        val preference = rxPreferences.getString("foo", "bar")
        val observer: RecordingObserver<String> = observerRule.create()
        preference.asObservable().subscribe(observer)
        observer.assertValue("bar")
        preferences.edit().putString("foo", "baz").commit()
        observer.assertValue("baz")
        preferences.edit().clear().commit()
        observer.assertValue("bar")
    }

    @Test
    @Throws(Exception::class)
    fun asConsumer() {
        val preference = rxPreferences.getString("foo")
        val consumer = preference.asConsumer()
        consumer.accept("bar")
        Assertions.assertThat(preferences.getString("foo", null)).isEqualTo("bar")
        consumer.accept("baz")
        Assertions.assertThat(preferences.getString("foo", null)).isEqualTo("baz")
        try {
            consumer.accept(null)
            Assertions.fail<Any>("Disallow accepting null.")
        } catch (e: NullPointerException) {
            Assertions.assertThat(e).hasMessage("value == null")
        }
    }

    @Test
    fun legacyNullString() {
        nullValue("string")
        Assertions.assertThat(rxPreferences.getString("string", "default").get())
            .isEqualTo("default")
    }

    @Test
    fun legacyNullBoolean() {
        nullValue("bool")
        Assertions.assertThat(rxPreferences.getBoolean("bool", true).get()).isEqualTo(true)
    }

    @Test
    fun legacyNullEnum() {
        nullValue("enum")
        Assertions.assertThat(
            rxPreferences.getEnum("enum", Roshambo.PAPER, Roshambo::class.java).get()
        ).isEqualTo(Roshambo.PAPER)
    }

    @Test
    fun legacyNullFloat() {
        nullValue("float")
        Assertions.assertThat(rxPreferences.getFloat("float", 123.45f).get()).isEqualTo(123.45f)
    }

    @Test
    fun legacyNullInteger() {
        nullValue("int")
        Assertions.assertThat(rxPreferences.getInteger("int", 12345).get()).isEqualTo(12345)
    }

    @Test
    fun legacyNullLong() {
        nullValue("long")
        Assertions.assertThat(rxPreferences.getLong("long", 12345L).get()).isEqualTo(12345L)
    }

    @Test
    fun legacyNullObject() {
        nullValue("obj")
        Assertions.assertThat(
            rxPreferences.getObject("obj", Point(10, 11), pointConverter).get()
        )
            .isEqualTo(Point(10, 11))
    }

    @Test
    fun legacyNullSet() {
        nullValue("set")
        val strings = listOf("able", "baker", "charlie")
        val defaultSet = HashSet(strings)
        val expectedSet = HashSet(strings)
        Assertions.assertThat(rxPreferences.getStringSet("key", defaultSet).get())
            .isEqualTo(expectedSet)
    }

    private fun nullValue(key: String) {
        preferences.edit()
            .putString(key, null)
            .commit()
    }
}