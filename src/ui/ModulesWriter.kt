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
        }
    }

    private fun writeModule(index: Int, configPOJO: ConfigPOJO) {
        val moduleRoot = configPOJO.root + "/module" + index + "/"
        val moduleRootFile = File(moduleRoot)
        moduleRootFile.mkdir()

        writeLibsFolder(moduleRootFile, configPOJO)
        writeBuildGradle(moduleRootFile, configPOJO)

        val packagesWriter = PackagesWriter()
        packagesWriter.writePackages(configPOJO,
                moduleRoot + "/src/main/java/")
    }

    private fun writeBuildGradle(moduleRootFile: File, configPOJO: ConfigPOJO) {
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

    private fun writeLibsFolder(moduleRootFile: File, configPOJO: ConfigPOJO) {
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
