package com.google.androidstudiopoet.converters

import com.google.androidstudiopoet.input.ProjectConfig
import com.google.androidstudiopoet.models.ConfigPOJO

class ConfigPojoToProjectConfigConverter(private val configPojoToModuleConfigConverter: ConfigPojoToModuleConfigConverter) {
    fun convert(configPojo: ConfigPOJO): ProjectConfig {
        val pureModulesConfigs = (0 until configPojo.numModules)
                .map { configPojoToModuleConfigConverter.convert(configPojo, it) }
        return ProjectConfig(configPojo.projectName, configPojo.root, pureModulesConfigs)
    }
}
