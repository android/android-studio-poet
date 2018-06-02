package com.google.androidstudiopoet.generators.project

import com.google.androidstudiopoet.models.GradlePropertiesBlueprint
import com.google.androidstudiopoet.testutils.mock
import com.google.androidstudiopoet.utils.joinPath
import com.google.androidstudiopoet.writers.FileWriter
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class GradlePropertiesGeneratorTest {

    private val fileWriter: FileWriter = mock()

    private val generator = GradlePropertiesGenerator(fileWriter)

    @Test
    fun `generate should write properties into the file`() {
        val key1 = "randomKey1"
        val value1 = "randomValue1"

        val key2 = "randomKey2"
        val value2 = "randomValue2"
        generator.generate(getGradlePropertiesBlueprint(
                root = "root",
                properties = mapOf(
                        key1 to value1,
                        key2 to value2
                )
        ))

        verify(fileWriter).writeToFile("$key1=$value1\n$key2=$value2", "root".joinPath("gradle.properties"))
    }

    @Test
    fun `generate should not create a file if properties are absent`() {
        generator.generate(getGradlePropertiesBlueprint(
                properties = null
        ))

        verify(fileWriter, never())
    }

    private fun getGradlePropertiesBlueprint(root: String = "root", properties: Map<String, String>? = mapOf()) = GradlePropertiesBlueprint(root, properties)
}