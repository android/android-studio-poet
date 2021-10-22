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

class AndroidModuleConfig : ModuleConfig() {

    var androidBuildConfig: AndroidBuildConfig = AndroidBuildConfig()

    var activityCount: Int = 0
    var productFlavorConfigs: List<FlavorConfig>? = null
    var buildTypes: List<BuildTypeConfig>? = null
    var hasLaunchActivity: Boolean = false

    var resourcesConfig: ResourcesConfig? = null
    var dataBindingConfig: DataBindingConfig? = null
    var composeConfig: ComposeConfig? = null
    var viewBinding: Boolean = false
}