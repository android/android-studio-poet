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

import com.google.androidstudiopoet.DEFAULT_DEPENDENCY_METHOD
import com.google.androidstudiopoet.input.ConfigPOJO
import java.security.InvalidParameterException
import java.util.*

/**
 * Enum with all supported topologies
 */
enum class Topologies {
    FULL {
        override fun generateDependencies(parameters: Map<String, String>, configPOJO: ConfigPOJO): List<FromToDependencyConfig> {
            val method = getMethod(parameters)
            val random = randomWithSeed(parameters)
            val density = getDensity(parameters)
            val result = mutableListOf<FromToDependencyConfig>()
            val allNames = generateModuleNames(configPOJO)
            for (from in 0 until allNames.size) {
                val fromName = allNames[from]
                for (to in from + 1 until allNames.size) {
                    if (random.nextFloat() < density) {
                        result.add(FromToDependencyConfig(fromName, allNames[to], method))
                    }
                }
            }
            return result
        }
    },

    CONNECTED {
        override fun generateDependencies(parameters: Map<String, String>, configPOJO: ConfigPOJO): List<FromToDependencyConfig> {
            val method = getMethod(parameters)
            val random = randomWithSeed(parameters)
            val density = getDensity(parameters)
            val result = mutableListOf<FromToDependencyConfig>()
            val allNames = generateModuleNames(configPOJO)
            var to = 1
            while (to < allNames.size) {
                var numFrom = 0
                val toName = allNames[to]
                for (from in 0 until to) {
                    if (random.nextFloat() < density) {
                        result.add(FromToDependencyConfig(allNames[from], toName, method))
                        numFrom++
                    }
                }
                if (numFrom > 0) {
                    to++
                }
            }
            return result
        }
    },

    LINEAR {
        override fun generateDependencies(parameters: Map<String, String>, configPOJO: ConfigPOJO): List<FromToDependencyConfig> {
            val method = getMethod(parameters)
            val allNames = generateModuleNames(configPOJO)
            val random = randomWithSeed(parameters)
            val density = getDensity(parameters)
            val result = mutableListOf<FromToDependencyConfig>()
            for (id in 1 until allNames.size) {
                if (random.nextFloat() < density) {
                    result.add(FromToDependencyConfig(allNames[id - 1], allNames[id], method))
                }
            }
            return result
        }
    },

    STAR {
        override fun generateDependencies(parameters: Map<String, String>, configPOJO: ConfigPOJO): List<FromToDependencyConfig> {
            val method = getMethod(parameters)
            val allNames = generateModuleNames(configPOJO)
            val random = randomWithSeed(parameters)
            val density = getDensity(parameters)
            val result = mutableListOf<FromToDependencyConfig>()
            for (id in 1 until allNames.size) {
                if (random.nextFloat() < density) {
                    result.add(FromToDependencyConfig(allNames[0], allNames[id], method))
                }
            }
            return result
        }
    },

    TREE {
        private fun getParent(node: Int, degree: Int) = (node - 1) / degree

        override fun generateDependencies(parameters: Map<String, String>, configPOJO: ConfigPOJO): List<FromToDependencyConfig> {
            val method = getMethod(parameters)
            val allNames = generateModuleNames(configPOJO)
            val degree: Int = parameters["degree"]?.toInt() ?: throw InvalidParameterException("No degree was specified: $parameters")
            if (degree < 1) {
                throw InvalidParameterException("Degree must be a positive integer: $parameters")
            }
            val random = randomWithSeed(parameters)
            val density = getDensity(parameters)
            val result = mutableListOf<FromToDependencyConfig>()
            for (id in 1 until allNames.size) {
                if (random.nextFloat() < density) {
                    result.add(FromToDependencyConfig(allNames[getParent(id, degree)], allNames[id], method))
                }
            }
            return result
        }
    },

