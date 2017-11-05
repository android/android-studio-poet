package main.models

import main.Blueprint
import main.utils.joinPath

data class AndroidModuleBlueprint(val index: Int,
                                  val numOfActivities: Int,
                                  val numOfStrings: Int,
                                  val numOfImages: Int,
                                  val projectRoot: String): Blueprint {

    val name = "androidAppModule" + index
    val moduleRoot = projectRoot.joinPath(name)
    val srcPath = moduleRoot.joinPath("src")
    val mainPath = srcPath.joinPath("main")
    val resDirPath = mainPath.joinPath("res")
    val codePath = mainPath.joinPath("java")
}
