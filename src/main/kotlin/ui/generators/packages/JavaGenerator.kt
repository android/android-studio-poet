package ui.generators.packages

import ui.FileWriter
import java.io.File

class JavaGenerator constructor(fileWriter: FileWriter): PackageGenerator(fileWriter, "Java") {

    override fun generateClass(packageName: String, classNumber: Int, methodsPerClass: Int, mainPackage: File) {
        val className = "Foo" + classNumber
        val buff = StringBuilder()

        buff.append("packages $packageName;\n")
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

        writeFile(classPath, buff.toString())
    }

}
