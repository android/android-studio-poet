package com.google.androidstudiopoet.generators

import com.google.androidstudiopoet.BuildGradle
import com.google.androidstudiopoet.models.ModuleBlueprint

class BuildGradleGenerator {
    fun create(moduleBlueprint: ModuleBlueprint): String {
        return BuildGradle.print(moduleBlueprint.dependencies
                .map { it -> "compile project(':module$it')\n" }
                .fold("", { acc, next -> acc + next }))
    }
}
