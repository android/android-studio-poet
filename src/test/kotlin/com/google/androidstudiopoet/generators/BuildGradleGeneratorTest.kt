package com.google.androidstudiopoet.generators

import com.google.androidstudiopoet.input.ModuleBuildGradleBlueprint
import com.google.androidstudiopoet.models.LibraryDependency
import com.google.androidstudiopoet.models.ModuleDependency
import com.google.androidstudiopoet.testutils.mock
import com.google.androidstudiopoet.writers.FileWriter
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test

class BuildGradleGeneratorTest {
    private val fileWriter: FileWriter = mock()
    private val buildGradleGenerator = BuildGradleGenerator(fileWriter)

    @Test
    fun `generator applies plugins from the blueprint`() {
        val blueprint = getModuleBuildGradleBlueprint(plugins = setOf("plugin1", "plugin2"))
        buildGradleGenerator.generate(blueprint)
        val expected = """apply plugin: 'plugin1'
apply plugin: 'plugin2'
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
}
sourceCompatibility = "1.8"
targetCompatibility = "1.8""""
        verify(fileWriter).writeToFile(expected, "path")
    }

    @Test
    fun `generator applies libraries from the blueprint`() {
        val blueprint = getModuleBuildGradleBlueprint(libraries = setOf(
                LibraryDependency("compile", "library1"),
                LibraryDependency("testCompile", "library2")
        ))
        buildGradleGenerator.generate(blueprint)
        val expected = """dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile "library1"
    testCompile "library2"
}
sourceCompatibility = "1.8"
targetCompatibility = "1.8""""
        verify(fileWriter).writeToFile(expected, "path")
    }

    @Test
    fun `generator applies dependencies from the blueprint`() {
        val blueprint = getModuleBuildGradleBlueprint(dependencies = setOf(
                ModuleDependency("library1", mock(),"compile"),
                ModuleDependency("library2", mock(),"testCompile")
        ))
        buildGradleGenerator.generate(blueprint)
        val expected = """dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile project(':library1')
    testCompile project(':library2')
}
sourceCompatibility = "1.8"
targetCompatibility = "1.8""""
        verify(fileWriter).writeToFile(expected, "path")
    }

    @Test
    fun `generator applies extralines from the blueprint`() {
        val blueprint = getModuleBuildGradleBlueprint(extraLines = listOf(
                "line1",
                "line2"
        ))
        buildGradleGenerator.generate(blueprint)
        val expected = """dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
}
sourceCompatibility = "1.8"
targetCompatibility = "1.8"
line1
line2"""
        verify(fileWriter).writeToFile(expected, "path")
    }

    private fun getModuleBuildGradleBlueprint(
            plugins: Set<String> = setOf(),
            libraries: Set<LibraryDependency> = setOf(),
            dependencies: Set<ModuleDependency> = setOf(),
            extraLines: List<String>? = null
    ): ModuleBuildGradleBlueprint {
        return mock<ModuleBuildGradleBlueprint>().apply {
            whenever(this.dependencies).thenReturn(dependencies)
            whenever(this.plugins).thenReturn(plugins)
            whenever(this.libraries).thenReturn(libraries)
            whenever(this.extraLines).thenReturn(extraLines)
            whenever(this.path).thenReturn("path")
        }
    }
}