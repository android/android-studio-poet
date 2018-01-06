package com.google.androidstudiopoet.converters

import com.google.androidstudiopoet.input.ProjectConfig
import com.google.androidstudiopoet.models.ConfigPOJO

class ConfigPojoToProjectConfigConverter(private val configPojoToModuleConfigConverter: ConfigPojoToModuleConfigConverter,
                                         private val configPojoToFlavourConfigsConverter: ConfigPojoToFlavourConfigsConverter,
                                         private val configPojoToBuildTypeConfigsConverter: ConfigPojoToBuildTypeConfigsConverter,
                                         private val configPojoToAndroidModuleConfigConverter: ConfigPojoToAndroidModuleConfigConverter) {
    fun convert(configPojo: ConfigPOJO): ProjectConfig {
        val pureModulesConfigs = (0 until configPojo.numModules)
                .map { configPojoToModuleConfigConverter.convert(configPojo, it) }

        val productFlavors = configPojoToFlavourConfigsConverter.convert(configPojo)
        val buildTypes = configPojoToBuildTypeConfigsConverter.convert(configPojo)

        val androidModulesConfigs = (0 until configPojo.androidModules!!.toInt())
                .map { configPojoToAndroidModuleConfigConverter.convert(configPojo, it, productFlavors, buildTypes) }
        return ProjectConfig(configPojo.projectName, configPojo.root, pureModulesConfigs, androidModulesConfigs)
    }
}
