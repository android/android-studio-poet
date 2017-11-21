package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.utils.joinPath

data class ModuleBlueprint(val index: Int, val name: String, val root: String, val dependencies: List<String>,
                           val methodsToCall: List<MethodToCall>, private val config: ConfigPOJO) {

   /*
   class ModuleBlueprint accept, "val dependencies: List<String>" and
"val methodsToCall: List<MethodToCall>", size of those two lists should be equal, because first list is a name of
module and the second is a method to call from it.
So if we merge those lists into a List<Dependency> where dependency contains a name and a method call list from
that particular module, then we'll have a place to put more stuff for android modules, while keeping amount of arguments low
Android module still accept only "val dependencies: List<String>" that should also be changed in the future
    */



    private val javaPackageCount = config.javaPackageCount!!.toInt()
    private val javaClassCount = config.javaClassCount!!.toInt()
    private val javaMethodsPerClass = config.javaMethodsPerClass

    private val kotlinPackageCount = config.kotlinPackageCount!!.toInt()
    private val kotlinClassCount = config.kotlinClassCount!!.toInt()
    private val kotlinMethodsPerClass = config.kotlinMethodsPerClass

    val moduleRoot = root.joinPath(name)
    val packagesBlueprint = PackagesBlueprint(javaPackageCount, javaClassCount, javaMethodsPerClass, kotlinPackageCount,
            kotlinClassCount, kotlinMethodsPerClass,moduleRoot + "/src/main/java/", name, methodsToCall)

    var methodToCallFromOutside = packagesBlueprint.methodToCallFromOutside
}