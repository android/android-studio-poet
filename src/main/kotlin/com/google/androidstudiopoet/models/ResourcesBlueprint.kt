/*
Copyright 2017 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.Blueprint
import com.google.androidstudiopoet.utils.joinPath

data class ResourcesBlueprint(private val moduleName: String,
                              val resDirPath: String,
                              private val stringCount: Int,
                              private val imageCount: Int,
                              private val layoutCount: Int,
                              private val resourcesToReferWithin: ResourcesToRefer) : Blueprint {
    val stringNames = (0..stringCount).map { "${moduleName}string$it" }
    val imageNames = (0 until imageCount).map { "${moduleName.toLowerCase()}image$it" }

    val layoutNames = (0 until layoutCount).map { "${moduleName.toLowerCase()}activity_main$it" }

    val layoutsDir = resDirPath.joinPath("layout")

    private val stringsPerLayout = (stringNames + resourcesToReferWithin.strings).splitPerLayout(layoutCount)
    private val imagesPerLayout = (imageNames + resourcesToReferWithin.images).splitPerLayout(layoutCount)

    val layoutBlueprints = layoutNames.mapIndexed { index, layoutName ->
        LayoutBlueprint(layoutsDir.joinPath(layoutName) + ".xml",
                stringsPerLayout.getOrElse(index, { listOf() }),
                imagesPerLayout.getOrElse(index, { listOf() }),
                if (index == 0) resourcesToReferWithin.layouts else listOf()) }

    var resourcesToReferFromOutside = ResourcesToRefer(listOf(stringNames.last()), listOf(imageNames.last()), listOf(layoutNames.last()))
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