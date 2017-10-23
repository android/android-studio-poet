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

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

class PackagesWriter {

    private var packagesRoot: File = File("")
    private var moduleRoot: File = File("")
    fun writePackages(config: ConfigPOJO, moduleIndex: Int, where: String) {

        packagesRoot = File(where)
        packagesRoot.mkdirs()
        moduleRoot = File(config.root)

        for (packageIndex in 0 until config.javaPackageCount!!.toInt()) {
            generateJavaPackage(packageIndex, moduleIndex, config.javaClassCount!!.toInt(),
                    config.javaMethodsPerClass, packagesRoot)
        }

        val kotlinMethodsPerClass = config.kotlinMethodsPerClass

        for (packageIndex in 0 until config.kotlinPackageCount!!.toInt()) {
            var kotlinPackageIndex = (packageIndex + config.javaPackageCount!!.toInt())
            generateKotlinPackage(kotlinPackageIndex, moduleIndex,
                    config.kotlinPackageCount!!.toInt(), kotlinMethodsPerClass, packagesRoot)
        }
    }

    private fun generateJavaPackage(packageIndex: Int, moduleIndex: Int, classesPerPackage: Int,
                                    methodsPerClass: Int, mainPackage: File) {
        val packageName = moduleRoot.name + moduleIndex +"packageJava" + packageIndex
        val packageFolder = File(mainPackage.toString() + "/" + packageName)
        if (packageFolder.exists()) {
            packageFolder.delete()
        }

        packageFolder.mkdir()

        for (classIndex in 0 until classesPerPackage) {
            generateJavaClass(packageName, classIndex, methodsPerClass, mainPackage)
        }
    }

    private fun generateKotlinPackage(packageIndex: Int, moduleIndex: Int, classesPerPackage: Int,
                                      methodsPerClass: Int, mainPackage: File) {
        // TODO add module index
        val packageName = moduleRoot.name + moduleIndex + "packageKt" + packageIndex
        val packageFolder = File(mainPackage.toString() + "/" + packageName)
        if (packageFolder.exists()) {
            packageFolder.delete()
        }

        packageFolder.mkdir()

        for (i in 0 until classesPerPackage) {
            generateKotlinClass(packageName, i, methodsPerClass, mainPackage)
        }
    }

    private fun generateJavaClass(packageName: String, classNumber: Int,
                                  methodsPerClass: Int, mainPackage: File) {

        val className = "Foo" + classNumber
        val buff = StringBuilder()

        buff.append("package $packageName;\n")
        buff.append("public class $className {\n")

        for (i in 0 until methodsPerClass) {

            buff.append("public void foo$i(){\n")

            if (i > 0) {
                buff.append("foo" + (i - 1) + "();\n")
            } else if (classNumber > 0) {
                buff.append("new Foo" + (classNumber - 1) + "().foo" + (methodsPerClass - 1) + "();\n")
            }

            // adding lambda
            val lambda = "final Runnable anything = () -> System.out.println(\"anything\");"
            buff.append(lambda + "\n")

            buff.append("\n}")
        }

        buff.append("\n}")

        val classPath = mainPackage.absolutePath + "/" + packageName +
                "/" + className + ".java"

        writeFile(classPath, buff)
    }

    private fun generateKotlinClass(packageName: String, classNumber: Int,
                                    methodsPerClass: Int, mainPackage: File) {

        val className = "Foo" + classNumber
        val buff = StringBuilder()

        buff.append("package $packageName;\n")

        var annotName = className + "Fancy"
        buff.append("annotation class " + annotName + "\n")
        buff.append("@" + annotName + "\n")

        buff.append("public class $className {\n")

        for (i in 0 until methodsPerClass) {

            buff.append("fun foo$i(){\n")

            if (i > 0) {
                buff.append("foo" + (i - 1) + "()\n")
            } else if (classNumber > 0) {
                buff.append("Foo" + (classNumber - 1) + "().foo" + (methodsPerClass - 1) + "()\n")
            }

            buff.append("\n}")
        }

        buff.append("\n}")

        val classPath = mainPackage.absolutePath + "/" + packageName +
                "/" + className + ".kt"

        writeFile(classPath, buff)
    }

    private fun writeFile(classPath: String, buff: StringBuilder) {
        try {
            val classFile = File(classPath)
            classFile.createNewFile()
            val writer = BufferedWriter(FileWriter(classFile))
            writer.write(buff.toString())
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

