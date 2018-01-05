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

package com.google.androidstudiopoet.generators.packages

import com.google.androidstudiopoet.models.ClassBlueprint
import com.google.androidstudiopoet.models.MethodToCall
import com.google.androidstudiopoet.models.PackageBlueprint
import com.google.androidstudiopoet.writers.FileWriter
import java.io.File

abstract class PackageGenerator(private val fileWriter: FileWriter) {

    fun generatePackage(blueprint: PackageBlueprint): MethodToCall? {

        val srcFolder = File(blueprint.srcFolder, blueprint.packageName)
        if (srcFolder.exists()) {
            srcFolder.delete()
        }

        srcFolder.mkdirs()

        if (blueprint.generateTests) {
            val testFolder = File(blueprint.testFolder, blueprint.packageName)
            if (testFolder.exists()) {
                testFolder.delete()
            }
            testFolder.mkdirs()
        }

        blueprint.classBlueprints.forEach({ generateClass(it) })

        return blueprint.methodToCallFromOutside
    }

    abstract fun generateClass(blueprint: ClassBlueprint): MethodToCall?

    protected fun writeFile(path: String, content: String) {
        fileWriter.writeToFile(content, path)
    }
}
