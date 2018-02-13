/*
Copyright 2017 Google Inc.

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

package com.google.androidstudiopoet.generators.android_modules.resources

import com.google.androidstudiopoet.models.LayoutBlueprint
import com.google.androidstudiopoet.utils.fold
import com.google.androidstudiopoet.writers.FileWriter
import org.intellij.lang.annotations.Language

class LayoutResourcesGenerator(val fileWriter: FileWriter) {

    fun generate(blueprint: LayoutBlueprint) {
        val textViews = generateTextViews(blueprint.stringNamesToUse)
        val imageViews = generateImageViews(blueprint.imagesToUse)
        val includeLayoutTags = generateIncludeLayoutTags(blueprint.layoutsToInclude)
        val layoutText = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                getLayoutViews(textViews, imageViews, includeLayoutTags, true)
        fileWriter.writeToFile(layoutText, blueprint.filePath)
    }

    private fun getLayoutViews(textViews: String, imageViews: String, includeLayoutTags: String,
                               isNotInsideLayoutTag: Boolean): String {
        return """
    <ScrollView
            ${if (isNotInsideLayoutTag)  "xmlns:android=\"http://schemas.android.com/apk/res/android\"" else ""}
            android:layout_width="match_parent"
            android:layout_height="match_parent">
        <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical">
            $textViews
            $imageViews
            $includeLayoutTags
        </LinearLayout>
    </ScrollView>"""
    }

    private fun generateTextViews(stringNamesToUse: List<String>): String {
        return stringNamesToUse.map {
            //language=XML
            """<TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/$it"
/>"""
        }.fold()
    }

    private fun generateImageViews(imagesToUse: List<String>): String {
        return imagesToUse.map {
            //language=XML
            """<ImageView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:src="@drawable/$it"
/>
            """
        }.fold()
    }

    private fun generateIncludeLayoutTags(layoutsToInclude: List<String>): String {
        return layoutsToInclude.map {
            //language=XML
            """<include
        layout="@layout/$it"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
/>
            """
        }.fold()
    }
}