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

import com.google.androidstudiopoet.input.CodeConfig
import com.google.androidstudiopoet.input.PluginConfig

class ModuleBlueprint(name: String,
                      root: String,
                      useKotlin: Boolean,
                      dependencies: Set<Dependency>,
                      javaConfig: CodeConfig?,
                      kotlinConfig: CodeConfig?,
                      extraLines: List<String>?,
                      generateTests: Boolean,
                      pluginConfigs: List<PluginConfig>?,
                      generateBazelFiles: Boolean?)
    : AbstractModuleBlueprint(name, root, useKotlin, dependencies, javaConfig, kotlinConfig, extraLines,
        generateTests) {

    val buildGradleBlueprint by lazy {
        ModuleBuildGradleBlueprint(dependencies.toSet(), useKotlin, generateTests, extraLines, moduleRoot,
                pluginConfigs)
    }

    val buildBazelBlueprint by lazy {
        when (generateBazelFiles) {
            true -> ModuleBuildBazelBlueprint(dependencies.toSet(), moduleRoot, name)
            else -> null
        }
    }
}