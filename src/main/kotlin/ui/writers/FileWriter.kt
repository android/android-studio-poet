package ui.writers

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

class FileWriter() {
    fun writeToFile(content: String, path: String) {
        var writer: BufferedWriter? = null
        try {
            val buildGradle = File(path)
            buildGradle.createNewFile()

            writer = BufferedWriter(FileWriter(buildGradle))

            writer.write(content)

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                writer?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun mkdir(path: String) {
        File(path).mkdir()
    }
}