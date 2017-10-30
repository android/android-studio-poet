package ui

import ui.models.ConfigPOJO
import ui.models.ModuleBlueprint

class ModuleBlueprintFactory() {
    fun create(index: Int, config: ConfigPOJO): ModuleBlueprint {
        val dependencies = config.dependencies
                ?.filter { it.from == index}
                ?.map { it.to } ?: listOf()
        return ModuleBlueprint(index, dependencies)
    }
}
