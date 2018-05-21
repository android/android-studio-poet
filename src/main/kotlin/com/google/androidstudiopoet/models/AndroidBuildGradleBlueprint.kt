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
import com.google.androidstudiopoet.utils.joinPath

class AndroidBuildGradleBlueprint(val isApplication: Boolean, private val enableKotlin: Boolean, val enableDataBinding: Boolean,
                                  moduleRoot: String, private val androidBuildConfig: AndroidBuildConfig, val packageName: String,
                                  val extraLines: List<String>?, productFlavorConfigs: List<FlavorConfig>?,
                                  buildTypeConfigs: List<BuildTypeConfig>?, additionalDependencies: Set<Dependency>) {
    val plugins: Set<String> = createSetOfPlugins()

    val libraries: Set<LibraryDependency> = createSetOfLibraries()

    val dependencies = additionalDependencies + libraries

    val path = moduleRoot.joinPath("build.gradle")

    val minSdkVersion = androidBuildConfig.minSdkVersion
    val targetSdkVersion = androidBuildConfig.targetSdkVersion
    val compileSdkVersion = androidBuildConfig.compileSdkVersion

    val productFlavors = productFlavorConfigs?.flatMap { it.toFlavors() }?.toSet()
    val flavorDimensions = productFlavors?.mapNotNull { it.dimension }?.toSet()

    val buildTypes = buildTypeConfigs?.map { BuildType(it.name, it.body) }?.toSet()

    private fun createSetOfLibraries(): Set<LibraryDependency> {
        val result = mutableSetOf(
                LibraryDependency("implementation", "com.android.support:appcompat-v7:26.1.0"),
                LibraryDependency("implementation", "com.android.support.constraint:constraint-layout:1.0.2"),
                LibraryDependency("testImplementation", "junit:junit:4.12"),
                LibraryDependency("androidTestImplementation", "com.android.support.test:runner:1.0.1"),
                LibraryDependency("androidTestImplementation", "com.android.support.test.espresso:espresso-core:3.0.1"),
                LibraryDependency("implementation", "com.android.support:multidex:1.0.1")
        )

        if (enableKotlin) {
            result += LibraryDependency("implementation", "org.jetbrains.kotlin:kotlin-stdlib-jre8:${'$'}kotlin_version")
        }

        if (enableKotlin && enableDataBinding) {
            result += LibraryDependency("kapt", "com.android.databinding:compiler:${androidBuildConf}")
        }

        return result
    }

    private fun createSetOfPlugins(): Set<String> {
        val result = mutableSetOf<String>()
        result += if (isApplication) "com.android.application" else "com.android.library"
        if (enableKotlin) {
            result += listOf("kotlin-android", "kotlin-android-extensions")
        }
        if (enableKotlin && enableDataBinding) {
            result += "kotlin-kapt"
        }
        return result
    }
}

private fun FlavorConfig.toFlavors(): List<Flavor> {
    return if (this.count == null || this.count <= 1) {
        listOf(Flavor(this.name, this.dimension))
    } else {
        (0 until this.count).map { Flavor(this.name + it, this.dimension) }
    }
}