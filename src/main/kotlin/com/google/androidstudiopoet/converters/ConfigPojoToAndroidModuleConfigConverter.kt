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

import com.google.androidstudiopoet.input.AndroidModuleConfig
import com.google.androidstudiopoet.input.BuildTypeConfig
import com.google.androidstudiopoet.input.FlavorConfig
import com.google.androidstudiopoet.input.ModuleConfig
import com.google.androidstudiopoet.models.ConfigPOJO

class ConfigPojoToAndroidModuleConfigConverter {
    fun convert(config: ConfigPOJO, index: Int, productFlavorConfigs: List<FlavorConfig>,
                buildTypes: List<BuildTypeConfig>): AndroidModuleConfig {

        val javaPackageCount = config.javaPackageCount!!.toInt()
        val javaClassCount = config.javaClassCount!!.toInt()
        val javaMethodsPerClass = config.javaMethodsPerClass

        val kotlinPackageCount = config.kotlinPackageCount!!.toInt()
        val kotlinClassCount = config.kotlinClassCount!!.toInt()
        val kotlinMethodsPerClass = config.kotlinMethodsPerClass
        val useKotlin: Boolean = config.useKotlin

        val activityCount = config.numActivitiesPerAndroidModule!!.toInt()

        return AndroidModuleConfig(index, activityCount, productFlavorConfigs, buildTypes,
                config.extraAndroidBuildFileLines, javaPackageCount, javaClassCount, javaMethodsPerClass, kotlinPackageCount,
                kotlinClassCount, kotlinMethodsPerClass, useKotlin)
    }
}