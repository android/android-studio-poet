package com.google.androidstudiopoet

import com.google.androidstudiopoet.models.AndroidModuleBlueprint
import com.google.androidstudiopoet.models.ConfigPOJO
import com.google.androidstudiopoet.models.ModuleBlueprint

object ModuleBlueprintFactory {
    fun create(index: Int, config: ConfigPOJO, projectRoot: String): ModuleBlueprint {
        val dependencies = config.dependencies
                ?.filter { it.from == index}
                ?.map { "module${it.to}" } ?: listOf()

        return ModuleBlueprint(index, "module" + index, projectRoot, dependencies, listOf())
    }

    fun createAndroidModule(i: Int, configPOJO: ConfigPOJO?, projectRoot: String, dependencies: List<String>):
            AndroidModuleBlueprint {

         return AndroidModuleBlueprint(i,
                 configPOJO!!.numActivitiesPerAndroidModule!!.toInt(),
                 configPOJO.numActivitiesPerAndroidModule!!.toInt(),
                 configPOJO.numActivitiesPerAndroidModule.toInt(), projectRoot, i == 0, dependencies)
    }
}
