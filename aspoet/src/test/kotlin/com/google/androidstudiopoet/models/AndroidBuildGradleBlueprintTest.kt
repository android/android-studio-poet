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
    fun `plugins contain kotlin-android when Kotlin is enabled`() {
        val blueprint = createAndroidBuildGradleBlueprint(enableKotlin = true)

        assertOn(blueprint) {
            plugins.assertContains("kotlin-android")
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
    fun `plugins contain kotlin-kapt when Kotlin, data binding, and kapt is enabled`() {
        val blueprint = createAndroidBuildGradleBlueprint(enableKotlin = true, enableDataBinding = true, enableKapt = true)

        assertOn(blueprint) {
            plugins.assertContains("kotlin-kapt")
        }
    }

    @Test
    fun `plugins contain id of provided plugin`() {
        val pluginId = "random plugin name"
        val blueprint = createAndroidBuildGradleBlueprint(pluginConfigs = listOf(PluginConfig(id = pluginId)))

        assertOn(blueprint) {
            plugins.assertContains(pluginId)
        }
    }

    @Test
    fun `libraries contain default set of libraries`() {
        val blueprint = createAndroidBuildGradleBlueprint()

        assertOn(blueprint) {
            libraries.assertEquals(setOf(
                    LibraryDependency("implementation", "androidx.core:core-ktx:1.6.0"),
                    LibraryDependency("implementation", "androidx.appcompat:appcompat:1.3.1"),
                    LibraryDependency("implementation", "androidx.constraintlayout:constraintlayout:2.1.0"),
                    LibraryDependency("testImplementation", "junit:junit:4.12"),
                    LibraryDependency("androidTestImplementation", "androidx.test.ext:junit:1.1.3"),
                    LibraryDependency("androidTestImplementation", "androidx.test.espresso:espresso-core:3.4.0")
            ))
        }
    }

    @Test
    fun `compose variant contains compose libraries`() {
        val blueprint = createAndroidBuildGradleBlueprint(enableCompose = true)

        assertOn(blueprint) {
            libraries.assertEquals(setOf(
                    LibraryDependency("implementation", "androidx.core:core-ktx:1.6.0"),
                    LibraryDependency("implementation", "androidx.appcompat:appcompat:1.3.1"),
                    LibraryDependency("implementation", "androidx.constraintlayout:constraintlayout:2.1.0"),
                    LibraryDependency("testImplementation", "junit:junit:4.12"),
                    LibraryDependency("androidTestImplementation", "androidx.test.ext:junit:1.1.3"),
                    LibraryDependency("androidTestImplementation", "androidx.test.espresso:espresso-core:3.4.0"),
                    LibraryDependency("implementation", "androidx.compose.ui:ui:1.0.4"),
                    LibraryDependency("implementation", "androidx.compose.material:material:1.0.4"),
                    LibraryDependency("implementation", "androidx.activity:activity-compose:1.3.1"),
                    LibraryDependency("androidTestImplementation", "androidx.compose.ui:ui-test-junit4:1.0.4"),
                    LibraryDependency("debugImplementation", "androidx.compose.ui:ui-tooling:1.0.4")
            ))
        }
    }

    @Test
    fun `libraries contains kotlin stldlib when kotlin is enabled`() {
        val blueprint = createAndroidBuildGradleBlueprint(enableKotlin = true)

        assertOn(blueprint) {
            libraries.assertContains(LibraryDependency("implementation", "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${'$'}kotlin_version"))
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

    @Test
    fun `blueprint creates proper flavors and dimensions`() {
        val dimension1 = "dim1"
        val dimension2 = "dim2"
        val flavorName1 = "flav1"
        val flavorName2 = "flav2"
        val flavorName3 = "flav3"
        val flavorConfigs = listOf(
                FlavorConfig(flavorName1, dimension1),
                FlavorConfig(flavorName2, dimension1),
                FlavorConfig(flavorName3, dimension2))
        val blueprint = createAndroidBuildGradleBlueprint(productFlavorConfigs = flavorConfigs)

        blueprint.flavorDimensions!!.assertEquals(setOf(dimension1, dimension2))
        blueprint.productFlavors!!.assertEquals(setOf(
                Flavor(flavorName1, dimension1),
                Flavor(flavorName2, dimension1),
                Flavor(flavorName3, dimension2)
        ))
    }

    @Test
    fun `blueprint creates amount of flavors that set in "count" field of each FlavorConfig`() {
        val dimension = "dim"
        val flavorName = "flav"
        val expectedFlavorName0 = "flav0"
        val expectedFlavorName1 = "flav1"
        val expectedFlavorName2 = "flav2"
        val flavorCount = 3
        val flavorConfig = FlavorConfig(flavorName, dimension, flavorCount)

        val blueprint = createAndroidBuildGradleBlueprint(productFlavorConfigs = listOf(flavorConfig))

        assertOn(blueprint) {
            blueprint.flavorDimensions!!.assertEquals(setOf(dimension))
            blueprint.productFlavors!!.assertEquals(
                setOf(
                    Flavor(expectedFlavorName0, dimension),
                    Flavor(expectedFlavorName1, dimension),
                    Flavor(expectedFlavorName2, dimension)
                )
            )
        }
    }

    @Test
    fun `blueprint creates buildTypes from BuildTypeConfigs`() {
        val name1 = "name1"
        val body1 = "body1"
        val name2 = "name2"
        val body2 = "body2"

        val buildTypeConfigs = listOf(
                BuildTypeConfig(name1, body1),
                BuildTypeConfig(name2, body2)
        )

        val blueprint = createAndroidBuildGradleBlueprint(buildTypeConfigs = buildTypeConfigs)

        assertOn(blueprint) {
            blueprint.buildTypes!!.assertEquals(setOf(
                    BuildType(name1, body1),
                    BuildType(name2, body2)
            ))
        }
    }

    @Test
    fun `additionalTasks contain task from provided pluginConfig`() {
        val pluginId = "random plugin name"
        val taskName = "someTaskName"
        val taskBody = listOf("line1", "line2")
        val blueprint = createAndroidBuildGradleBlueprint(pluginConfigs = listOf(
                PluginConfig(
                        id = pluginId,
                        taskName = taskName,
                        taskBody = taskBody
                )))

        assertOn(blueprint) {
            additionalTasks.assertContains(GradleTask(taskName, taskBody))
        }
    }

    private fun createAndroidBuildGradleBlueprint(isApplication: Boolean = false,
                                                  enableKotlin: Boolean = false,
                                                  enableCompose: Boolean = false,
                                                  enableDataBinding: Boolean = false,
                                                  enableKapt: Boolean = false,
                                                  enableViewBinding: Boolean = false,
                                                  moduleRoot: String = "",
                                                  androidBuildConfig: AndroidBuildConfig = AndroidBuildConfig(),
                                                  packageName: String = "com.example",
                                                  extraLines: List<String>? = null,
                                                  productFlavorConfigs: List<FlavorConfig>? = null,
                                                  buildTypeConfigs: List<BuildTypeConfig>? = null,
                                                  dependencies: Set<ModuleDependency> = setOf(),
                                                  pluginConfigs: List<PluginConfig>? = null
    ) = AndroidBuildGradleBlueprint(
            isApplication,
            enableKotlin,
            enableCompose,
            enableDataBinding,
            enableKapt,
            enableViewBinding,
            moduleRoot,
            androidBuildConfig,
            packageName,
            extraLines,
            productFlavorConfigs,
            buildTypeConfigs,
            dependencies,
            pluginConfigs
    )
}