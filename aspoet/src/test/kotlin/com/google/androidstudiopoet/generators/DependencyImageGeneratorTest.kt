/*
Copyright 2018 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.google.androidstudiopoet.generators

import com.google.androidstudiopoet.testutils.*
import com.google.androidstudiopoet.utils.joinPath
import com.google.androidstudiopoet.writers.ImageWriter
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import org.junit.Test
import com.nhaarman.mockito_kotlin.verify
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

class DependencyImageGeneratorTest: DependencyGraphBase() {
    private val imageWriter: ImageWriter = mock()
    private val dependencyImageGenerator = DependencyImageGenerator(imageWriter)

    @Test
    fun `generated image content is what is expected`() {
        val blueprint = getProjectBlueprint()
        dependencyImageGenerator.generate(blueprint)
        val expectedPath = "projectRoot".joinPath("dependencies.png")
        val stream = this.javaClass.getResourceAsStream("dependencyGraphTest.png")
        val expectedImage = ImageIO.read(stream)
        val imgCaptor = argumentCaptor<BufferedImage>()
        verify(imageWriter).writeToFile(imgCaptor.capture(), eq(expectedPath))
        assertOn(imgCaptor) {
            allValues.assertSize(1)
            allValues[0].assertEqualImage(expectedImage)
        }
    }
}