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

class AndroidBuildBazelBlueprint(val isApplication: Boolean, private val enableKotlin: Boolean, val enableDataBinding: Boolean,
                                 moduleRoot: String, androidBuildConfig: AndroidBuildConfig, val packageName: String,
                                 override val extraLines: List<String>?, productFlavorConfigs: List<FlavorConfig>?,
                                 buildTypeConfigs: List<BuildTypeConfig>?, additionalDependencies: Set<Dependency>,
                                 private val generateTests: Boolean) : AndroidModuleBuildSpecificationBlueprint {
//    override val plugins: Set<String> = createSetOfPlugins()
    override val plugins = mutableSetOf<String>()

    val libraries: Set<GmavenBazelDependency> = createSetOfLibraries()

    override val dependencies = additionalDependencies + libraries

    override val path = moduleRoot.joinPath("BUILD.bazel")

//    val minSdkVersion = androidBuildConfig.minSdkVersion
//    val targetSdkVersion = androidBuildConfig.targetSdkVersion
//    val compileSdkVersion = androidBuildConfig.compileSdkVersion
//
//    val productFlavors = productFlavorConfigs?.flatMap { it.toFlavors() }?.toSet()
//    val flavorDimensions = productFlavors?.mapNotNull { it.dimension }?.toSet()
//
//    val buildTypes = buildTypeConfigs?.map { BuildType(it.name, it.body) }?.toSet()

    private fun createSetOfLibraries(): Set<GmavenBazelDependency> {
        val result = mutableSetOf(
                GmavenBazelDependency( "com.android.support:appcompat-v7:aar:26.1.0"),
                GmavenBazelDependency( "com.android.support.constraint:constraint-layout:aar:1.0.2"),
                GmavenBazelDependency("com.android.support:multidex:aar:1.0.1")
        )

        if (generateTests) {
            result += mutableSetOf(
                    // TODO(jin): junit doesn't exist on Gmaven, use regular maven_jar
                    GmavenBazelDependency( "junit:junit::4.12"),
                    GmavenBazelDependency("com.android.support.test:runner:aar:1.0.1"),
                    GmavenBazelDependency("com.android.support.test.espresso:espresso-core:aar:3.0.1")
            )
        }

//        if (enableKotlin) {
//            result += LibraryDependency("implementation", "org.jetbrains.kotlin:kotlin-stdlib-jre8:${'$'}kotlin_version")
//        }
//
//        if (enableKotlin && enableDataBinding) {
//            result += LibraryDependency("kapt", "com.android.databinding:compiler:3.0.1")
//        }

        return result
    }
//
//    private fun createSetOfPlugins(): Set<String> {
//        val result = mutableSetOf<String>()
//        result += if (isApplication) "com.android.application" else "com.android.library"
//        if (enableKotlin) {
//            result += listOf("kotlin-android", "kotlin-android-extensions")
//        }
//        if (enableKotlin && enableDataBinding) {
//            result += "kotlin-kapt"
//        }
//        return result
//    }
}

//private fun FlavorConfig.toFlavors(): List<Flavor> {
//    return if (this.count == null || this.count <= 1) {
//        listOf(Flavor(this.name, this.dimension))
//    } else {
//        (0 until this.count).map { Flavor(this.name + it, this.dimension) }
//    }
//}