    VARIABLE_TREE {
        override fun generateDependencies(parameters: Map<String, String>, configPOJO: ConfigPOJO): List<FromToDependencyConfig> {
            val method = getMethod(parameters)
            val allNames = generateModuleNames(configPOJO)
            val random = randomWithSeed(parameters)
            val wideness = getWideness(parameters)
            if (wideness < 0.0f || wideness > 1.0f) {
                throw InvalidParameterException("Wideness should be in [0.0, 1.0]: $parameters")
            }
            val density = getDensity(parameters)
            val result = mutableListOf<FromToDependencyConfig>()
            var currentParent = 0
            for (to in 1 until allNames.size) {
                if (random.nextFloat() < density) {
                    result.add(FromToDependencyConfig(allNames[currentParent], allNames[to], method))
                }
                if (random.nextFloat() >= wideness) {
                    currentParent++
                }
            }
            return result
        }
    },

    RECTANGLE {
        override fun generateDependencies(parameters: Map<String, String>, configPOJO: ConfigPOJO): List<FromToDependencyConfig> {
            val method = getMethod(parameters)
            val width: Int = parameters["width"]?.toInt() ?: throw InvalidParameterException("width was not specified: $parameters")
            if (width <= 0) {
                throw InvalidParameterException("width must be greater than 0: $parameters")
            }
            val allNames = generateModuleNames(configPOJO)
            val random = randomWithSeed(parameters)
            val density = getDensity(parameters)

            val result = mutableListOf<FromToDependencyConfig>()
            for (to in width until allNames.size) {
                val base = ((to / width) - 1) * width
                for (from in 0 until width) {
                    if (random.nextFloat() < density) {
                        result.add(FromToDependencyConfig(allNames[base + from], allNames[to], method))
                    }
                }
            }
            return result
        }
    },

    CONNECTED_RECTANGLE {
        override fun generateDependencies(parameters: Map<String, String>, configPOJO: ConfigPOJO): List<FromToDependencyConfig> {
            val method = getMethod(parameters)
            val width: Int = parameters["width"]?.toInt() ?: throw InvalidParameterException("width was not specified: $parameters")
            if (width <= 0) {
                throw InvalidParameterException("width must be greater than 0: $parameters")
            }
            val allNames = generateModuleNames(configPOJO)
            val random = randomWithSeed(parameters)
            val density = getDensity(parameters)

            val result = mutableListOf<FromToDependencyConfig>()
            var to = width
            while (to < allNames.size) {
                val base = ((to / width) - 1) * width
                var numFrom = 0
                for (from in 0 until width) {
                    if (random.nextFloat() < density) {
                        result.add(FromToDependencyConfig(allNames[base + from], allNames[to], method))
                        numFrom++
                    }
                }
                if (numFrom > 0) {
                    to++
                }
            }
            return result
        }
    };

    protected fun randomWithSeed(parameters: Map<String, String>) : Random {
        val seedInput = parameters["seed"]
        val seed : Long = seedInput?.toLong() ?: 0
        return Random(seed)
    }

    protected fun generateModuleNames( configPOJO: ConfigPOJO): List<String> {
        val result = mutableListOf<String>()
        (0 until configPOJO.androidModules).mapTo(result) {getAndroidModuleNameByIndex(it)}
        (0 until configPOJO.numModules).mapTo(result) {getModuleNameByIndex(it)}
        return result
    }

    protected fun getAndroidModuleNameByIndex(index: Int) = "androidAppModule$index"

    protected fun getModuleNameByIndex(index: Int) = "module$index"

    protected fun getDensity(parameters: Map<String, String>, default: Float = 1.0f) = parameters["density"]?.toFloat() ?: default

    protected fun getWideness(parameters: Map<String, String>, default: Float = 0.5f) = parameters["wideness"]?.toFloat() ?: default

    protected fun getMethod(parameters: Map<String, String>) = parameters.getOrDefault("method", DEFAULT_DEPENDENCY_METHOD)

    /**
     * Function that should add dependencies to configPOJO based on the given parameters and the
     * content of configPOJO
     */
    abstract fun generateDependencies(parameters: Map<String, String>, configPOJO: ConfigPOJO): List<FromToDependencyConfig>
}
