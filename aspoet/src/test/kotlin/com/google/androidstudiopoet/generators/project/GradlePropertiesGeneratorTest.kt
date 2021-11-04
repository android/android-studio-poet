/*
Copyright 2018 Google Inc.

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

package com.google.androidstudiopoet.generators.project

import com.google.androidstudiopoet.models.GradlePropertiesBlueprint
import com.google.androidstudiopoet.testutils.mock
import com.google.androidstudiopoet.writers.FileWriter
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
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
                path = "path",
                properties = mapOf(
                        key1 to value1,
                        key2 to value2
                )
        ))

        verify(fileWriter).writeToFile("$key1=$value1\n$key2=$value2", "path")
    }

    @Test
    fun `generate should not create a file if properties are absent`() {
        generator.generate(getGradlePropertiesBlueprint(
                properties = null
        ))

    verifyZeroInteractions(fileWriter)
    }

    private fun getGradlePropertiesBlueprint(
            path: String = "path",
            properties: Map<String, String>? = mapOf()
    ): GradlePropertiesBlueprint {
        return mock<GradlePropertiesBlueprint>().apply {
            whenever(this.path).thenReturn(path)
            whenever(this.properties).thenReturn(properties)
        }
    }
}