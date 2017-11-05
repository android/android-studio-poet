package main

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import main.generators.BuildGradleGenerator
import main.generators.PackagesGenerator
import main.generators.project.GradleSettingsGenerator
import main.generators.project.ProjectBuildGradleGenerator
import main.test_utils.mock
import main.writers.AndroidModuleWriter
import main.writers.FileWriter
import main.writers.SourceModuleWriter

class ModulesWriterTest {

    val fileWriter: FileWriter = mock()
    val projectBuildGradleGenerator: ProjectBuildGradleGenerator = mock()
    val gradleSettingsGenerator: GradleSettingsGenerator = mock()
    val dependencyValidator: DependencyValidator = mock()
    val moduleBlueprinFactory: ModuleBlueprintFactory = mock()
    val buildGradleGenerator: BuildGradleGenerator = mock()
    val androidModuleGenerator: AndroidModuleWriter = mock()
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