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

package com.google.androidstudiopoet

import com.google.androidstudiopoet.models.ConfigPOJO
import com.google.androidstudiopoet.models.ProjectBlueprint
import com.google.androidstudiopoet.writers.SourceModuleWriter
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.intellij.lang.annotations.Language
import java.awt.BorderLayout
import java.awt.Color
import java.awt.EventQueue
import java.awt.Font
import java.io.File
import javax.swing.*
import javax.swing.border.EmptyBorder

class AndroidStudioPoet(private val modulesWriter: SourceModuleWriter, filename: String?) : JFrame() {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            EventQueue.invokeLater {
                try {
                    val frame = AndroidStudioPoet(Injector.modulesWriter, args.firstOrNull())
                    frame.isVisible = true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        @Language("JSON") const val SAMPLE_CONFIG = """
            {
              "projectName": "genny",
              "root": "./modules/",
              "gradleVersion": "4.3.1",
              "androidGradlePluginVersion": "3.0.1",
              "kotlinVersion": "1.1.60",
              "numModules": "5",
              "allMethods": "4000",
              "javaPackageCount": "20",
              "javaClassCount": "8",
              "javaMethodCount": "2000",
              "kotlinPackageCount": "20",
              "kotlinClassCount": "8",
              "androidModules": "2",
              "numActivitiesPerAndroidModule": "8",
              "productFlavors": [
                  2, 3, 4
               ],
               "topologies": [
                  {"type": "random", "seed": "2"}
               ],
              "dependencies": [
                {"from": 3, "to": 2},
                {"from": 4, "to": 2},
                {"from": 4, "to": 3}
              ]
            }
            """
    }

    init {

        val jsonText = fromFileNameOrDefault(filename)

        val textArea = createTextArea(jsonText)
        val scrollPane = JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS)

        val btnGenerate = JButton("Generate").apply {
            addActionListener {
                println(textArea.text)
                val config: ConfigPOJO = configFrom(textArea.text) ?: configFrom(SAMPLE_CONFIG)!!
                modulesWriter.generate(ProjectBlueprint(config))
            }
        }

        val contentPane = JPanel().apply {
            border = EmptyBorder(5, 5, 5, 5)
            layout = BorderLayout(0, 0)
            add(createTitleLabel(), BorderLayout.NORTH)
            add(scrollPane, BorderLayout.CENTER)
            add(btnGenerate, BorderLayout.SOUTH)
        }

        defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        setContentPane(contentPane)

        pack()
    }


    private fun createTitleLabel(): JLabel {
        return JLabel("Android Studio Poet").apply {
            horizontalAlignment = SwingConstants.CENTER
        }
    }

    private fun createTextArea(jsonText: String): JTextArea {
        return JTextArea().apply {
            background = Color(46, 48, 50)
            foreground = Color.CYAN
            font = Font("Menlo", Font.PLAIN, 18)
            text = jsonText
            caretPosition = text.length
            caretColor = Color.YELLOW
            rows = 30
            columns = 50
        }
    }

    private fun fromFileNameOrDefault(filename: String?): String = when {
        filename == null -> SAMPLE_CONFIG
        !File(filename).canRead() -> SAMPLE_CONFIG
        else -> File(filename).readText().let { json ->
            if (configFrom(json) == null) {
                SAMPLE_CONFIG
            } else {
                json
            }
        }
    }


    private fun configFrom(json: String): ConfigPOJO? {

        val gson = Gson()

        try {
            return gson.fromJson(json, ConfigPOJO::class.java)
        } catch (js: JsonSyntaxException) {
            System.err.println("Cannot parse json: $js")
            return null
        }
    }
}