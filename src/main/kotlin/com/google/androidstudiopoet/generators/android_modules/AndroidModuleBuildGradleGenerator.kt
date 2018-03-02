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

package com.google.androidstudiopoet.generators.android_modules

import com.google.androidstudiopoet.input.AndroidBuildGradleBlueprint
import com.google.androidstudiopoet.models.Flavor
import com.google.androidstudiopoet.models.LibraryDependency
import com.google.androidstudiopoet.utils.fold
import com.google.androidstudiopoet.utils.isNullOrEmpty
import com.google.androidstudiopoet.utils.joinLines
import com.google.androidstudiopoet.writers.FileWriter

class AndroidModuleBuildGradleGenerator(val fileWriter: FileWriter) {
    fun generate(blueprint: AndroidBuildGradleBlueprint) {
        val applicationId = if (blueprint.isApplication) "applicationId \"${blueprint.packageName}\"" else ""

        val moduleDependencies = blueprint.dependencies.map { "${it.method.value} project(':${it.name}')\n" }.fold()

        val flavorsSection = createFlavorsSection(blueprint.productFlavors, blueprint.flavorDimensions)

        val gradleText =
                """
${applyPlugins(blueprint.plugins)}
android {
    compileSdkVersion ${blueprint.compileSdkVersion}

    defaultConfig {
        $applicationId
        minSdkVersion ${blueprint.minSdkVersion}
        targetSdkVersion ${blueprint.targetSdkVersion}
        versionCode 1
        versionName "1.0"
        multiDexEnabled true

        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
        ${align(blueprint.buildTypes?.joinToString(separator = "\n") { buildType ->
                    "${buildType.name} {\n" +
                            "    ${align(buildType.body, "    ")}\n" +
                            "}"
                }, "        ")}
    }

    ${align(flavorsSection, "    ")}

    ${if (blueprint.enableDataBinding) "dataBinding {\n        enabled = true\n    }" else ""}

    compileOptions {
        targetCompatibility 1.8
        sourceCompatibility 1.8
    }
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    ${addLibraries(blueprint.libraries)}
    ${align(moduleDependencies.trimEnd(), "    ")}
}
${blueprint.extraLines.joinLines()}
""".trim()

        fileWriter.writeToFile(gradleText, blueprint.path)
    }

    private fun createFlavorsSection(productFlavors: Set<Flavor>?, flavorDimensions: Set<String>?): String {
        if (productFlavors.isNullOrEmpty() && flavorDimensions.isNullOrEmpty()) {
            return ""
        }

        val dimensionsList = flavorDimensions?.joinToString { "\"$it\"" } ?: ""

        val flavorsList = productFlavors?.joinToString(separator = "") {
            "${it.name} {\n" +
                    (if (it.dimension != null) "    dimension \"${it.dimension}\"\n" else "") +
                    "}\n"
        }

        return "flavorDimensions $dimensionsList\n" +
                "productFlavors {\n" +
                "    ${align(flavorsList?.trimEnd(), "    ")}\n" +
                "}"
    }

    private fun align(input: String?, spaces: String): String = input?.lines()?.joinToString(separator = "\n$spaces") ?: ""

    private fun applyPlugins(plugins: Set<String>): String {
        return plugins.map { "apply plugin: '$it'\n" }.fold()
    }

    private fun addLibraries(libraries: Set<LibraryDependency>): String {
        return libraries.map { "${it.method} \"${it.name}\"\n" }.fold()
    }
}
