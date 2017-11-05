package main.generators.project

import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption

internal const val ASSETS_PATH = "src/main/assets"

class ProjectBuildGradleGenerator {

    fun generate(root: String) {

        val buildGradle = "build.gradle"

        val gradleWFile = File(root, buildGradle).toPath()
        Files.copy(FileInputStream(File(ASSETS_PATH, buildGradle)), gradleWFile, StandardCopyOption.REPLACE_EXISTING)
    }
}
