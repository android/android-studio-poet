import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

object JavaPackagesGenerator {

    @JvmStatic
    fun main(args: Array<String>) {

        // directory where generator should put all generated packages
        val mainPackage = File(System.getProperty("user.dir") + "/src/")
        println(mainPackage.absolutePath)

        // how many packages should be generated
        val packageCounter = 4

        // how many classes should be generated in each package
        val classCounter = 50

        // how many method should be generated all together (!!!)
        val methodCounter = 4000

        val methodsPerClass = methodCounter / (classCounter * packageCounter)

        println(packageCounter.toString() + " packages, " + classCounter + " classes, " +
                methodCounter + " methods, " + methodsPerClass + " methods per class")

        for (i in 0 until packageCounter) {
            generatePackage(i, classCounter, methodsPerClass, mainPackage)
        }
    }

    private fun generatePackage(packageNumber: Int, classCounter: Int,
                                methodsPerClass: Int, mainPackage: File) {
        val packageName = "package_" + packageNumber
        val packageFolder = File(mainPackage.toString() + "/" + packageName)
        if (packageFolder.exists()) {
            packageFolder.delete()
        }

        packageFolder.mkdir()

        for (i in 0 until classCounter) {
            generateClass(packageName, i, methodsPerClass, mainPackage)
        }
    }

    private fun generateClass(packageName: String, classNumber: Int,
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

            buff.append("\n}")
        }

        buff.append("\n}")

        val classPath = mainPackage.absolutePath + "/" + packageName +
                "/" + className + ".java"

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
