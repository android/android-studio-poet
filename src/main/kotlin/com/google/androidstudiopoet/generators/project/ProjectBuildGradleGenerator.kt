package com.google.androidstudiopoet.generators.project

import com.google.androidstudiopoet.utils.joinPath
import com.google.androidstudiopoet.writers.FileWriter

internal const val ASSETS_PATH = "src/main/assets"

class ProjectBuildGradleGenerator(val fileWriter: FileWriter) {

    fun generate(root: String, useKotlin: Boolean) {
        val gradleText = """
// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    ${if (useKotlin) "ext.kotlin_version = '1.1.60'" else ""}
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.1.0-dev'
        ${if (useKotlin) "classpath \"org.jetbrains.kotlin:kotlin-gradle-plugin:${'$'}kotlin_version\"" else ""}

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
""".trim()

        fileWriter.writeToFile(gradleText, root.joinPath("build.gradle"))
    }
}
