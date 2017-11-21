package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.ModuleBlueprintFactory
import com.google.androidstudiopoet.utils.joinPath

class ProjectBlueprint(val configPOJO: ConfigPOJO) {
    val projectRoot = configPOJO.root.joinPath(configPOJO.projectName)

    val moduleBlueprints = (0 until configPOJO.numModules).map { i ->
        ModuleBlueprintFactory.create(i, configPOJO, projectRoot)
    }

    val androidModuleBlueprints = (0 until configPOJO.androidModules!!.toInt()).map { i ->
        ModuleBlueprintFactory.createAndroidModule(i, configPOJO, projectRoot, moduleBlueprints.map { it.index },
                (i until configPOJO.androidModules.toInt()).toList())
    }

    val allModulesNames = moduleBlueprints.map { it.name } + androidModuleBlueprints.map { it.name }
 }
