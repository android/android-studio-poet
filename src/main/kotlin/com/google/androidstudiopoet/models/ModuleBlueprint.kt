package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.utils.joinPath

data class ModuleBlueprint(val index: Int,
                           val name: String,
                           val root: String,
                           val dependencies: List<ModuleDependency>,
                           private val javaPackageCount: Int, private val javaClassCount: Int, private val javaMethodsPerClass: Int,
                           private val kotlinPackageCount: Int, private val kotlinClassCount: Int, private val kotlinMethodsPerClass: Int) {


    val moduleRoot = root.joinPath(name)
    val packagesBlueprint = PackagesBlueprint(javaPackageCount, javaClassCount, javaMethodsPerClass, kotlinPackageCount,
            kotlinClassCount, kotlinMethodsPerClass, moduleRoot + "/src/main/java/", name, convertDependenciesToMethodsToCall(dependencies))

    var methodToCallFromOutside = packagesBlueprint.methodToCallFromOutside

    private fun convertDependenciesToMethodsToCall(dependencies: List<ModuleDependency>): List<MethodToCall> = dependencies.map { it.methodToCall }
}