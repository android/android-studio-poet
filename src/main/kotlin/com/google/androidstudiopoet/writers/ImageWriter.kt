package com.google.androidstudiopoet.writers

import java.awt.image.RenderedImage
import java.io.File
import javax.imageio.ImageIO

class ImageWriter : AbstractWriter {
    fun writeToFile(content: RenderedImage, path: String) {
        ImageIO.write(content, "png", File(path))
    }
}