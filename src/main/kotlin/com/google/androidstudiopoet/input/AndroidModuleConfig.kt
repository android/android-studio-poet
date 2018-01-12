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

package com.google.androidstudiopoet.input

class AndroidModuleConfig(moduleName: String, val activityCount: Int,
                          val productFlavorConfigs: List<FlavorConfig>, val buildTypes: List<BuildTypeConfig>,
                          extraAndroidBuildFileLines: List<String>?, javaPackageCount: Int, javaClassCount: Int,
                          javaMethodsPerClass: Int, kotlinPackageCount: Int, kotlinClassCount: Int,
                          kotlinMethodsPerClass: Int, useKotlin: Boolean, generateTests: Boolean, val hasLaunchActivity: Boolean)
    : ModuleConfig(moduleName, javaPackageCount, javaClassCount, javaMethodsPerClass, kotlinPackageCount,
        kotlinClassCount, kotlinMethodsPerClass, useKotlin, extraAndroidBuildFileLines, listOf(), generateTests) {

    val resourcesConfig = ResourcesConfig(activityCount + 2,
            activityCount + 5, activityCount)
}