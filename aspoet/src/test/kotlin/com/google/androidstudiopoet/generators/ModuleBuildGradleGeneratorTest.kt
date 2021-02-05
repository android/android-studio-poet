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

package com.google.androidstudiopoet.generators

import com.google.androidstudiopoet.models.Dependency
import com.google.androidstudiopoet.models.ModuleBuildGradleBlueprint
import com.google.androidstudiopoet.models.LibraryDependency
import com.google.androidstudiopoet.models.ModuleDependency
import com.google.androidstudiopoet.testutils.mock
import com.google.androidstudiopoet.writers.FileWriter
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test

class ModuleBuildGradleGeneratorTest {
    private val fileWriter: FileWriter = mock()
    private val buildGradleGenerator = ModuleBuildGradleGenerator(fileWriter)

    @Test
    fun `generator applies plugins from the blueprint`() {
        val blueprint = getModuleBuildGradleBlueprint(plugins = setOf("plugin1", "plugin2"))
        buildGradleGenerator.generate(blueprint)
        val expected = """apply plugin: 'plugin1'
apply plugin: 'plugin2'
dependencies {

}
sourceCompatibility = "1.8"
targetCompatibility = "1.8""""
        verify(fileWriter).writeToFile(expected, "path")
    }

    @Test
    fun `generator applies libraries from the blueprint`() {
        val blueprint = getModuleBuildGradleBlueprint(dependencies = setOf(
                LibraryDependency("implementation", "library1"),
                LibraryDependency("testImplementation", "library2")
        ))
        buildGradleGenerator.generate(blueprint)
        val expected = """dependencies {
    implementation "library1"
    testImplementation "library2"
}
sourceCompatibility = "1.8"
targetCompatibility = "1.8""""
        verify(fileWriter).writeToFile(expected, "path")
    }

    @Test
    fun `generator applies dependencies from the blueprint`() {
        val blueprint = getModuleBuildGradleBlueprint(dependencies = setOf(
                ModuleDependency("library1", mock(),"implementation"),
                ModuleDependency("library2", mock(),"testImplementation")
        ))
        buildGradleGenerator.generate(blueprint)
        val expected = """dependencies {
    implementation project(':library1')
    testImplementation project(':library2')
}
sourceCompatibility = "1.8"
targetCompatibility = "1.8""""
        verify(fileWriter).writeToFile(expected, "path")
    }

    @Test
    fun `generator applies extralines from the blueprint`() {
        val blueprint = getModuleBuildGradleBlueprint(extraLines = listOf(
                "line1",
                "line2"
        ))
        buildGradleGenerator.generate(blueprint)
        val expected = """dependencies {

}
sourceCompatibility = "1.8"
targetCompatibility = "1.8"
line1
line2"""
        verify(fileWriter).writeToFile(expected, "path")
    }

    private fun getModuleBuildGradleBlueprint(
            plugins: Set<String> = setOf(),
            dependencies: Set<Dependency> = setOf(),
            extraLines: List<String>? = null
    ): ModuleBuildGradleBlueprint {
        return mock<ModuleBuildGradleBlueprint>().apply {
            whenever(this.dependencies).thenReturn(dependencies)
            whenever(this.plugins).thenReturn(plugins)
            whenever(this.extraLines).thenReturn(extraLines)
            whenever(this.path).thenReturn("path")
        }
    }
}