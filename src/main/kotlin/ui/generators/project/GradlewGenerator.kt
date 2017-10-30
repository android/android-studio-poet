package ui.generators.project

import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption

object GradlewGenerator {

    fun generateGradleW(root: String) {

        val gradlew = "gradlew"
        val gradlewbat = "gradlew.bat"

        val gradleWFile = File(root, gradlew).toPath()
        Files.copy(FileInputStream(File(gradlew)), gradleWFile, StandardCopyOption.REPLACE_EXISTING)
        Files.copy(FileInputStream(File(gradlewbat)), File(root, gradlewbat).toPath(), StandardCopyOption.REPLACE_EXISTING)

        Runtime.getRuntime().exec("chmod u+x " + gradleWFile)

        val gradleFolder = File(root, "gradle")
        gradleFolder.mkdir()
        val gradleWrapperFolder = File(gradleFolder, "wrapper")
        gradleWrapperFolder.mkdir()

        val sourceFolder = File("gradle", "wrapper")

        val gradleWrapperJar = "gradle-wrapper.jar"
        val gradleWrapperProperties = "gradle-wrapper.properties"

        Files.copy(FileInputStream(File(sourceFolder, gradleWrapperJar)),
                File(gradleWrapperFolder, gradleWrapperJar).toPath(), StandardCopyOption.REPLACE_EXISTING)
        Files.copy(FileInputStream(File(sourceFolder, gradleWrapperProperties)),
                File(gradleWrapperFolder, gradleWrapperProperties).toPath(), StandardCopyOption.REPLACE_EXISTING)
    }
}
