package ui.models

import utils.joinPath

data class AndroidModuleBlueprint(val index: Int,
                                  val numOfActivities: Int,
                                  val numOfStrings: Int,
                                  val numOfImages: Int,
                                  val projectRoot: String) {

    val name = "androidAppModule" + index
    val moduleRoot = projectRoot.joinPath(name)
}
