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


package main.generators.android_modules

import main.models.AndroidModuleBlueprint
import main.writers.FileWriter
import main.utils.joinPath
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class ImagesGenerator(val fileWriter: FileWriter) {

    /**
     * generates image resources by blueprint, returns list of image names to refer later.
     */
    fun generate(blueprint: AndroidModuleBlueprint): List<String> {

        var result: ArrayList<String> = ArrayList()

        val imagesDir = blueprint.resDirPath.joinPath("drawable")
        fileWriter.mkdir(imagesDir)

        for (i in 0 until blueprint.numOfImages) {
            var image = generateRandomImage()
            var imageName:String = "image$i"

            ImageIO.write(image,
                    "jpg",
                    File(imagesDir,imageName + ".jpg"))

            result.add(imageName)
        }
        return result
    }

    private fun generateRandomImage(): BufferedImage {
        // Create buffered image object
        var img = BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB)

        // create random values pixel by pixel
        for (y in 0 until 100) {
            for (x in 0 until 100) {
                val a = (Math.random() * 256).toInt() //generating
                val r = (Math.random() * 256).toInt() //values
                val g = (Math.random() * 256).toInt() //less than
                val b = (Math.random() * 256).toInt() //256

                val p = a shl 24 or (r shl 16) or (g shl 8) or b //pixel

                img.setRGB(x, y, p)
            }
        }

        return img
    }
}