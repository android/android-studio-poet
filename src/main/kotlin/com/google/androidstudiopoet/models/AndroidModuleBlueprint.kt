package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.Blueprint
import com.google.androidstudiopoet.utils.joinPath
import com.google.androidstudiopoet.utils.joinPaths

data class AndroidModuleBlueprint(val index: Int,
                                  val numOfActivities: Int,
                                  val numOfStrings: Int,
                                  val numOfImages: Int,
                                  val projectRoot: String,
                                  val hasLaunchActivity: Boolean,
                                  val useKotlin: Boolean,
                                  val dependencies: List<String>,
                                  val productFlavors: List<Int>?): Blueprint {
    val name = "androidAppModule" + index
    val packageName = "com.$name"
    val moduleRoot = projectRoot.joinPath(name)
    val srcPath = moduleRoot.joinPath("src")
    val mainPath = srcPath.joinPath("main")
    val resDirPath = mainPath.joinPath("res")
    val codePath = mainPath.joinPath("java")
    val packagePath = codePath.joinPaths(packageName.split("."))
}
