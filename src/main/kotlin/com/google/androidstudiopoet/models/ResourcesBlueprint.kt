package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.Blueprint
import com.google.androidstudiopoet.utils.joinPath

data class ResourcesBlueprint(private val moduleName: String,
                              val resDirPath: String,
                              private val numOfStrings: Int,
                              val numOfImages: Int,
                              private val numOfLayouts: Int) : Blueprint {
    val stringNames = (0..numOfStrings).map { "${moduleName}string$it" }
    val imageNames = (0 until numOfImages).map { "image$it" }

    //TODO layouts shouldn't depend on numOfImages
    val layoutNames = (0 until numOfLayouts).map { "activity_main$it" }

    val layoutsDir = resDirPath.joinPath("layout")

    private val stringsPerLayout = stringNames.splitPerLayout(numOfLayouts)
    private val imagesPerLayout = imageNames.splitPerLayout(numOfLayouts)

    val layoutBlueprints = layoutNames.mapIndexed { index, layoutName ->
        LayoutBlueprint(layoutsDir.joinPath(layoutName) + ".xml",
                stringsPerLayout.getOrElse(index, { listOf() }),
                imagesPerLayout.getOrElse(index, { listOf() }),
                listOf()) }

}

fun List<String>.splitPerLayout(limit: Int) =
        this.foldIndexed(mutableListOf<List<String>>()) { index, acc, stringName ->
            if (index < limit) {
                acc.add(listOf(stringName))
            } else {
                acc.add(limit - 1, acc[limit - 1] + stringName)
            }
            return@foldIndexed acc
        }