package com.f2prateek.rx.preferences2.sample

import android.app.Activity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.CheckBox
import android.widget.EditText
import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences.Companion.create
import com.jakewharton.rxbinding4.widget.checkedChanges
import com.jakewharton.rxbinding4.widget.textChangeEvents
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class SampleActivity : Activity() {
    private val foo1Checkbox: CheckBox by lazy { findViewById(R.id.foo_1) }
    private val foo2Checkbox: CheckBox by lazy { findViewById(R.id.foo_2) }
    private val foo1EditText: EditText by lazy { findViewById(R.id.text_1) }
    private val foo2EditText: EditText by lazy { findViewById(R.id.text_2) }
    private lateinit var fooBoolPreference: Preference<Boolean>
    private lateinit var fooTextPreference: Preference<String>
    private var disposables: CompositeDisposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Views
        setContentView(R.layout.sample_activity)

        // Preferences
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val rxPreferences = create(preferences)

        // foo
        fooBoolPreference = rxPreferences.getBoolean("fooBool")
        fooTextPreference = rxPreferences.getString("fooText")
    }

    override fun onResume() {
        super.onResume()
        disposables = CompositeDisposable()
        bindPreference(foo1Checkbox, fooBoolPreference)
        bindPreference(foo2Checkbox, fooBoolPreference)
        bindPreference(foo1EditText, fooTextPreference)
        bindPreference(foo2EditText, fooTextPreference)
    }

    override fun onPause() {
        super.onPause()
        disposables?.dispose()
    }

    private fun bindPreference(checkBox: CheckBox, preference: Preference<Boolean>) {
        // Bind the preference to the checkbox.
        disposables?.add(
            preference.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { checked -> checkBox.isChecked = checked })
        // Bind the checkbox to the preference.
        disposables?.add(
            checkBox.checkedChanges()
                .skip(1) // First emission is the original state.
                .subscribe(preference.asConsumer())
        )
    }

    private fun bindPreference(editText: EditText, preference: Preference<String>) {
        disposables?.add(
            preference.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .filter { !editText.isFocused }
                .subscribe { text: String? -> editText.setText(text) })
        disposables?.add(
            editText.textChangeEvents()
                .skip(1) // First emission is the original state.
                .debounce(
                    500,
                    TimeUnit.MILLISECONDS
                ) // Filter out UI events that are emitted in quick succession.
                .map { (_, text) -> text.toString() }
                .subscribe(preference.asConsumer()))
    }
}