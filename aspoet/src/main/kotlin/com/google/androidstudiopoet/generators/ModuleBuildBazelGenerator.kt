/*
Copyright 2018 Google Inc.

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

import com.google.androidstudiopoet.models.FileTreeDependency
import com.google.androidstudiopoet.models.ModuleBuildBazelBlueprint
import com.google.androidstudiopoet.models.ModuleDependency
import com.google.androidstudiopoet.writers.FileWriter

class ModuleBuildBazelGenerator(private val fileWriter: FileWriter) {
    fun generate(blueprint: ModuleBuildBazelBlueprint) {
        val deps: Set<String> = blueprint.dependencies.map {
            when (it) {
                is ModuleDependency -> "\"//${it.name}\""
                is FileTreeDependency -> "\":imported\""
                else -> ""
            }
        }.toSet()
        var imported = blueprint.dependencies.firstOrNull() {
          it is FileTreeDependency
        }?.let { it ->
          val dep = it as FileTreeDependency
          """java_import(
    name = "imported",
    constraints = ["android"],
    jars = glob(["${dep.dir}/${dep.include}}"]),
)"""
      } ?: ""
        val depsString = """
    deps = [
        ${deps.joinToString(separator = ",\n        ") { it }}
    ],"""
        val ruleClass = "java_library"
        val targetName = blueprint.targetName
        val ruleDefinition = """$ruleClass(
    name = "$targetName",
    srcs = glob(["src/main/java/**/*.java"]),
    visibility = ["//visibility:public"],${if (deps.isNotEmpty()) depsString else ""}
)
$imported"""

        fileWriter.writeToFile(ruleDefinition, blueprint.path)
    }
}
