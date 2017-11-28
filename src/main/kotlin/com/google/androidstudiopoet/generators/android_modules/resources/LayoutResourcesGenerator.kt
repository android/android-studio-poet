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
        @Language("XML") val layoutText = """<?xml version="1.0" encoding="utf-8"?>
<ScrollView
        xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
    $textViews
    $imageViews
    $includeLayoutTags
</ScrollView>"""
        fileWriter.writeToFile(layoutText, blueprint.filePath)
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