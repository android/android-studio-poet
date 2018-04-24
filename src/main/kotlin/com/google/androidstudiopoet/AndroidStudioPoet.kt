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

import com.google.androidstudiopoet.converters.ConfigPojoToProjectConfigConverter
import com.google.androidstudiopoet.generators.SourceModuleGenerator
import com.google.androidstudiopoet.input.ConfigPOJO
import com.google.androidstudiopoet.input.GenerationConfig
import com.google.androidstudiopoet.input.ProjectConfig
import com.google.androidstudiopoet.models.ProjectBlueprint
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import org.intellij.lang.annotations.Language
import java.awt.*
import java.io.File
import javax.swing.*
import javax.swing.JFrame.EXIT_ON_CLOSE
import javax.swing.border.EmptyBorder
import kotlin.system.measureTimeMillis


class AndroidStudioPoet(private val modulesGenerator: SourceModuleGenerator, private val filename: String?,
                        private val configPojoToProjectConfigConverter: ConfigPojoToProjectConfigConverter,
                        private val dependencyValidator: DependencyValidator, private val gson: Gson) {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            AndroidStudioPoet(Injector.modulesWriter, args.firstOrNull(),
                    Injector.configPojoToProjectConfigConverter,
                    Injector.dependencyValidator, Injector.gson).run()
        }

        @Language("JSON")
        val CONFIG_COMPACT = """
            {
              "projectName": "GeneratedASProject",
              "root": "./../",
              "gradleVersion": "4.3.1",
              "androidGradlePluginVersion": "3.0.1",
              "kotlinVersion": "1.1.60",
              "numModules": "2",
              "allMethods": "40",
              "javaPackageCount": "1",
              "javaClassCount": "4",
              "javaMethodCount": "20",
              "kotlinPackageCount": "1",
              "kotlinClassCount": "4",
              "androidModules": "2",
              "numActivitiesPerAndroidModule": "2",
              "productFlavors": [
                  2, 3
               ],
               "topologies": [
                  {"type": "star", "seed": "2"}
               ],
              "dependencies": [
                {"from": "module1", "to": "module0"}
              ],
              "buildTypes": 2,
              "generateTests": true
            }
            """.trimIndent()
    }

    fun run() {
        when {
            filename != null -> processFile(filename)
            else -> showUI(CONFIG_COMPACT)
        }
    }

    private fun showUI(jsonText: String) {
        EventQueue.invokeLater {
            try {
                val frame = createUI(jsonText)
                frame.isVisible = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun createUI(jsonText: String): JFrame {
        val frame = JFrame()
        val textArea = createTextArea(jsonText)
        val scrollPane = JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS)

        val btnGenerate = JButton("Generate").apply {
            addActionListener {
                try {
                    val text = textArea.text
                    println(text)
                    processInput(text)

                } catch (e: Exception) {
                    println("ERROR: the generation failed due to JSON script errors - " +
                            "please fix and try again")
                    e.printStackTrace()
                }
            }
        }

        val contentPane = JPanel().apply {
            border = EmptyBorder(5, 5, 5, 5)
            layout = BorderLayout(0, 0)
            add(createTitleLabel(), BorderLayout.NORTH)
            add(scrollPane, BorderLayout.CENTER)
            add(btnGenerate, BorderLayout.SOUTH)
        }

        val dim = Toolkit.getDefaultToolkit().screenSize

        frame.setLocation((dim.width - frame.size.width)/3,
                (dim.height - frame.height)/5 )

        frame.defaultCloseOperation = EXIT_ON_CLOSE
        frame.contentPane = contentPane
        frame.pack()

        return frame
    }

    private fun processInput(jsonText: String) {
        val input = parseInput(jsonText)
        when (input) {
            is GenerationConfig -> processInput(input, jsonText)
            is ConfigPOJO -> processInput(input, jsonText)
            else -> println("Can't parse json")
        }
    }

    private fun processInput(config: GenerationConfig, jsonText: String) {
        println("Input version: ${config.inputVersion}")
        config.projectConfig.jsonText = jsonText
        startGeneration(config.projectConfig)
    }

    private fun processInput(configPOJO: ConfigPOJO, jsonText: String) {

        if (!dependencyValidator.isValid(configPOJO.dependencies ?: listOf(), configPOJO.numModules, configPOJO.androidModules)) {
            throw IllegalStateException("Incorrect dependencies")
        }

        val projectConfig = configPojoToProjectConfigConverter.convert(configPOJO)
        projectConfig.jsonText = jsonText

        startGeneration(projectConfig)
    }

    private fun startGeneration(projectConfig: ProjectConfig) {
        var projectBluePrint: ProjectBlueprint? = null
        val timeSpent = measureTimeMillis {
            projectBluePrint = ProjectBlueprint(projectConfig)
            modulesGenerator.generate(projectBluePrint!!)
            if (projectBluePrint!!.hasCircularDependencies()) {
                println("WARNING: there are circular dependencies")
            }
        }
        println("Finished in $timeSpent ms")
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

    private fun processFile(filename: String?): Any? = when {
        filename == null -> null
        !File(filename).canRead() -> null
        else -> File(filename).readText().let { processInput(it) }
    }

    private fun parseInput(json: String): Any? {
        val configJsonObject = JsonParser().parse(json).asJsonObject
        return when {
            configJsonObject.has("inputVersion") -> generationConfigFrom(configJsonObject, gson)
            else -> configPojoFrom(configJsonObject, gson)
        }
    }

    private fun configPojoFrom(configJsonObject: JsonObject, gson: Gson): ConfigPOJO? {
        return try {
            gson.fromJson(configJsonObject, ConfigPOJO::class.java)
        } catch (js: JsonSyntaxException) {
            System.err.println("Cannot parse json: $js")
            null
        }
    }

    private fun generationConfigFrom(configJsonObject: JsonObject, gson: Gson): GenerationConfig? {
        return try {
            gson.fromJson(configJsonObject, GenerationConfig::class.java)
        } catch (js: JsonSyntaxException) {
            System.err.println("Cannot parse json as GenerationConfig: $js")
            null
        }
    }
}
