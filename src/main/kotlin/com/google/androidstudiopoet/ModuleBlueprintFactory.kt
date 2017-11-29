package com.google.androidstudiopoet

import com.google.androidstudiopoet.models.*

object ModuleBlueprintFactory {
    fun create(index: Int, config: ConfigPOJO, projectRoot: String): ModuleBlueprint {

        val javaPackageCount = config.javaPackageCount!!.toInt()
        val javaClassCount = config.javaClassCount!!.toInt()
        val javaMethodsPerClass = config.javaMethodsPerClass

        val kotlinPackageCount = config.kotlinPackageCount!!.toInt()
        val kotlinClassCount = config.kotlinClassCount!!.toInt()
        val kotlinMethodsPerClass = config.kotlinMethodsPerClass

        val moduleDependencies = config.dependencies
                ?.filter { it.from == index }
                ?.map { it.to }
                ?.map {
                    getModuleDependency(it, projectRoot, javaPackageCount, javaClassCount,
                            javaMethodsPerClass, kotlinPackageCount, kotlinClassCount, kotlinMethodsPerClass)
                } ?: listOf()

        return ModuleBlueprint(index, getModuleNameByIndex(index), projectRoot, moduleDependencies, javaPackageCount,
                javaClassCount, javaMethodsPerClass, kotlinPackageCount, kotlinClassCount, kotlinMethodsPerClass)
    }

    private fun getModuleDependency(index: Int, projectRoot: String, javaPackageCount: Int, javaClassCount: Int, javaMethodsPerClass: Int,
                                    kotlinPackageCount: Int, kotlinClassCount: Int, kotlinMethodsPerClass: Int): ModuleDependency {
        /*
            Because method to call from outside doesn't depend on the dependencies, we can create ModuleBlueprint for
            dependency, return methodToCallFromOutside and forget about this module blueprint.
            WARNING: creation of ModuleBlueprint could be expensive
         */
        val moduleBlueprint = ModuleBlueprint(index, getModuleNameByIndex(index), projectRoot, listOf(), javaPackageCount, javaClassCount, javaMethodsPerClass, kotlinPackageCount,
                kotlinClassCount, kotlinMethodsPerClass)

        return ModuleDependency(moduleBlueprint.name, moduleBlueprint.methodToCallFromOutside)
    }

    private fun getAndroidModuleDependency(index: Int, config: ConfigPOJO, projectRoot: String): AndroidModuleDependency {
        /*
            Because method to call from outside and resources to refer don't depend on the dependencies, we can create AndroidModuleBlueprint for
            dependency, return methodToCallFromOutside with resourcesToRefer and forget about this module blueprint.
            WARNING: creation of AndroidModuleBlueprint could be expensive
         */

        val moduleBlueprint = createAndroidModule(index, config, projectRoot, listOf(), listOf())
        return AndroidModuleDependency(moduleBlueprint.name, moduleBlueprint.methodToCallFromOutside, moduleBlueprint.resourcesToReferFromOutside)
    }

    private fun getModuleNameByIndex(index: Int) = "module$index"

    fun createAndroidModule(i: Int, config: ConfigPOJO, projectRoot: String, codeModuleDependencyIndexes: List<Int>,
                            androidModuleDependencyIndexes: List<Int>):
            AndroidModuleBlueprint {

        val javaPackageCount = config.javaPackageCount!!.toInt()
        val javaClassCount = config.javaClassCount!!.toInt()
        val javaMethodsPerClass = config.javaMethodsPerClass

        val kotlinPackageCount = config.kotlinPackageCount!!.toInt()
        val kotlinClassCount = config.kotlinClassCount!!.toInt()
        val kotlinMethodsPerClass = config.kotlinMethodsPerClass

        val moduleDependencies = codeModuleDependencyIndexes
                .map {
                    getModuleDependency(it, projectRoot, javaPackageCount, javaClassCount,
                            javaMethodsPerClass, kotlinPackageCount, kotlinClassCount, kotlinMethodsPerClass)
                } + androidModuleDependencyIndexes.map { getAndroidModuleDependency(it, config, projectRoot) }

        return AndroidModuleBlueprint(i,
                config.numActivitiesPerAndroidModule!!.toInt(),
                config.numActivitiesPerAndroidModule.toInt() + 2,
                config.numActivitiesPerAndroidModule.toInt() + 5,
                config.numActivitiesPerAndroidModule.toInt(),
                projectRoot, i == 0, moduleDependencies, config.productFlavors,
                javaPackageCount, javaClassCount, javaMethodsPerClass, kotlinPackageCount, kotlinClassCount, kotlinMethodsPerClass)
    }
}
