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

import com.google.androidstudiopoet.models.*
import com.google.androidstudiopoet.testutils.mock
import com.google.androidstudiopoet.writers.FileWriter
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test

class ModuleBuildBazelGeneratorTest {
    private val fileWriter: FileWriter = mock()
    private val buildBazelGenerator = ModuleBuildBazelGenerator(fileWriter)

    @Test
    fun `generator applies dependencies from the blueprint`() {
        val blueprint = getModuleBuildBazelBlueprint(dependencies = setOf(
                ModuleDependency("library1", mock(),"unused"),
                ModuleDependency("library2", mock(),"unused")
        ))
        buildBazelGenerator.generate(blueprint)
        val expected = """java_library(
    name = "target_name",
    srcs = glob(["src/main/java/**/*.java"]),
    visibility = ["//visibility:public"],
    deps = [
        "//library1",
        "//library2"
    ],
)"""
        verify(fileWriter).writeToFile(expected, "BUILD.bazel")
    }

    private fun getModuleBuildBazelBlueprint(dependencies: Set<Dependency> = setOf()): ModuleBuildBazelBlueprint {
        return mock<ModuleBuildBazelBlueprint>().apply {
            whenever(this.moduleName).thenReturn("target_name")
            whenever(this.dependencies).thenReturn(dependencies)
            whenever(this.path).thenReturn("BUILD.bazel")
        }
    }
}