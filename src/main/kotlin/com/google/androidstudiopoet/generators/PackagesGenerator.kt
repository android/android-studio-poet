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

package com.google.androidstudiopoet.generators

import com.google.androidstudiopoet.generators.packages.JavaGenerator
import com.google.androidstudiopoet.generators.packages.KotlinGenerator
import com.google.androidstudiopoet.models.PackagesBlueprint
import java.io.File

class PackagesGenerator(private val javaGenerator: JavaGenerator,
                        private val kotlinGenerator: KotlinGenerator) {

    fun writePackages(blueprint: PackagesBlueprint) {
        val packagesRoot = File(blueprint.where)
        packagesRoot.mkdirs()
        blueprint.javaPackageBlueprints.forEach({javaGenerator.generatePackage(it) })
        blueprint.kotlinPackageBlueprints.forEach({ kotlinGenerator.generatePackage(it) })
    }
}

