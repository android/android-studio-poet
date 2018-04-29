package com.google.androidstudiopoet.writers

import org.apache.commons.io.FileUtils
import java.io.File

interface AbstractWriter {

    fun mkdir(path: String) {
        val file = File(path)
        if (file.parentFile != null && !file.parentFile.exists()) {
            mkdir(file.parent)
        }
        File(path).mkdir()
    }

    fun delete(path: String) {
        val file = File(path)
        if (file.isDirectory && !FileUtils.isSymlink(file)) {
            file.listFiles()?.forEach { delete(it.absolutePath) }
        }
        file.delete()
    }
}