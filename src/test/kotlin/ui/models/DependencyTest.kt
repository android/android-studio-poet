package ui.models

import com.google.gson.Gson

import org.intellij.lang.annotations.Language
import org.junit.Assert.assertEquals
import org.junit.Test

class DependencyTest {

    @Test
    fun `Dependency deserialized correctly`() {
        @Language("JSON") val dependencyString = "{\n  \"from\": {\n    \"language\": \"KOTLIN\",\n    \"index\": 2\n  },\n  \"to\": {\n    \"language\": \"JAVA\",\n    \"index\": 1\n  }\n}"
        val dependency = Gson().fromJson(dependencyString, Dependency::class.java)
        assertEquals(ui.models.Language.KOTLIN, dependency.from.language)
        assertEquals(2, dependency.from.index)

        assertEquals(ui.models.Language.JAVA, dependency.to.language)
        assertEquals(1, dependency.to.index)
    }
}
