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


package com.google.androidstudiopoet.generators.android_modules.resources

import com.google.androidstudiopoet.models.ResourcesBlueprint
import com.google.androidstudiopoet.utils.joinPath
import com.google.androidstudiopoet.writers.FileWriter
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

class ImagesGenerator(val fileWriter: FileWriter) {

    /**
     * generates image resources by blueprint, returns list of image names to refer later.
     */
    fun generate(blueprint: ResourcesBlueprint, random: Random) {

        val imagesDir = blueprint.resDirPath.joinPath("drawable")

        fileWriter.mkdir(imagesDir)

        blueprint.imageNames.forEach { imageName ->
            val image = generateRandomImage(random)

            ImageIO.write(image,
                    "png",
                    File(imagesDir, "$imageName.png"))
        }
    }

    private fun generateRandomImage(random: Random): BufferedImage {
        // Create buffered image object
        val img = BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB)

        // create random values pixel by pixel
        for (y in 0 until 100) {
            for (x in 0 until 100) {
                val a = random.nextInt(256) //generating
                val r = random.nextInt(256) //values
                val g = random.nextInt(256) //less than
                val b = random.nextInt(256) //256

                val p = a shl 24 or (r shl 16) or (g shl 8) or b //pixel

                img.setRGB(x, y, p)
            }
        }

        return img
    }
}