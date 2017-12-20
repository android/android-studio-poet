package com.google.androidstudiopoet.converters

import com.google.androidstudiopoet.input.BuildTypeConfig
import com.google.androidstudiopoet.models.ConfigPOJO

class ConfigPojoToBuildTypeConfigsConverter {
    fun convert(configPOJO: ConfigPOJO): List<BuildTypeConfig> {
        val buildTypes = configPOJO.buildTypes
        if (buildTypes == null || buildTypes <= 0) {
            return listOf()
        }

        return (0 until buildTypes).map { index -> BuildTypeConfig("buildType$index") }
    }
}
