package com.f2prateek.rx.preferences2

import android.annotation.SuppressLint
import android.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import com.f2prateek.rx.preferences2.RxSharedPreferences.Companion.create
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class) //
@SuppressLint("ApplySharedPref") //
//
class RxSharedPreferencesTest {
    private lateinit var rxPreferences: RxSharedPreferences

    @Before
    fun setUp() {
        val preferences =
            PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext())
        preferences.edit().clear().commit()
        rxPreferences = create(preferences)
    }

    @Test
    fun clearRemovesAllPreferences() {
        val preference = rxPreferences.getString("key", "default")
        preference.set("foo")
        rxPreferences.clear()
        Assertions.assertThat(preference.get()).isEqualTo("default")
    }

}