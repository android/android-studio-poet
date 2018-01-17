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

package com.google.androidstudiopoet.converters

import com.google.androidstudiopoet.input.ProjectConfig
import com.google.androidstudiopoet.models.ConfigPOJO

class ConfigPojoToProjectConfigConverter(private val configPojoToModuleConfigConverter: ConfigPojoToModuleConfigConverter,
                                         private val configPojoToFlavourConfigsConverter: ConfigPojoToFlavourConfigsConverter,
                                         private val configPojoToBuildTypeConfigsConverter: ConfigPojoToBuildTypeConfigsConverter,
                                         private val configPojoToAndroidModuleConfigConverter: ConfigPojoToAndroidModuleConfigConverter,
                                         private val configPojoToBuildSystemConfigConverter: ConfigPojoToBuildSystemConfigConverter) {
    fun convert(configPojo: ConfigPOJO): ProjectConfig {
        val pureModulesConfigs = (0 until configPojo.numModules)
                .map { configPojoToModuleConfigConverter.convert(configPojo, it) }

        val productFlavors = configPojoToFlavourConfigsConverter.convert(configPojo)
        val buildTypes = configPojoToBuildTypeConfigsConverter.convert(configPojo)

        val androidModulesConfigs = (0 until configPojo.androidModules)
                .map {
                    configPojoToAndroidModuleConfigConverter.convert(configPojo, it, productFlavors, buildTypes,
                            pureModulesConfigs.map { it.moduleName })
                }

        val buildSystemConfig = configPojoToBuildSystemConfigConverter.convert(configPojo)
        return ProjectConfig().apply {
            projectName = configPojo.projectName
            root = configPojo.root
            this.buildSystemConfig = buildSystemConfig
            moduleConfigs = pureModulesConfigs + androidModulesConfigs
        }
    }
}
