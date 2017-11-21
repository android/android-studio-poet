package com.google.androidstudiopoet

import com.google.androidstudiopoet.models.*

object ModuleBlueprintFactory {
    fun create(index: Int, config: ConfigPOJO, projectRoot: String): ModuleBlueprint {
        val dependencies = config.dependencies
                ?.filter { it.from == index}
                ?.map { it.to }

        val moduleNames = dependencies?.map { getModuleNameByIndex(it) } ?: listOf()
        val methodsToCallWithinModule = dependencies?.map { getMethodToCallForDependency(it, config, projectRoot) } ?: listOf()

        val combinations = moduleNames.combine(methodsToCallWithinModule)

        val moduleDependencies:MutableList<ModuleDependency> = combinations
                .map { ModuleDependency(it.first, it.second) }
                .toMutableList()

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

    /**
     * Method extension code to combine
     */

    private fun <T1, T2> Collection<T1>.combine(other: Iterable<T2>): List<Pair<T1, T2>> =
            combine(other, { thisItem: T1, otherItem: T2 -> Pair(thisItem, otherItem) })

    private fun <T1, T2, R> Collection<T1>.combine(other: Iterable<T2>, transformer: (thisItem: T1, otherItem:T2) -> R): List<R> =
            this.flatMap { thisItem -> other.map { otherItem -> transformer(thisItem, otherItem) }}

    private fun getModuleNameByIndex(index: Int) = "module$index"

    fun createAndroidModule(i: Int, configPOJO: ConfigPOJO?, projectRoot: String, dependencies: List<String>):
            AndroidModuleBlueprint {

         return AndroidModuleBlueprint(i,
                 configPOJO!!.numActivitiesPerAndroidModule!!.toInt(),
                 configPOJO.numActivitiesPerAndroidModule!!.toInt(),
                 configPOJO.numActivitiesPerAndroidModule.toInt(), projectRoot, i == 0, dependencies, configPOJO.productFlavors)
    }
}
