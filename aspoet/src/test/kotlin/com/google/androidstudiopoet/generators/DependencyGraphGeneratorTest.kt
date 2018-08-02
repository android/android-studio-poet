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

package com.google.androidstudiopoet.generators

import com.google.androidstudiopoet.testutils.mock
import com.google.androidstudiopoet.utils.joinPath
import com.google.androidstudiopoet.writers.FileWriter
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class DependencyGraphGeneratorTest: DependencyGraphBase() {
    private val fileWriter: FileWriter = mock()
    private val dependencyImageGenerator: DependencyImageGenerator = mock()
    private val dependencyGraphGenerator = DependencyGraphGenerator(fileWriter, dependencyImageGenerator)

    @Test
    fun `generator dot file is correct`() {
        val blueprint = getProjectBlueprint()
        dependencyGraphGenerator.generate(blueprint)
        val expectedPath = "projectRoot".joinPath("dependencies.dot")
        verify(fileWriter).writeToFile(expectedGraphText, expectedPath)
    }

    @Test
    fun `generator image is called`() {
        val blueprint = getProjectBlueprint()
        dependencyGraphGenerator.generate(blueprint)
        verify(dependencyImageGenerator).generate(blueprint)
    }
}