package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.Blueprint
import com.google.androidstudiopoet.utils.joinPath
import com.google.androidstudiopoet.utils.joinPaths

data class AndroidModuleBlueprint(val index: Int,
                                  val numOfActivities: Int,
                                  val numOfStrings: Int,
                                  val numOfImages: Int,
                                  private val projectRoot: String,
                                  val hasLaunchActivity: Boolean,
                                  val dependencies: List<ModuleDependency>,
                                  val productFlavors: List<Int>?,
                                  private val javaPackageCount: Int, private val javaClassCount: Int, private val javaMethodsPerClass: Int,
                                  private val kotlinPackageCount: Int, private val kotlinClassCount: Int, private val kotlinMethodsPerClass: Int): Blueprint {
    val name = "androidAppModule" + index
    val packageName = "com.$name"
    val moduleRoot = projectRoot.joinPath(name)
    val srcPath = moduleRoot.joinPath("src")
    val mainPath = srcPath.joinPath("main")
    val resDirPath = mainPath.joinPath("res")
    val codePath = mainPath.joinPath("java")
    val packagePath = codePath.joinPaths(packageName.split("."))


    val packagesBlueprint = PackagesBlueprint(javaPackageCount, javaClassCount, javaMethodsPerClass, kotlinPackageCount,
            kotlinClassCount, kotlinMethodsPerClass, moduleRoot + "/src/main/java/", name, listOf())

    val stringNames = (0..numOfStrings).map { "${name}string$it" }
    val imageNames = (0 until numOfImages).map { "image$it" }

    //TODO layouts shouldn't depend on numOfImages
    val layoutNames = (0 until numOfImages).map { "activity_main$it" }

    val activityNames = 0.until(numOfActivities).map { "Activity$it" }

}
