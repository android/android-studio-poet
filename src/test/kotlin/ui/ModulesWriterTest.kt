package ui

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import ui.generators.BuildGradleGenerator
import ui.generators.PackagesGenerator
import ui.generators.project.GradleSettingsGenerator
import ui.generators.project.ProjectBuildGradleGenerator
import ui.test_utils.mock
import ui.writers.AndroidModuleWriter
import ui.writers.FileWriter
import ui.writers.SourceModuleWriter

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