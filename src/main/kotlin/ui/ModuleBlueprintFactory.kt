package ui

import ui.models.AndroidModuleBlueprint
import ui.models.ConfigPOJO
import ui.models.ModuleBlueprint

class ModuleBlueprintFactory {
    fun create(index: Int, config: ConfigPOJO, projectRoot: String): ModuleBlueprint {
        val dependencies = config.dependencies
                ?.filter { it.from == index}
                ?.map { it.to } ?: listOf()
        return ModuleBlueprint(index, "module" + index, projectRoot, dependencies)
    }

    fun createAndroidModule(i: Int, configPOJO: ConfigPOJO?, projectRoot: String):
            AndroidModuleBlueprint {

         return AndroidModuleBlueprint(i,
                configPOJO!!.numActivitiesPerAndroidModule!!.toInt(),
                configPOJO!!.numActivitiesPerAndroidModule!!.toInt(),
                configPOJO!!.numActivitiesPerAndroidModule!!.toInt(), projectRoot)
    }
}
