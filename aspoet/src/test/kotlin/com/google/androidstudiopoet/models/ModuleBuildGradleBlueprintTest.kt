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

package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.input.PluginConfig
import com.google.androidstudiopoet.testutils.assertContains
import com.google.androidstudiopoet.testutils.assertEmpty
import com.google.androidstudiopoet.testutils.assertEquals
import com.google.androidstudiopoet.testutils.assertOn
import com.google.androidstudiopoet.utils.joinPath
import org.junit.Test

class ModuleBuildGradleBlueprintTest {

    @Test
    fun `plugins contain only java library plugin`() {
        val blueprint = createModuleBuildGradleBlueprint()

        assertOn(blueprint) {
            plugins.assertEquals(setOf("java-library"))
        }
    }

    @Test
    fun `plugins contain kotlin plugin when kotlin is enabled`() {
        val blueprint = createModuleBuildGradleBlueprint(enableKotlin = true)

        assertOn(blueprint) {
            plugins.assertContains("kotlin")
        }
    }

    @Test
    fun `plugins contain id of provided plugin`() {
        val pluginId = "random plugin name"
        val blueprint = createModuleBuildGradleBlueprint(pluginConfigs = listOf(PluginConfig(id = pluginId)))

        assertOn(blueprint) {
            plugins.assertContains(pluginId)
        }
    }

    @Test
    fun `path equals module root joined with build gradle`() {
        val blueprint = createModuleBuildGradleBlueprint(moduleRoot = "root")

        assertOn(blueprint) {
            path.assertEquals("root".joinPath("build.gradle"))
        }
    }

    @Test
    fun `dependencies are empty by default`() {
        val blueprint = createModuleBuildGradleBlueprint()

        assertOn(blueprint) {
            dependencies.assertEmpty()
        }
    }

    @Test
    fun `dependencies contains kotlin stldlib when kotlin is enabled`() {
        val blueprint = createModuleBuildGradleBlueprint(enableKotlin = true)

        assertOn(blueprint) {
            dependencies.assertContains(LibraryDependency("implementation", "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${'$'}kotlin_version"))
        }
    }

    @Test
    fun `dependencies contains junit when generate tests is true`() {
        val blueprint = createModuleBuildGradleBlueprint(generateTests = true)

        assertOn(blueprint) {
            dependencies.assertContains(LibraryDependency("testImplementation", "junit:junit:4.12"))
        }
    }

    private fun createModuleBuildGradleBlueprint(
            dependencies: Set<ModuleDependency> = setOf(),
            enableKotlin: Boolean = false,
            generateTests: Boolean = false,
            extraLines: List<String>? = null,
            moduleRoot: String = "",
            pluginConfigs: List<PluginConfig>? = null
    ) = ModuleBuildGradleBlueprint(dependencies, enableKotlin, generateTests, extraLines, moduleRoot, pluginConfigs)
}