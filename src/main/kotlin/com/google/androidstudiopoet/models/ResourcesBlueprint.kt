package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.Blueprint

data class ResourcesBlueprint(private val moduleName: String,
                              val resDirPath: String,
                              private val numOfActivities: Int,
                              private val numOfStrings: Int,
                              val numOfImages: Int): Blueprint {
    val stringNames = (0..numOfStrings).map { "${moduleName}string$it" }
    val imageNames = (0 until numOfImages).map { "image$it" }

    //TODO layouts shouldn't depend on numOfImages
    val layoutNames = (0 until numOfImages).map { "activity_main$it" }

    val activityNames = 0.until(numOfActivities).map { "Activity$it" }
}