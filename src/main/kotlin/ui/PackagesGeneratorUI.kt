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

package ui

import org.intellij.lang.annotations.Language
import ui.generators.BuildGradleGenerator
import ui.generators.project.GradleSettingsGenerator
import ui.generators.project.ProjectBuildGradleGenerator
import java.awt.BorderLayout
import java.awt.Color
import java.awt.EventQueue
import java.awt.Font
import javax.swing.*
import javax.swing.border.EmptyBorder

class PackagesGeneratorUI(private val modulesWriter: ModulesWriter) : JFrame() {

    private val contentPane: JPanel
    private val textArea: JTextArea

    init {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        contentPane = JPanel()
        contentPane.border = EmptyBorder(5, 5, 5, 5)
        contentPane.layout = BorderLayout(0, 0)
        setContentPane(contentPane)

        val lblTextLineExample = JLabel("Android Project Generator")
        lblTextLineExample.horizontalAlignment = SwingConstants.CENTER
        contentPane.add(lblTextLineExample, BorderLayout.NORTH)

        textArea = JTextArea()

        textArea.background = Color(46, 48, 50)
        textArea.foreground = Color.CYAN
        textArea.font = Font("Menlo", Font.PLAIN, 18)

        textArea.text = SAMPLE_CONFIG
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
            modulesWriter.generate(textArea.text)
        }
        contentPane.add(btnGenerate, BorderLayout.SOUTH)

        pack()
    }

    companion object {

        @Language("JSON") val SAMPLE_CONFIG = "{\n" +
                "  \"projectName\": \"genny\",\n" +
                "  \"root\": \"/Users/bfarber/Desktop/modules/\",\n" +
                "  \"numModules\": \"5\",\n" +
                "  \"allMethods\": \"4000\",\n" +
                "  \"javaPackageCount\": \"20\",\n" +
                "  \"javaClassCount\": \"8\",\n" +
                "  \"javaMethodCount\": \"2000\",\n" +
                "  \"kotlinPackageCount\": \"20\",\n" +
                "  \"kotlinClassCount\": \"8\",\n" +
                "  \"androidModules\": \"1\",\n" +
                "  \"numActivitiesPerAndroidModule\": \"8\",\n" +
                "  \"dependencies\": [{\"from\": 3, \"to\": 2},\n" +
                "    {\"from\": 4, \"to\": 2}, {\"from\": 4, \"to\": 3}]\n" +
                "}"

        @JvmStatic
        fun main(args: Array<String>) {

            EventQueue.invokeLater {
                try {
                    val fileWriter = FileWriter()
                    val frame = PackagesGeneratorUI(ModulesWriter(DependencyValidator(),
                            ModuleBlueprintFactory(),
                            BuildGradleGenerator(),
                            GradleSettingsGenerator(fileWriter),
                            ProjectBuildGradleGenerator(),
                            fileWriter))
                    frame.isVisible = true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}