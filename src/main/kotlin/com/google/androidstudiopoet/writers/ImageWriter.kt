package com.google.androidstudiopoet.writers

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class ImageWriter : AbstractWriter {
    fun writeToFile(content: BufferedImage, path: String) {
        ImageIO.write(content, "png", File(path))
    }
}