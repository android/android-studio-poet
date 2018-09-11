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

package com.google.androidstudiopoet.generators

import com.google.androidstudiopoet.gradle.Closure
import com.google.androidstudiopoet.gradle.Expression
import com.google.androidstudiopoet.gradle.Statement
import com.google.androidstudiopoet.gradle.StringStatement
import com.google.androidstudiopoet.models.ModuleBuildGradleBlueprint
import com.google.androidstudiopoet.models.ModuleBuildSpecificationBlueprint
import com.google.androidstudiopoet.writers.FileWriter

class ModuleBuildGradleGenerator(private val fileWriter: FileWriter) {
    fun generate(blueprint: ModuleBuildGradleBlueprint) {
        val statements = applyPlugins(blueprint.plugins) +
                dependenciesClosure(blueprint) +
                codeCompatibilityStatements() +
                (blueprint.extraLines?.map { StringStatement(it) } ?: listOf())

        val gradleText = statements.joinToString(separator = "\n") { it.toGroovy(0) }

        fileWriter.writeToFile(gradleText, blueprint.path)
    }

    private fun applyPlugins(plugins: Set<String>): List<Statement> {
        return plugins.map { it.toApplyPluginExpression() }
    }

    private fun dependenciesClosure(blueprint: ModuleBuildSpecificationBlueprint): Closure {
        val dependencyExpressions: Set<Statement> = blueprint.dependencies.mapNotNull { it.toExpression() }.toSet()

        val statements = listOf(Expression("compile", "fileTree(dir: 'libs', include: ['*.jar'])")) +
                dependencyExpressions
        return Closure("dependencies", statements)
    }

    private fun codeCompatibilityStatements(): List<Statement> {
        return listOf(
                StringStatement("sourceCompatibility = \"1.8\""),
                StringStatement("targetCompatibility = \"1.8\"")
        )
    }
}
