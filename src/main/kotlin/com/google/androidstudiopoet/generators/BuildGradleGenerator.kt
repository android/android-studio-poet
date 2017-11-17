package com.google.androidstudiopoet.generators

import com.google.androidstudiopoet.BuildGradle
import com.google.androidstudiopoet.models.ModuleBlueprint
import com.google.androidstudiopoet.utils.fold

class BuildGradleGenerator {
    fun create(moduleBlueprint: ModuleBlueprint): String {
        return BuildGradle.print(moduleBlueprint.dependencies
                .map { it -> "compile project(':$it')\n" }
                .fold())
    }
}
