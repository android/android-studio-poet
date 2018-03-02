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

package com.google.androidstudiopoet.input

import com.google.androidstudiopoet.models.LibraryDependency
import com.google.androidstudiopoet.utils.joinPath

class AndroidBuildGradleBlueprint(val isApplication: Boolean, val enableKotlin: Boolean, val enableDataBinding: Boolean,
                                  moduleRoot: String, androidBuildConfig: AndroidBuildConfig) {
    val plugins: Set<String> = createSetOfPlugins()

    val libraries: Set<LibraryDependency> = createSetOfLibraries()

    val path = moduleRoot.joinPath("build.gradle")

    val minSdkVersion = androidBuildConfig.minSdkVersion
    val targetSdkVersion = androidBuildConfig.targetSdkVersion
    val compileSdkVersion = androidBuildConfig.compileSdkVersion

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
            result += LibraryDependency("kapt", "com.android.databinding:compiler:3.0.1")
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