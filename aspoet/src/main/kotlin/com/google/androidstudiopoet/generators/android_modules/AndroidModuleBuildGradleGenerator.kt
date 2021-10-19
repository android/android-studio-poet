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

import com.google.androidstudiopoet.generators.toApplyPluginExpression
import com.google.androidstudiopoet.generators.toExpression
import com.google.androidstudiopoet.gradle.Closure
import com.google.androidstudiopoet.gradle.Expression
import com.google.androidstudiopoet.gradle.Statement
import com.google.androidstudiopoet.gradle.StringStatement
import com.google.androidstudiopoet.models.*
import com.google.androidstudiopoet.utils.isNullOrEmpty
import com.google.androidstudiopoet.writers.FileWriter

class AndroidModuleBuildGradleGenerator(val fileWriter: FileWriter) {
    fun generate(blueprint: AndroidBuildGradleBlueprint) {

        val statements = applyPlugins(blueprint.plugins) +
                androidClosure(blueprint) +
                dependenciesClosure(blueprint) +
                additionalTasksClosures(blueprint) +
                (blueprint.extraLines?.map { StringStatement(it) } ?: listOf())

        val gradleText = statements.joinToString(separator = "\n") { it.toGroovy(0) }

        fileWriter.writeToFile(gradleText, blueprint.path)
    }

    private fun applyPlugins(plugins: Set<String>): List<Statement> {
        return plugins.map { it.toApplyPluginExpression() }
    }

    private fun androidClosure(blueprint: AndroidBuildGradleBlueprint): Closure {
        val statements = listOfNotNull(
                Expression("compileSdkVersion", "${blueprint.compileSdkVersion}"),
                defaultConfigClosure(blueprint),
                buildTypesClosure(blueprint),
                kotlinOptionsClosure(blueprint),
                buildFeaturesClosure(blueprint),
                compileOptionsClosure()
        ) + createFlavorsSection(blueprint.productFlavors, blueprint.flavorDimensions)

        return Closure("android", statements)
    }

    private fun defaultConfigClosure(blueprint: AndroidBuildGradleBlueprint): Closure {
        val expressions = listOfNotNull(
                if (blueprint.isApplication) Expression("applicationId", "\"${blueprint.packageName}\"") else null,
                Expression("minSdkVersion", "${blueprint.minSdkVersion}"),
                Expression("targetSdkVersion", "${blueprint.targetSdkVersion}"),
                Expression("versionCode", "1"),
                Expression("versionName", "\"1.0\""),
                Expression("multiDexEnabled", "true"),
                Expression("testInstrumentationRunner", "\"android.support.test.runner.AndroidJUnitRunner\"")
        )
        return Closure("defaultConfig", expressions)
    }

    private fun buildTypesClosure(blueprint: AndroidBuildGradleBlueprint): Statement {

        val releaseClosure = Closure("release", listOf(
                Expression("minifyEnabled", "false"),
                Expression("proguardFiles", "getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'")
        ))

        val buildTypesClosures = blueprint.buildTypes?.map {
            Closure(it.name, it.body?.lines()?.map { line -> StringStatement(line) } ?: listOf())
        } ?: listOf()

        return Closure("buildTypes", listOf(releaseClosure) + buildTypesClosures)
    }

    private fun createFlavorsSection(productFlavors: Set<Flavor>?, flavorDimensions: Set<String>?): List<Statement> {
        if (productFlavors.isNullOrEmpty() && flavorDimensions.isNullOrEmpty()) {
            return listOf()
        }

        val flavorDimensionsExpression = flavorDimensions?.joinToString { "\"$it\"" }?.let { Expression("flavorDimensions", it) }

        val flavorsList = productFlavors!!.map {
            Closure(it.name, listOfNotNull(
                    it.dimension?.let { dimensionName -> Expression("dimension", "\"$dimensionName\"") }
            ))
        }

        return listOfNotNull(
                flavorDimensionsExpression,
                Closure("productFlavors", flavorsList)
        )
    }

    private fun buildFeaturesClosure(blueprint: AndroidBuildGradleBlueprint): Closure? {
        val statements = mutableListOf<StringStatement>()
        if (blueprint.enableDataBinding) {
            statements.add(StringStatement("dataBinding true"))
        } else if (blueprint.enableCompose) {
            statements.add(StringStatement("compose true"))
        }
        return if (statements.isNotEmpty()) Closure("buildFeatures", statements) else null
    }

    private fun kotlinOptionsClosure(blueprint: AndroidBuildGradleBlueprint): Closure? {
        return if (blueprint.enableCompose) {
            Closure("kotlinOptions", listOf(
                    StringStatement("jvmTarget = '1.8'"),
                    StringStatement("useIR = true")
            ))
        } else {
            null
        }
    }

    private fun compileOptionsClosure(): Closure {
        val expressions = listOf(
                Expression("targetCompatibility", "1.8"),
                Expression("sourceCompatibility", "1.8")
        )
        return Closure("compileOptions", expressions)
    }

    private fun dependenciesClosure(blueprint: AndroidBuildGradleBlueprint): Closure {
        val dependencyExpressions: Set<Statement> = blueprint.dependencies.mapNotNull { it.toExpression() }.toSet()

        val statements = dependencyExpressions.toList()
        return Closure("dependencies", statements)
    }

    private fun additionalTasksClosures(blueprint: AndroidBuildGradleBlueprint): Set<Closure> =
            blueprint.additionalTasks.map { task ->
                Closure(
                        task.name,
                        task.body?.map { StringStatement(it) } ?: listOf())
            }.toSet()
}

