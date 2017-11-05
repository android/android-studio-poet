package main.models

import com.google.gson.Gson

import org.intellij.lang.annotations.Language
import org.junit.Assert.assertEquals
import org.junit.Test

class DependencyTest {
    @Test
    fun `Dependency deserialized correctly`() {
        @Language("JSON") val dependencyString = "{\n  \"from\": 2,\n  \"to\": 1\n}"
        val dependency = Gson().fromJson(dependencyString, Dependency::class.java)
        assertEquals(2, dependency.from)
        assertEquals(1, dependency.to)
    }
}
