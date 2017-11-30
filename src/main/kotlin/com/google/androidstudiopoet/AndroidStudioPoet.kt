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

class AndroidStudioPoet(private val modulesWriter: SourceModuleWriter, config: Array<String>) : JFrame() {

    private val contentPane: JPanel
    private val textArea: JTextArea

    @Language("JSON") private val SAMPLE_CONFIG = "{\n" +
            "  \"projectName\": \"genny\",\n" +
            "  \"root\": \"./modules/\",\n" +
            "  \"numModules\": \"5\",\n" +
            "  \"allMethods\": \"4000\",\n" +
            "  \"javaPackageCount\": \"20\",\n" +
            "  \"javaClassCount\": \"8\",\n" +
            "  \"javaMethodCount\": \"2000\",\n" +
            "  \"kotlinPackageCount\": \"20\",\n" +
            "  \"kotlinClassCount\": \"8\",\n" +
            "  \"androidModules\": \"2\",\n" +
            "  \"numActivitiesPerAndroidModule\": \"8\",\n" +
            "  \"productFlavors\": [2, 3, 4],\n" +
            "  \"topologies\": [{\"type\": \"random\", \"seed\": \"2\"}],\n" +
            "  \"dependencies\": [{\"from\": 3, \"to\": 2},\n" +
            "    {\"from\": 4, \"to\": 2}, {\"from\": 4, \"to\": 3}]\n" +
            "}"

    private val gson = Gson()

    init {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        contentPane = JPanel()
        contentPane.border = EmptyBorder(5, 5, 5, 5)
        contentPane.layout = BorderLayout(0, 0)
        setContentPane(contentPane)

        val lblTextLineExample = JLabel("Android Studio Poet")
        lblTextLineExample.horizontalAlignment = SwingConstants.CENTER
        contentPane.add(lblTextLineExample, BorderLayout.NORTH)

        textArea = JTextArea()

        textArea.background = Color(46, 48, 50)
        textArea.foreground = Color.CYAN
        textArea.font = Font("Menlo", Font.PLAIN, 18)

        textArea.text = generateConfigTextFromArgs(config)
        textArea.caretPosition = textArea.text.length
        textArea.caretColor = Color.YELLOW

        val scrollPane = JScrollPane(textArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS)
        textArea.rows = 30
        textArea.columns = 50

        contentPane.add(scrollPane, BorderLayout.CENTER)

        val btnGenerate = JButton("Generate")
        btnGenerate.addActionListener {
            println(textArea.text)
            val configPOJO = gson.fromJson(textArea.text, ConfigPOJO::class.java)
            modulesWriter.generate(ProjectBlueprint(configPOJO))
        }
        contentPane.add(btnGenerate, BorderLayout.SOUTH)

        pack()
    }

    private fun generateConfigTextFromArgs(args: Array<String>): String? {

        if (!args.isEmpty()) {
            return try {
                var configFile = File(args[0])
                var result: String = configFile.readText()
                gson.fromJson(result, ConfigPOJO::class.java)
                result
            } catch (jss: JsonSyntaxException) {
                SAMPLE_CONFIG
            }
        }

        return SAMPLE_CONFIG
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            EventQueue.invokeLater {
                try {
                    val frame = AndroidStudioPoet(Injector.modulesWriter, args)
                    frame.isVisible = true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
