package com.google.androidstudiopoet

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import com.google.androidstudiopoet.generators.BuildGradleGenerator
import com.google.androidstudiopoet.generators.PackagesGenerator
import com.google.androidstudiopoet.generators.project.GradleSettingsGenerator
import com.google.androidstudiopoet.generators.project.ProjectBuildGradleGenerator
import com.google.androidstudiopoet.test_utils.mock
import com.google.androidstudiopoet.generators.AndroidModuleGenerator
import com.google.androidstudiopoet.writers.FileWriter
import com.google.androidstudiopoet.writers.SourceModuleWriter

class ModulesWriterTest {

    val fileWriter: FileWriter = mock()
    val projectBuildGradleGenerator: ProjectBuildGradleGenerator = mock()
    val gradleSettingsGenerator: GradleSettingsGenerator = mock()
    val dependencyValidator: DependencyValidator = mock()
    val moduleBlueprinFactory: ModuleBlueprintFactory = mock()
    val buildGradleGenerator: BuildGradleGenerator = mock()
    val androidModuleGenerator: AndroidModuleGenerator = mock()
    val packagesGenerator: PackagesGenerator = mock()
    val modulesWriter = SourceModuleWriter(dependencyValidator,
            moduleBlueprinFactory,
            buildGradleGenerator,
            gradleSettingsGenerator,
            projectBuildGradleGenerator,
            androidModuleGenerator,
            packagesGenerator,
            fileWriter)

    @Test(expected = IllegalStateException::class)
    fun `generate throws ISE when input is invalid`() {
        whenever(dependencyValidator.isValid(any())).thenReturn(false)
        modulesWriter.generate("{}")
    }
}