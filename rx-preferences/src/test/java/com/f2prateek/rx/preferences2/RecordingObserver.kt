/*
 * Copyright (C) 2016 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.f2prateek.rx.preferences2

import io.reactivex.rxjava3.core.Notification
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import org.assertj.core.api.Assertions.assertThat
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/** A test [Observer] and JUnit rule which guarantees all events are asserted.  */
class RecordingObserver<T> private constructor() : Observer<T> {
    private val events = ArrayDeque<Notification<T>>()

    override fun onSubscribe(disposable: Disposable?) {}

    override fun onNext(value: T) {
        events.add(Notification.createOnNext(value))
    }

    override fun onComplete() {
        events.add(Notification.createOnComplete())
    }

    override fun onError(e: Throwable) {
        events.add(Notification.createOnError(e))
    }

    private fun takeNotification(): Notification<T> {
        return events.firstOrNull() ?: throw AssertionError("No event found!")
    }

    private fun takeValue(): T {
        val notification: Notification<T> = takeNotification()
        assertThat(notification.isOnNext).isTrue
        return notification.value
    }

    fun assertValue(value: T): RecordingObserver<T> {
        assertThat(takeValue()).isEqualTo(value)
        return this
    }

    fun assertNoEvents() {
        assertThat(events).isEmpty()
    }

    class Rule : TestRule {
        val subscribers: MutableList<RecordingObserver<*>> = ArrayList()
        fun <T> create(): RecordingObserver<T> {
            val subscriber = RecordingObserver<T>()
            subscribers.add(subscriber)
            return subscriber
        }

        override fun apply(base: Statement, description: Description): Statement {
            return object : Statement() {
                @Throws(Throwable::class)
                override fun evaluate() {
                    base.evaluate()
                    for (subscriber in subscribers) {
                        subscriber.assertNoEvents()
                    }
                }
            }
        }
    }
}
