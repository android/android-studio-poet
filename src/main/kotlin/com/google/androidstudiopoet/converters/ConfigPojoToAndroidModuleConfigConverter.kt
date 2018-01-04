package com.google.androidstudiopoet.converters

import com.google.androidstudiopoet.input.AndroidModuleConfig
import com.google.androidstudiopoet.input.BuildTypeConfig
import com.google.androidstudiopoet.input.FlavorConfig
import com.google.androidstudiopoet.input.ModuleConfig
import com.google.androidstudiopoet.models.ConfigPOJO

class ConfigPojoToAndroidModuleConfigConverter {
    fun convert(config: ConfigPOJO, index: Int, productFlavorConfigs: List<FlavorConfig>,
                buildTypes: List<BuildTypeConfig>): AndroidModuleConfig {

        val activityCount = config.numActivitiesPerAndroidModule!!.toInt()

        return AndroidModuleConfig(index, config, activityCount, productFlavorConfigs, buildTypes,
                config.extraAndroidBuildFileLines)
    }
}