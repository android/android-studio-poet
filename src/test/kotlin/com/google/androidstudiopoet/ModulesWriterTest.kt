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

package com.google.androidstudiopoet

import com.google.androidstudiopoet.generators.BuildGradleGenerator
import com.google.androidstudiopoet.generators.PackagesGenerator
import com.google.androidstudiopoet.generators.project.GradleSettingsGenerator
import com.google.androidstudiopoet.generators.project.ProjectBuildGradleGenerator
import com.google.androidstudiopoet.models.ProjectBlueprint
import com.google.androidstudiopoet.test_utils.mock
import com.google.androidstudiopoet.generators.android_modules.AndroidModuleGenerator
import com.google.androidstudiopoet.writers.FileWriter
import com.google.androidstudiopoet.writers.SourceModuleWriter
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test

class ModulesWriterTest {

    val fileWriter: FileWriter = mock()
    val projectBuildGradleGenerator: ProjectBuildGradleGenerator = mock()
    val gradleSettingsGenerator: GradleSettingsGenerator = mock()
    val dependencyValidator: DependencyValidator = mock()
    val moduleBlueprinFactory: ModuleBlueprintFactory = mock()
    val projectBlueprint: ProjectBlueprint = mock()
    val buildGradleGenerator: BuildGradleGenerator = mock()
    val androidModuleGenerator: AndroidModuleGenerator = mock()
    val packagesGenerator: PackagesGenerator = mock()
    val modulesWriter = SourceModuleWriter(dependencyValidator,
            buildGradleGenerator,
            gradleSettingsGenerator,
            projectBuildGradleGenerator,
            androidModuleGenerator,
            packagesGenerator,
            fileWriter
    )

    @Test(expected = IllegalStateException::class)
    fun `generate throws ISE when input is invalid`() {
        whenever(dependencyValidator.isValid(any())).thenReturn(false)
        modulesWriter.generate(projectBlueprint)
    }
}