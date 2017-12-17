package com.google.androidstudiopoet.test_utils

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