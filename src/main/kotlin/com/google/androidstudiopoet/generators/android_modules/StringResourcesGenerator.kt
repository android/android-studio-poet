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

package com.google.androidstudiopoet.generators.android_modules

import com.google.androidstudiopoet.GenerationResult
import com.google.androidstudiopoet.Generator
import com.google.androidstudiopoet.models.AndroidModuleBlueprint
import com.google.androidstudiopoet.utils.fold
import com.google.androidstudiopoet.utils.joinPath
import com.google.androidstudiopoet.writers.FileWriter

class StringResourcesGenerator(private val fileWriter: FileWriter): Generator<AndroidModuleBlueprint, StringResourceGenerationResult> {

    /**
     * generates string resources by blueprint, returns list of string names to refer later.
     * Precondition: resources package is generated
     */
    override fun generate(blueprint: AndroidModuleBlueprint): StringResourceGenerationResult {
        val valuesDirPath = blueprint.resDirPath.joinPath("values")
        fileWriter.mkdir(valuesDirPath)

        val stringNames = (0..blueprint.numOfStrings).map { "${blueprint.name}string$it" }
        val stringsFileContent = getFileContent(stringNames)

        fileWriter.writeToFile(stringsFileContent, valuesDirPath.joinPath("strings.xml"))
        return StringResourceGenerationResult(stringNames)
    }

    private fun getFileContent(stringNames: List<String>): String {
        val stringsFileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><resources>" +
                stringNames.map { "<string name=\"$it\">$it</string>\n" }.fold() +
                "</resources>"
        return stringsFileContent
    }
}

data class StringResourceGenerationResult(val stringNames: List<String>): GenerationResult