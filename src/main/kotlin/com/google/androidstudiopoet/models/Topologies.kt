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

import java.util.*

/**
 * Enum with all supported topologies
 */
enum class Topologies {

    FULL {
        override fun generateDependencies(parameters: Map<String, String>, configPOJO: ConfigPOJO): List<DependencyConfig> {
            val result = mutableListOf<DependencyConfig>()
            for (from in 0 until configPOJO.numModules) {
                for (to in from + 1 until configPOJO.numModules) {
                    result.add(DependencyConfig(from, to))
                }
            }
            return result
        }
    },

    RANDOM {
        override fun generateDependencies(parameters: Map<String, String>, configPOJO: ConfigPOJO): List<DependencyConfig> {
            val seedInput = parameters["seed"]
            val seed: Long = seedInput?.toLong() ?: 0

            Random().setSeed(seed)
            val result = mutableListOf<DependencyConfig>()
            for (from in 0 until configPOJO.numModules) {
                for (to in from + 1 until configPOJO.numModules) {
                    if (Random().nextBoolean()) {
                        result.add(DependencyConfig(from, to))
                    }
                }
            }
            return result
        }
    },

    LINEAR {
        override fun generateDependencies(parameters: Map<String, String>, configPOJO: ConfigPOJO): List<DependencyConfig> = (1 until configPOJO.numModules).map { DependencyConfig(it - 1, it) }
    }
    ;

    /**
     * Function that should add dependencies to configPOJO based on the given parameters and the
     * content of configPOJO
     */
    abstract fun generateDependencies(parameters: Map<String, String>, configPOJO: ConfigPOJO): List<DependencyConfig>
}