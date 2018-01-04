/*
Copyright 2017 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.google.androidstudiopoet.testutils

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue

fun <T> Collection<T>.assertEmpty() {
    assertTrue(this.isEmpty())
}

inline fun <T> Collection<T>.assertSize(expectedSize: Int) {
    assertEquals(expectedSize, size)
}

inline fun Any.assertEquals(expected: Any) {
    assertEquals(expected, this)
}

inline fun <T, R> assertOn(receiver: T, block: T.() -> R): R = receiver.block()