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
import com.google.androidstudiopoet.utils.joinPath

class ModuleBuildGradleBlueprint(
        additionalDependencies: Set<Dependency>,
        private val enableKotlin: Boolean,
        private val generateTests: Boolean,
        val extraLines: List<String>? = null,
        moduleRoot: String,
        pluginConfigs: List<PluginConfig>?
) : ModuleBuildSpecificationBlueprint {

    override val path = moduleRoot.joinPath("build.gradle")

    val plugins: Set<String> = createSetOfPlugins(pluginConfigs)

    override val dependencies = additionalDependencies + createSetOfMandatoryLibraries()

    private fun createSetOfMandatoryLibraries(): Set<LibraryDependency> {
        val result = mutableSetOf<LibraryDependency>()

        if (enableKotlin) {
            result += LibraryDependency("compile", "org.jetbrains.kotlin:kotlin-stdlib-jre8:${'$'}kotlin_version")
        }

        if (generateTests) {
            result += LibraryDependency("testCompile", "junit:junit:4.12")
        }

        return result
    }

    private fun createSetOfPlugins(pluginConfigs: List<PluginConfig>?): Set<String> {
        val result = mutableSetOf("java-library")
        if (enableKotlin) {
            result += listOf("kotlin")
        }

        pluginConfigs?.map { it.id }?.forEach { result.add(it) }

        return result
    }


}
