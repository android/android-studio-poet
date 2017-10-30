package ui.generators.project

import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption

object GradlewGenerator {

    fun generateGradleW(root: String) {

        val gradlew = "gradlew"
        val gradlewbat = "gradlew.bat"

        Files.copy(FileInputStream(File(gradlew)), File(root, gradlew).toPath(), StandardCopyOption.REPLACE_EXISTING)
        Files.copy(FileInputStream(File(gradlewbat)),File(root, gradlewbat).toPath(), StandardCopyOption.REPLACE_EXISTING)
    }
}
