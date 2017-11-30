package com.google.androidstudiopoet.generators.project

import com.google.androidstudiopoet.models.ConfigPOJO
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption

object GradlewGenerator {

    fun generateGradleW(root: String, configPOJO: ConfigPOJO) {

        val gradlew = "gradlew"
        val gradlewbat = "gradlew.bat"

        val gradleWFile = File(root, gradlew).toPath()
        Files.copy(FileInputStream(File(ASSETS_PATH, gradlew)), gradleWFile, StandardCopyOption.REPLACE_EXISTING)
        Files.copy(FileInputStream(File(ASSETS_PATH, gradlewbat)), File(root, gradlewbat).toPath(), StandardCopyOption.REPLACE_EXISTING)

        Runtime.getRuntime().exec("chmod u+x " + gradleWFile)

        val gradleFolder = File(root, "gradle")
        gradleFolder.mkdir()
        val gradleWrapperFolder = File(gradleFolder, "wrapper")
        gradleWrapperFolder.mkdir()

        val sourceFolder = File(ASSETS_PATH, "gradle/wrapper")

        val gradleWrapperJar = "gradle-wrapper.jar"
        val gradleWrapperProperties = "gradle-wrapper.properties"

        Files.copy(FileInputStream(File(sourceFolder, gradleWrapperJar)),
                File(gradleWrapperFolder, gradleWrapperJar).toPath(), StandardCopyOption.REPLACE_EXISTING)

        File(gradleWrapperFolder, gradleWrapperProperties).writeText(gradleWrapper(configPOJO.gradleVersion!!))
    }


    fun gradleWrapper(gradleVersion: String) = """
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-$gradleVersion-all.zip
         """
}
