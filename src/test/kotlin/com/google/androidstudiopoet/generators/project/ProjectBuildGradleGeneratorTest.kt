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

import com.google.androidstudiopoet.models.ProjectBuildGradleBlueprint
import com.google.androidstudiopoet.models.Repository
import com.google.androidstudiopoet.testutils.mock
import com.google.androidstudiopoet.writers.FileWriter
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test

class ProjectBuildGradleGeneratorTest {
    private val fileWriter: FileWriter = mock()
    private val projectBuildGradleGenerator = ProjectBuildGradleGenerator(fileWriter)

    @Test
    fun `generators creates empty repositories and dependencies closures when nothing is provided`() {
        val blueprint = getProjectBuildGradleBlueprint()

        projectBuildGradleGenerator.generate(blueprint)

        val expected = """buildscript {
    repositories {

    }
    dependencies {

    }
}
plugins {
    id 'com.gradle.build-scan' version '1.8'
}
allprojects {
    repositories {

    }
}
task clean(type: Delete) {
    delete rootProject.buildDir
}
buildScan {
    licenseAgreementUrl = 'https://gradle.com/terms-of-service'
    licenseAgree = 'yes'
    tag 'SAMPLE'
    link 'GitHub', 'https://github.com/gradle/gradle-build-scan-quickstart'
}"""

        verify(fileWriter).writeToFile(expected, "path")
    }

    @Test
    fun `generators adds provided repositories`() {
        val blueprint = getProjectBuildGradleBlueprint(
                repositories = setOf(
                        Repository.Named("jcenter"),
                        Repository.Named("google")
                )
        )

        projectBuildGradleGenerator.generate(blueprint)

        val expected = """buildscript {
    repositories {
        jcenter()
        google()
    }
    dependencies {

    }
}
plugins {
    id 'com.gradle.build-scan' version '1.8'
}
allprojects {
    repositories {
        jcenter()
        google()
    }
}
task clean(type: Delete) {
    delete rootProject.buildDir
}
buildScan {
    licenseAgreementUrl = 'https://gradle.com/terms-of-service'
    licenseAgree = 'yes'
    tag 'SAMPLE'
    link 'GitHub', 'https://github.com/gradle/gradle-build-scan-quickstart'
}"""

        verify(fileWriter).writeToFile(expected, "path")
    }

    @Test
    fun `generators adds provided classpaths`() {
        val blueprint = getProjectBuildGradleBlueprint(
                classpaths = setOf(
                        "classpath1",
                        "classpath2"
                )
        )

        projectBuildGradleGenerator.generate(blueprint)

        val expected = """buildscript {
    repositories {

    }
    dependencies {
        classpath "classpath1"
        classpath "classpath2"
    }
}
plugins {
    id 'com.gradle.build-scan' version '1.8'
}
allprojects {
    repositories {

    }
}
task clean(type: Delete) {
    delete rootProject.buildDir
}
buildScan {
    licenseAgreementUrl = 'https://gradle.com/terms-of-service'
    licenseAgree = 'yes'
    tag 'SAMPLE'
    link 'GitHub', 'https://github.com/gradle/gradle-build-scan-quickstart'
}"""

        verify(fileWriter).writeToFile(expected, "path")
    }

    @Test
    fun `generators adds provided kotlinExtStatement`() {
        val blueprint = getProjectBuildGradleBlueprint(
                kotlinExtStatement = "kotlinExtStatement"
        )

        projectBuildGradleGenerator.generate(blueprint)

        val expected = """buildscript {
    kotlinExtStatement
    repositories {

    }
    dependencies {

    }
}
plugins {
    id 'com.gradle.build-scan' version '1.8'
}
allprojects {
    repositories {

    }
}
task clean(type: Delete) {
    delete rootProject.buildDir
}
buildScan {
    licenseAgreementUrl = 'https://gradle.com/terms-of-service'
    licenseAgree = 'yes'
    tag 'SAMPLE'
    link 'GitHub', 'https://github.com/gradle/gradle-build-scan-quickstart'
}"""

        verify(fileWriter).writeToFile(expected, "path")
    }

    private fun getProjectBuildGradleBlueprint(
            kotlinExtStatement: String? = null,
            repositories: Set<Repository.Named> = setOf(),
            classpaths: Set<String> = setOf()
    ): ProjectBuildGradleBlueprint {
        val blueprint: ProjectBuildGradleBlueprint = mock()
        whenever(blueprint.kotlinExtStatement).thenReturn(kotlinExtStatement)
        whenever(blueprint.repositories).thenReturn(repositories)
        whenever(blueprint.classpaths).thenReturn(classpaths)
        whenever(blueprint.path).thenReturn("path")
        return blueprint
    }
}