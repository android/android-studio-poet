package ui

import ui.models.ModuleBlueprint

class BuildGradleCreator {
    fun create(moduleBlueprint: ModuleBlueprint): String {
        return BuildGradle.print(moduleBlueprint.dependencies
                .map { it -> "compile project(':module$it')\n" }
                .fold("", {acc, next -> acc + next }))
    }
}
