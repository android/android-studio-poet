package main.generators

import main.BuildGradle
import main.models.ModuleBlueprint

class BuildGradleGenerator {
    fun create(moduleBlueprint: ModuleBlueprint): String {
        return BuildGradle.print(moduleBlueprint.dependencies
                .map { it -> "compile project(':module$it')\n" }
                .fold("", { acc, next -> acc + next }))
    }
}
