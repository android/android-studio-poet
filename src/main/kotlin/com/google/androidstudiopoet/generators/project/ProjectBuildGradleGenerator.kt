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

import com.google.androidstudiopoet.generators.toClasspathExpression
import com.google.androidstudiopoet.generators.toExpression
import com.google.androidstudiopoet.gradle.*
import com.google.androidstudiopoet.models.ProjectBuildGradleBlueprint
import com.google.androidstudiopoet.writers.FileWriter

class ProjectBuildGradleGenerator(val fileWriter: FileWriter) {

    fun generate(blueprint: ProjectBuildGradleBlueprint) {

        val statements = listOf(
                getBuildscriptClosure(blueprint),
                getPluginsClosure(),
                getAllprojectsClosure(blueprint),
                getCleanTask(),
                getBuildScanClosure())

        val gradleText = statements.joinToString(separator = "\n") { it.toGroovy(0) }

        fileWriter.writeToFile(gradleText, blueprint.path)
    }

    private fun getBuildscriptClosure(blueprint: ProjectBuildGradleBlueprint): Closure {
        return Closure("buildscript", listOfNotNull(
                blueprint.kotlinExtStatement?.let { StringStatement(it) },
                getRespositoriesClosure(blueprint),
                getClasspathDependenciesClosure(blueprint)
        ))
    }

    private fun getRespositoriesClosure(blueprint: ProjectBuildGradleBlueprint): Closure {
        val repositoriesExpressions = blueprint.repositories.map { it.toExpression() }
        return Closure("repositories", repositoriesExpressions)
    }

    private fun getClasspathDependenciesClosure(blueprint: ProjectBuildGradleBlueprint): Closure {
        return Closure("dependencies", blueprint.classpaths.map { it.toClasspathExpression() })
    }

    private fun getAllprojectsClosure(blueprint: ProjectBuildGradleBlueprint) =
            Closure("allprojects", listOf(getRespositoriesClosure(blueprint)))

    private fun getBuildScanClosure() = Closure("buildScan", listOf(
            StringStatement("licenseAgreementUrl = 'https://gradle.com/terms-of-service'"),
            StringStatement("licenseAgree = 'yes'"),
            Expression("tag", "'SAMPLE'"),
            Expression("link", "'GitHub', 'https://github.com/gradle/gradle-build-scan-quickstart'")
    ))

    private fun getPluginsClosure() = Closure("plugins", listOf(
            StringStatement("id 'com.gradle.build-scan' version '1.8'")
    ))

    private fun getCleanTask() = Task("clean",
            listOf(TaskParameter("type", "Delete")),
            listOf(Expression("delete", "rootProject.buildDir")))
}
