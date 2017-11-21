package com.google.androidstudiopoet

import com.google.androidstudiopoet.models.*

object ModuleBlueprintFactory {
    fun create(index: Int, config: ConfigPOJO, projectRoot: String): ModuleBlueprint {
        val dependencies = config.dependencies
                ?.filter { it.from == index}
                ?.map { it.to }

        val moduleDependencies =
                dependencies?.map { ModuleDependency(getModuleNameByIndex(it),
                        getMethodToCallForDependency(it, config, projectRoot)) } ?: listOf()

        return ModuleBlueprint(index, getModuleNameByIndex(index), projectRoot, moduleDependencies, config)
    }

    private fun getMethodToCallForDependency(index: Int, config: ConfigPOJO, projectRoot: String): MethodToCall {
        /*
            Because method to call from outside doesn't depend on the dependencies, we can create ModuleBlueprint for
            dependency, return methodToCallFromOutside and forget about this module blueprint.
            WARNING: creation of ModuleBlueprint could be expensive
         */
        return ModuleBlueprint(index, getModuleNameByIndex(index), projectRoot, listOf(), config).methodToCallFromOutside
    }


    private fun getModuleNameByIndex(index: Int) = "module$index"

    fun createAndroidModule(i: Int, configPOJO: ConfigPOJO?, projectRoot: String, dependencies: List<String>):
            AndroidModuleBlueprint {

         return AndroidModuleBlueprint(i,
                 configPOJO!!.numActivitiesPerAndroidModule!!.toInt(),
                 configPOJO.numActivitiesPerAndroidModule!!.toInt(),
                 configPOJO.numActivitiesPerAndroidModule.toInt(), projectRoot, i == 0, dependencies, configPOJO.productFlavors)
    }
}
