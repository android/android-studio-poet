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

package ui

import com.google.gson.Gson
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

class ModulesWriter {

    fun generate(configStr: String) {

        val gson = Gson()
        val configPOJO = gson.fromJson(configStr, ConfigPOJO::class.java)

        writeRootFolder(configPOJO)

        for (i in 0 until Integer.parseInt(configPOJO.numModules!!)) {
            writeModule(i, configPOJO)
            println("Done writing module " + i)
        }
    }

    private fun writeModule(index: Int, configPOJO: ConfigPOJO) {
        val moduleRoot = configPOJO.root + "/module" + index + "/"
        val moduleRootFile = File(moduleRoot)
        moduleRootFile.mkdir()

        writeLibsFolder(moduleRootFile)
        writeBuildGradle(moduleRootFile)

        val packagesWriter = PackagesWriter()
        packagesWriter.writePackages(configPOJO,
                moduleRoot + "/src/main/java/")
    }

    private fun writeBuildGradle(moduleRootFile: File) {
        val libRoot = moduleRootFile.toString() + "/build.gradle/"
        var writer: BufferedWriter? = null
        try {
            val buildGradle = File(libRoot)
            buildGradle.createNewFile()

            writer = BufferedWriter(FileWriter(buildGradle))
            writer.write(BuildGradle.TEXT)

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                writer!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun writeLibsFolder(moduleRootFile: File) {
        // write libs
        val libRoot = moduleRootFile.toString() + "/libs/"
        File(libRoot).mkdir()
    }

    private fun writeRootFolder(configPOJO: ConfigPOJO) {
        val root = File(configPOJO.root!!)

        if (!root.exists()) {
            root.mkdir()
        } else {
            root.delete()
            root.mkdir()
        }
    }
}
