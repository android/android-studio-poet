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
import com.google.androidstudiopoet.testutils.assertContains
import com.google.androidstudiopoet.testutils.assertEquals
import com.google.androidstudiopoet.testutils.assertNotContains
import com.google.androidstudiopoet.testutils.assertOn
import com.google.androidstudiopoet.utils.joinPath
import org.junit.Test

class AndroidBuildGradleBlueprintTest {

    @Test
    fun `plugins contain android library when is not an application module`() {
        val blueprint = createAndroidBuildGradleBlueprint(isApplication = false)

        assertOn(blueprint) {
            plugins.assertContains("com.android.library")
        }
    }

    @Test
    fun `plugins contain android application when is an application module`() {
        val blueprint = createAndroidBuildGradleBlueprint(isApplication = true)

        assertOn(blueprint) {
            plugins.assertContains("com.android.application")
        }

    }

    @Test
    fun `plugins contain kotlin-android and kotlin-android-extensions when Kotlin is enabled`() {
        val blueprint = createAndroidBuildGradleBlueprint(enableKotlin = true)

        assertOn(blueprint) {
            plugins.assertContains("kotlin-android")
            plugins.assertContains("kotlin-android-extensions")
        }
    }

    @Test
    fun `plugins do not contain kotlin-kapt when Kotlin is disabled and data binding is enabled`() {
        val blueprint = createAndroidBuildGradleBlueprint(enableKotlin = false, enableDataBinding = true)

        assertOn(blueprint) {
            plugins.assertNotContains("kotlin-kapt")
        }
    }

    @Test
    fun `plugins do not contain kotlin-kapt when Kotlin is enabled and data binding is disabled`() {
        val blueprint = createAndroidBuildGradleBlueprint(enableKotlin = true, enableDataBinding = false)

        assertOn(blueprint) {
            plugins.assertNotContains("kotlin-kapt")
        }
    }

    @Test
    fun `plugins contain kotlin-kapt when Kotlin and data binding are enabled`() {
        val blueprint = createAndroidBuildGradleBlueprint(enableKotlin = true, enableDataBinding = true)

        assertOn(blueprint) {
            plugins.assertContains("kotlin-kapt")
        }
    }

    @Test
    fun `libraries contain default set of libraries`() {
        val blueprint = createAndroidBuildGradleBlueprint()

        assertOn(blueprint) {
            libraries.assertEquals(setOf(
                    LibraryDependency("implementation", "com.android.support:appcompat-v7:26.1.0"),
                    LibraryDependency("implementation", "com.android.support.constraint:constraint-layout:1.0.2"),
                    LibraryDependency("testImplementation", "junit:junit:4.12"),
                    LibraryDependency("androidTestImplementation", "com.android.support.test:runner:1.0.1"),
                    LibraryDependency("androidTestImplementation", "com.android.support.test.espresso:espresso-core:3.0.1"),
                    LibraryDependency("implementation", "com.android.support:multidex:1.0.1")
            ))
        }
    }

    @Test
    fun `libraries contains kotlin stldlib when kotlin is enabled`() {
        val blueprint = createAndroidBuildGradleBlueprint(enableKotlin = true)

        assertOn(blueprint) {
            libraries.assertContains(LibraryDependency("implementation", "org.jetbrains.kotlin:kotlin-stdlib-jre8:${'$'}kotlin_version"))
        }
    }

    @Test
    fun `plugins do not contain data binding compiler when Kotlin is disabled and data binding is enabled`() {
        val blueprint = createAndroidBuildGradleBlueprint(enableKotlin = false, enableDataBinding = true)

        assertOn(blueprint) {
            libraries.assertNotContains(LibraryDependency("kapt", "com.android.databinding:compiler:3.0.1"))
        }
    }

    @Test
    fun `plugins do not contain data binding compiler when Kotlin is enabled and data binding is disabled`() {
        val blueprint = createAndroidBuildGradleBlueprint(enableKotlin = true, enableDataBinding = false)

        assertOn(blueprint) {
            libraries.assertNotContains(LibraryDependency("kapt", "com.android.databinding:compiler:3.0.1"))
        }
    }

    @Test
    fun `plugins contain data binding compiler when Kotlin and data binding are enabled`() {
        val blueprint = createAndroidBuildGradleBlueprint(enableKotlin = true, enableDataBinding = true)

        assertOn(blueprint) {
            libraries.assertContains(LibraryDependency("kapt", "com.android.databinding:compiler:3.0.1"))
        }
    }

    @Test
    fun `path is module root joined with build gradle`() {
        val blueprint = createAndroidBuildGradleBlueprint(moduleRoot = "moduleRoot")

        assertOn(blueprint) {
            path.assertEquals("moduleRoot".joinPath("build.gradle"))
        }
    }

    @Test
    fun `minSdkVersion is passed from AndroidBuildConfig`() {
        val androidBuildConfig = AndroidBuildConfig(minSdkVersion = 7)
        val androidModuleBlueprint = createAndroidBuildGradleBlueprint(androidBuildConfig = androidBuildConfig)

        androidModuleBlueprint.minSdkVersion.assertEquals(androidBuildConfig.minSdkVersion)
    }

    @Test
    fun `targetSdkVersion is passed from AndroidBuildConfig`() {
        val androidBuildConfig = AndroidBuildConfig(targetSdkVersion = 7)
        val androidModuleBlueprint = createAndroidBuildGradleBlueprint(androidBuildConfig = androidBuildConfig)

        androidModuleBlueprint.targetSdkVersion.assertEquals(androidBuildConfig.targetSdkVersion)
    }

    @Test
    fun `compileSdkVersion is passed from AndroidBuildConfig`() {
        val androidBuildConfig = AndroidBuildConfig(compileSdkVersion = 7)
        val androidModuleBlueprint = createAndroidBuildGradleBlueprint(androidBuildConfig = androidBuildConfig)

        androidModuleBlueprint.compileSdkVersion.assertEquals(androidBuildConfig.compileSdkVersion)
    }

    private fun createAndroidBuildGradleBlueprint(isApplication: Boolean = false,
                                                  enableKotlin: Boolean = false,
                                                  enableDataBinding: Boolean = false,
                                                  moduleRoot: String = "",
                                                  androidBuildConfig: AndroidBuildConfig = AndroidBuildConfig()
    ) = AndroidBuildGradleBlueprint(isApplication, enableKotlin, enableDataBinding, moduleRoot, androidBuildConfig)
}