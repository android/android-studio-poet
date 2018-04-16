package com.google.androidstudiopoet.generators

import com.google.androidstudiopoet.models.AbstractModuleBlueprint
import com.google.androidstudiopoet.models.AndroidModuleBlueprint
import com.google.androidstudiopoet.models.ModuleBlueprint
import com.google.androidstudiopoet.models.ProjectBlueprint
import com.google.androidstudiopoet.utils.joinPath
import com.google.androidstudiopoet.writers.ImageWriter
import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage

class DependencyImageGenerator(private val imageWriter: ImageWriter) {
    companion object {
        const val CELL_SIZE = 3
        const val LINE_WIDTH = 1
        const val HEADER_SIZE = CELL_SIZE + 2 * LINE_WIDTH
        const val GRID_SIZE = CELL_SIZE + LINE_WIDTH
        val BACKGROUND_COLOR = Color(0x000000)
        val GRID_COLOR = Color(0x8F8F8F)
        val APP_COLOR = Color(0x5050FF)
        val ANDROID_COLOR = Color(0xA4C639)
        val JAVA_COLOR = Color(0xF7DB64)
        val ERROR_COLOR = Color(0xDF0000)
        // Colors match https://docs.gradle.org/current/userguide/java_library_plugin.html#sec:java_library_configurations_graph
        val dependencyMethodToColor = mapOf(
                "api" to Color(0x00AF00),
                "implementation" to Color(0x00AF00),
                "runtimeOnly" to Color(0x00AF00),
                "testImplementation" to Color(0x00AF00),
                "testRuntimeOnly" to Color(0x00AF00),

                "apiElements" to Color(0xFFA9C4),
                "runtimeElements" to Color(0xFFA9C4),

                "compileClassPath" to Color(0x35D9F0),
                "runtimeClassPath" to Color(0x35D9F0),
                "testCompileClassPath" to Color(0x35D9F0),
                "testRuntimeClassPath" to Color(0x35D9F0),

                "compileOnly" to Color(0xFFFFFF),
                "testCompileOnly" to Color(0xFFFFFF)
        )
        val DEPENDENCY_MULTIPLE_METHOD_COLOR = Color(0x9F009F)
        val DEPENDENCY_UNKNOWN_METHOD_COLOR = Color(0xDF0000)
    }

    /**
     * generates an image that represents the dependency matrix of the project
     */
    fun generate(blueprint: ProjectBlueprint) {
        val numModules = blueprint.allModuleBlueprints.size
        val imageSize = HEADER_SIZE + LINE_WIDTH + numModules * GRID_SIZE

        // Create buffered image object
        val img = BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB)
        val graphics = img.graphics

        // Background
        graphics.color = BACKGROUND_COLOR
        graphics.fillRect(0, 0, imageSize, imageSize)

        // Generate the grid
        graphics.color = GRID_COLOR
        for (i in 0..numModules) {
            // Horizontal
            val y = HEADER_SIZE + i * GRID_SIZE
            graphics.fillRect(HEADER_SIZE, y, imageSize - HEADER_SIZE, LINE_WIDTH)
            // Vertical
            val x = HEADER_SIZE + i * GRID_SIZE
            graphics.fillRect(x, HEADER_SIZE, LINE_WIDTH, imageSize - HEADER_SIZE)
        }

        // Add modules headers and generate indexes to use in dependencies
        val moduleNameToIndex = mutableMapOf<String, Int>()
        blueprint.allModuleBlueprints.withIndex().forEach{(index, blueprint) ->
            moduleNameToIndex[blueprint.name] = index
            graphics.drawHeader(index, getColorForModule(blueprint))
        }

        // Add dependencies
        blueprint.allDependencies.forEach { (moduleName, dependencies) ->
            // Split dependencies by name
            val seenDependencies = mutableMapOf<Int?, MutableSet<String>>()
            dependencies.forEach {
                seenDependencies.getOrPut(moduleNameToIndex[it.name]) { mutableSetOf() }.add(it.method)
            }

            // Draw dependencies
            val index = moduleNameToIndex[moduleName]!!
            seenDependencies.forEach { (dependencyIndex, methodSet) ->
                if (dependencyIndex != null) {
                    graphics.drawCell(index, dependencyIndex, getColorForDependencySet(methodSet))
                }
                else {
                    graphics.drawHeader(index, ERROR_COLOR)
                }
            }
        }

        // Save to file
        val imgPath = blueprint.projectRoot.joinPath("dependencies.png")
        imageWriter.writeToFile(img, imgPath)
        println("Dependency matrix image saved to $imgPath")
    }

    private fun getColorForDependencySet(dependencies: Set<String>) =
            when (dependencies.size) {
                0 ->
                    BACKGROUND_COLOR
                1 ->
                    dependencyMethodToColor.getOrDefault(dependencies.first(), DEPENDENCY_UNKNOWN_METHOD_COLOR)
                else ->
                    DEPENDENCY_MULTIPLE_METHOD_COLOR
            }

    private fun getColorForModule(blueprint: AbstractModuleBlueprint): Color =
        when (blueprint) {
            is AndroidModuleBlueprint ->
                if (blueprint.hasLaunchActivity) {
                    APP_COLOR
                }
                else {
                    ANDROID_COLOR
                }
            is ModuleBlueprint ->
                JAVA_COLOR
            else ->
                ERROR_COLOR
        }

    private fun Graphics.drawCell(r: Int, c: Int, color: Color) {
        val x = HEADER_SIZE + LINE_WIDTH + c * GRID_SIZE
        val y = HEADER_SIZE + LINE_WIDTH + r * GRID_SIZE
        this.color = color
        this.fillRect(x, y, CELL_SIZE, CELL_SIZE)
    }

    private fun Graphics.drawHeader(i: Int, color: Color) {
        val y = HEADER_SIZE + LINE_WIDTH + GRID_SIZE * (i)
        this.color = color
        this.fillRect(LINE_WIDTH, y, CELL_SIZE, CELL_SIZE)
        this.fillRect(y, LINE_WIDTH, CELL_SIZE, CELL_SIZE)
    }
}