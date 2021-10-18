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

import com.google.androidstudiopoet.utils.joinPath
import com.google.androidstudiopoet.writers.FileWriter

private const val INCLUDE_LENGTH_LIMIT = 250
class GradleSettingsGenerator(private val fileWriter: FileWriter) {

    fun generate(projectName: String, allModulesNames: List<String>, projectRoot: String) {
        val settingsGradleContent = buildString {
            appendLine("""
              plugins {
                id "com.gradle.enterprise" version "3.7"
              }
              gradleEnterprise {
                buildScan {
                  termsOfServiceUrl = "https://gradle.com/terms-of-service"
                  termsOfServiceAgree = "yes"
                }
              }
            """.trimIndent())
            appendLine()
            appendLine("rootProject.name = \'$projectName\'")
            append(generateIncludeStatements(allModulesNames))
        }
        fileWriter.writeToFile(settingsGradleContent, projectRoot.joinPath("settings.gradle"))
    }

    private fun generateIncludeStatements(moduleBlueprints: List<String>) =
            moduleBlueprints.asSequence()
                    .map { "\'$it\'" }
                    .foldIndexed("", { index, acc, next ->
                        acc + getIncludeStatementOrComma(index) + next
                    })

    private fun getIncludeStatementOrComma(index: Int): String {
        return if (index.rem(INCLUDE_LENGTH_LIMIT) == 0) {
            "\ninclude "
        } else ","
    }
}
