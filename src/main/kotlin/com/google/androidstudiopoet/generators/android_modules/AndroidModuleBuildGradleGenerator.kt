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

import com.google.androidstudiopoet.joinLines
import com.google.androidstudiopoet.models.AndroidModuleBlueprint
import com.google.androidstudiopoet.models.Flavor
import com.google.androidstudiopoet.utils.fold
import com.google.androidstudiopoet.utils.isNullOrEmpty
import com.google.androidstudiopoet.utils.joinPath
import com.google.androidstudiopoet.writers.FileWriter

class AndroidModuleBuildGradleGenerator(val fileWriter: FileWriter) {
    fun generate(blueprint: AndroidModuleBlueprint) {
        val moduleRoot = blueprint.moduleRoot

        val androidPlugin = if (blueprint.hasLaunchActivity) "application" else "library"
        val applicationId = if (blueprint.hasLaunchActivity) "applicationId \"${blueprint.packageName}\"" else ""

        val moduleDependencies = blueprint.dependencies.map { "implementation project(':${it.name}')\n" }.fold()

        val flavorsSection = createFlavorsSection(blueprint.productFlavors, blueprint.flavorDimensions)

        val gradleText =
"""
apply plugin: 'com.android.$androidPlugin'
${if (blueprint.useKotlin) "apply plugin: 'kotlin-android'" else ""}
${if (blueprint.useKotlin) "apply plugin: 'kotlin-android-extensions'" else ""}

android {
    compileSdkVersion 26

    defaultConfig {
        $applicationId
        minSdkVersion 19
        targetSdkVersion 26
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
            "    ${align(buildType.body,"    ")}\n" +
            "}"
        }, "        ")}
    }

    ${align(flavorsSection, "    ")}

    compileOptions {
        targetCompatibility 1.8
        sourceCompatibility 1.8
    }
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    ${if (blueprint.useKotlin) "implementation \"org.jetbrains.kotlin:kotlin-stdlib-jre7:${'$'}kotlin_version\"" else ""}
    implementation 'com.android.support:appcompat-v7:26.1.0'
    implementation 'com.android.support.constraint:constraint-layout:1.0.2'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:1.0.1'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.1'
    implementation "com.android.support:multidex:1.0.1"

    ${align(moduleDependencies.trimEnd(),"    ")}
}
${blueprint.extraLines.joinLines()}
""".trim()

        fileWriter.writeToFile(gradleText, moduleRoot.joinPath("build.gradle"))
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
               "    ${align(flavorsList?.trimEnd(),"    ")}\n" +
               "}"
    }

    private fun align(input: String?, spaces: String): String = if (input != null) input.lines().joinToString(separator = "\n$spaces") else ""
}
