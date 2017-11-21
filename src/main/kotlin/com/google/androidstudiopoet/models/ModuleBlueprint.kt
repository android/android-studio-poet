package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.utils.joinPath

data class ModuleBlueprint(val index: Int,
                           val name: String,
                           val root: String,
                           val dependencies: List<ModuleDependency>,
                           private val config: ConfigPOJO) {
    
    private val javaPackageCount = config.javaPackageCount!!.toInt()
    private val javaClassCount = config.javaClassCount!!.toInt()
    private val javaMethodsPerClass = config.javaMethodsPerClass

    private val kotlinPackageCount = config.kotlinPackageCount!!.toInt()
    private val kotlinClassCount = config.kotlinClassCount!!.toInt()
    private val kotlinMethodsPerClass = config.kotlinMethodsPerClass

    val moduleRoot = root.joinPath(name)
    val packagesBlueprint = PackagesBlueprint(javaPackageCount, javaClassCount, javaMethodsPerClass, kotlinPackageCount,
            kotlinClassCount, kotlinMethodsPerClass, moduleRoot + "/src/main/java/", name, dependencies)

    var methodToCallFromOutside = packagesBlueprint.methodToCallFromOutside
}