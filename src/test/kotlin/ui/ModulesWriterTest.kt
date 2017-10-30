package ui

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import ui.test_utils.mock

class ModulesWriterTest {

    val fileWriter: FileWriter = mock()
    val dependencyValidator: DependencyValidator = mock()
    val moduleBlueprinFactory: ModuleBlueprintFactory = mock()
    val buildGradleCreator: BuildGradleCreator = mock()
    val modulesWriter = ModulesWriter(dependencyValidator, moduleBlueprinFactory, buildGradleCreator, fileWriter)

    @Test(expected = IllegalStateException::class)
    fun `generate throws ISE when input is invalid`() {
        whenever(dependencyValidator.isValid(any())).thenReturn(false)
        modulesWriter.generate("{}")
    }
}