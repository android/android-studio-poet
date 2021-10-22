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

import com.google.androidstudiopoet.input.AndroidBuildConfig
import com.google.androidstudiopoet.input.BuildTypeConfig
import com.google.androidstudiopoet.input.FlavorConfig
import com.google.androidstudiopoet.input.PluginConfig
import com.google.androidstudiopoet.utils.joinPath

class AndroidBuildGradleBlueprint(val isApplication: Boolean,
                                  private val enableKotlin: Boolean,
                                  val enableCompose: Boolean,
                                  val enableDataBinding: Boolean,
                                  val enableKapt: Boolean,
                                  val enableViewBinding: Boolean,
                                  moduleRoot: String,
                                  androidBuildConfig: AndroidBuildConfig,
                                  val packageName: String,
                                  val extraLines: List<String>?,
                                  productFlavorConfigs: List<FlavorConfig>?,
                                  buildTypeConfigs: List<BuildTypeConfig>?,
                                  additionalDependencies: Set<Dependency>,
                                  pluginConfigs: List<PluginConfig>?) {
    val plugins: Set<String> = createSetOfPlugins(pluginConfigs)

    val libraries: Set<LibraryDependency> = createSetOfLibraries()

    val dependencies = additionalDependencies + libraries

    val path = moduleRoot.joinPath("build.gradle")

    val minSdkVersion = androidBuildConfig.minSdkVersion
    val targetSdkVersion = androidBuildConfig.targetSdkVersion
    val compileSdkVersion = androidBuildConfig.compileSdkVersion

    val productFlavors = productFlavorConfigs?.flatMap { it.toFlavors() }?.toSet()
    val flavorDimensions = productFlavors?.mapNotNull { it.dimension }?.toSet()

    val buildTypes = buildTypeConfigs?.map { BuildType(it.name, it.body) }?.toSet()

    val additionalTasks: Set<GradleTask> = pluginConfigs?.filter { it.taskName != null }?.map { GradleTask(it.taskName!!,
            it.taskBody) }?.toSet() ?: setOf()

    private fun createSetOfLibraries(): Set<LibraryDependency> {
        val result = mutableSetOf(
                LibraryDependency("implementation", "androidx.core:core-ktx:1.6.0"),
                LibraryDependency("implementation", "androidx.appcompat:appcompat:1.3.1"),
                LibraryDependency("implementation", "androidx.constraintlayout:constraintlayout:2.1.0"),
                LibraryDependency("testImplementation", "junit:junit:4.12"),
                LibraryDependency("androidTestImplementation", "androidx.test.ext:junit:1.1.3"),
                LibraryDependency("androidTestImplementation", "androidx.test.espresso:espresso-core:3.4.0"),
        )

        if (enableKotlin) {
            result += LibraryDependency("implementation", "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${'$'}kotlin_version")
        }

        if (enableCompose) {
            result += LibraryDependency("implementation", "androidx.compose.ui:ui:1.0.4")
            result += LibraryDependency("implementation", "androidx.compose.material:material:1.0.4")
            result += LibraryDependency("implementation", "androidx.activity:activity-compose:1.3.1")
            result += LibraryDependency("androidTestImplementation", "androidx.compose.ui:ui-test-junit4:1.0.4")
            result += LibraryDependency("debugImplementation", "androidx.compose.ui:ui-tooling:1.0.4")
        }

        return result
    }

    private fun createSetOfPlugins(pluginConfigs: List<PluginConfig>?): Set<String> {
        val result = mutableSetOf<String>()
        result += if (isApplication) "com.android.application" else "com.android.library"
        if (enableKotlin) {
            result += listOf("kotlin-android")
        }
        if (enableKotlin && enableDataBinding && enableKapt) {
            result += "kotlin-kapt"
        }
        pluginConfigs?.map { it.id }?.forEach { result.add(it) }

        return result
    }
}

private fun FlavorConfig.toFlavors(): List<Flavor> {
    return if (this.count == null || this.count!! <= 1) {
        listOf(Flavor(this.name, this.dimension))
    } else {
        (0 until this.count!!).map { Flavor(this.name + it, this.dimension) }
    }
}