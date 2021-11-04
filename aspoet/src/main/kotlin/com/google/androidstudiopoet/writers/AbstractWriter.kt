package com.google.androidstudiopoet.writers

import java.io.File
import java.nio.file.Files

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
        if (!Files.isSymbolicLink(file.toPath())) {
            file.listFiles()?.forEach { delete(it.absolutePath) }
        }
        file.delete()
    }
}