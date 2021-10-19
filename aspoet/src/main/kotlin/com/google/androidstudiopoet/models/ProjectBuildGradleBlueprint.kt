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

import com.google.androidstudiopoet.input.RepositoryConfig
import com.google.androidstudiopoet.utils.joinPath

class ProjectBuildGradleBlueprint(root: String, enableKotlin: Boolean, androidGradlePluginVersion: String,
                                  kotlinVersion: String, additionalRepositories: List<RepositoryConfig>?,
                                  additionalClasspaths: List<String>?) {

    val path = root.joinPath("build.gradle")
    val kotlinExtStatement = if (enableKotlin) "ext.kotlin_version = '$kotlinVersion'" else null

    val classpaths: Set<String> by lazy {
        listOfNotNull(
                "com.android.tools.build:gradle:$androidGradlePluginVersion",
                if (enableKotlin) "org.jetbrains.kotlin:kotlin-gradle-plugin:${'$'}kotlin_version" else null,
                *additionalClasspaths?.toTypedArray().orEmpty()
        ).toSet()
    }

    val buildScriptRepositories = setOf(
        Repository.Named("google"),
        Repository.Named("mavenCentral"),
        Repository.Named("gradlePluginPortal")
    ) + additionalRepositories.orEmpty().map { Repository.Remote(it.url) }.toSet()

    val repositories = setOf(
            Repository.Named("google"),
            Repository.Named("mavenCentral")
    ) + additionalRepositories.orEmpty().map { Repository.Remote(it.url) }.toSet()
}
