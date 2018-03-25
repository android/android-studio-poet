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

package com.google.androidstudiopoet.generators.project

import com.google.androidstudiopoet.generators.toExpression
import com.google.androidstudiopoet.gradle.Closure
import com.google.androidstudiopoet.models.ProjectBlueprint
import com.google.androidstudiopoet.models.ProjectBuildGradleBlueprint
import com.google.androidstudiopoet.utils.joinPath
import com.google.androidstudiopoet.writers.FileWriter

class ProjectBuildGradleGenerator(val fileWriter: FileWriter) {

    fun generate(projectBlueprint: ProjectBlueprint, blueprint: ProjectBuildGradleBlueprint) {

        val (kotlinBuildScript, kotlinClasspath) = if (projectBlueprint.useKotlin) {

            "ext.kotlin_version = '${projectBlueprint.kotlinVersion}'" to """classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:${'$'}kotlin_version""""
        } else {
            Pair("", "")
        }

        val gradleText = """buildscript {
    $kotlinBuildScript
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:${projectBlueprint.androidGradlePluginVersion}'
        $kotlinClasspath
    }
}
allprojects {
    repositories {
        google()
        jcenter()
    }
}
plugins {
    id 'com.gradle.build-scan' version '1.8'

}

task clean(type: Delete) {
    delete rootProject.buildDir
}

buildScan {
 licenseAgreementUrl = 'https://gradle.com/terms-of-service'
 licenseAgree = 'yes'
 tag 'SAMPLE'
 link 'GitHub', 'https://github.com/gradle/gradle-build-scan-quickstart'
}

""".trim()

        fileWriter.writeToFile(gradleText, blueprint.root.joinPath("build.gradle"))
    }

    private fun getRespositoriesClosure(blueprint: ProjectBuildGradleBlueprint): Closure {
        val repositoriesExpressions = blueprint.repositories.map { it.toExpression() }
        return Closure("respositories", repositoriesExpressions)
    }

    private fun getAllprojectsClosure(blueprint: ProjectBuildGradleBlueprint) =
            Closure("allprojects", listOf(getRespositoriesClosure(blueprint)))
}
