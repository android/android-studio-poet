/*
 *  Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package main.generators

import main.generators.packages.JavaGenerator
import main.generators.packages.KotlinGenerator
import main.models.ConfigPOJO
import java.io.File

class PackagesGenerator(private val javaGenerator: JavaGenerator,
                        private val kotlinGenerator: KotlinGenerator) {

    fun writePackages(config: ConfigPOJO, moduleIndex: Int, where: String, moduleRoot: File) {

        val packagesRoot = File(where)
        packagesRoot.mkdirs()

        for (packageIndex in 0 until config.javaPackageCount!!.toInt()) {
            javaGenerator.generatePackage(packageIndex, moduleIndex, config.javaClassCount!!.toInt(),
                    config.javaMethodsPerClass, packagesRoot, moduleRoot)
        }

        val kotlinMethodsPerClass = config.kotlinMethodsPerClass

        for (packageIndex in 0 until config.kotlinPackageCount!!.toInt()) {
            var kotlinPackageIndex = (packageIndex + config.javaPackageCount!!.toInt())
            kotlinGenerator.generatePackage(kotlinPackageIndex, moduleIndex,
                    config.kotlinPackageCount!!.toInt(), kotlinMethodsPerClass, packagesRoot, moduleRoot)
        }
    }
}

