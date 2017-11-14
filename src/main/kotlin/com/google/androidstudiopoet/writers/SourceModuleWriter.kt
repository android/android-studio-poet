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

package com.google.androidstudiopoet.writers

import com.google.gson.Gson
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import com.google.androidstudiopoet.DependencyValidator
import com.google.androidstudiopoet.ModuleBlueprintFactory
import com.google.androidstudiopoet.generators.BuildGradleGenerator
import com.google.androidstudiopoet.generators.PackagesGenerator
import com.google.androidstudiopoet.generators.project.GradleSettingsGenerator
import com.google.androidstudiopoet.generators.project.GradlewGenerator
import com.google.androidstudiopoet.generators.project.ProjectBuildGradleGenerator
import com.google.androidstudiopoet.models.ConfigPOJO
import com.google.androidstudiopoet.models.ModuleBlueprint
import com.google.androidstudiopoet.models.PackagesBlueprint
import com.google.androidstudiopoet.utils.joinPath
import java.io.File


class SourceModuleWriter(private val dependencyValidator: DependencyValidator,
                         private val blueprintFactory: ModuleBlueprintFactory,
                         private val buildGradleGenerator: BuildGradleGenerator,
                         private val gradleSettingsGenerator: GradleSettingsGenerator,
                         private val projectBuildGradleGenerator: ProjectBuildGradleGenerator,
                         private val androidModuleGenerator: AndroidModuleWriter,
                         private val packagesGenerator: PackagesGenerator,
                         private val fileWriter: FileWriter) {

    fun generate(configStr: String) = runBlocking {

        val gson = Gson()
        val configPOJO = gson.fromJson(configStr, ConfigPOJO::class.java)

        if (!dependencyValidator.isValid(configPOJO)) {
            throw IllegalStateException("Incorrect dependencies")
        }

        val projectRoot = configPOJO.root.joinPath(configPOJO.projectName)

        writeRootFolder(configPOJO.root)
        writeRootFolder(projectRoot)
        GradlewGenerator.generateGradleW(projectRoot)

        val moduleBlueprints = (0 until configPOJO.numModules).map { i ->
            blueprintFactory.create(i, configPOJO, projectRoot)
        }


        projectBuildGradleGenerator.generate(projectRoot)

        val allJobs = mutableListOf<Job>()
        moduleBlueprints.forEach{ blueprint ->
            val job = launch {
                writeModule(blueprint, configPOJO)
            }
            allJobs.add(job)
        }
        for ((index, job) in allJobs.withIndex()) {
            println("Done writing module " + index)
            job.join()
        }


        val androidModuleBlueprints =
                (0 until configPOJO.androidModules!!.toInt()).map { i ->
            blueprintFactory.createAndroidModule(i, configPOJO, projectRoot, moduleBlueprints.map { it.name })
        }

        androidModuleBlueprints.forEach{ blueprint ->
            androidModuleGenerator.generate(blueprint)
            println("Done writing Android module " + blueprint.index)
        }

        gradleSettingsGenerator.generate(configPOJO.projectName, moduleBlueprints, androidModuleBlueprints, projectRoot)

    }

    private fun writeModule(moduleBlueprint: ModuleBlueprint, configPOJO: ConfigPOJO) {
        val moduleRootPath = moduleBlueprint.root
        val moduleRoot = moduleRootPath.joinPath(moduleBlueprint.name)
        val moduleRootFile = File(moduleRoot)
        moduleRootFile.mkdir()

        writeLibsFolder(moduleRootFile)
        writeBuildGradle(moduleRootFile, moduleBlueprint)

        packagesGenerator.writePackages(PackagesBlueprint(configPOJO, moduleBlueprint.index,
                moduleRoot + "/src/main/java/", File(moduleRootPath)))
    }

    private fun writeBuildGradle(moduleRootFile: File, moduleBlueprint: ModuleBlueprint) {
        val libRoot = moduleRootFile.toString() + "/build.gradle/"
        val content = buildGradleGenerator.create(moduleBlueprint)
        fileWriter.writeToFile(content, libRoot)
    }

    private fun writeLibsFolder(moduleRootFile: File) {
        // write libs
        val libRoot = moduleRootFile.toString() + "/libs/"
        File(libRoot).mkdir()
    }

    private fun writeRootFolder(projectRoot: String) {
        val root = File(projectRoot)

        if (!root.exists()) {
            root.mkdir()
        } else {
            root.delete()
            root.mkdir()
        }
    }
}
