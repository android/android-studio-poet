package ui.generators.packages

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import ui.FileWriter
import java.io.File
import javax.lang.model.element.Modifier

class JavaGenerator constructor(fileWriter: FileWriter): PackageGenerator(fileWriter, "Java") {

    override fun generateClass(packageName: String, classNumber: Int, methodsPerClass: Int, mainPackage: File) {
        val className = "Foo" + classNumber

        val clazz = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)

        for (i in 0 until methodsPerClass) {
            clazz.addMethod(generateMethod(i, classNumber, methodsPerClass))
        }

        val javaFile = JavaFile.builder(packageName, clazz.build()).build()

        val classPath = mainPackage.absolutePath + "/" + packageName +
                "/" + className + ".java"

        writeFile(classPath, javaFile.toString())
    }

    private fun generateMethod(methodNumber: Int, classNumber: Int, methodsPerClass: Int): MethodSpec {
        val methodName = "foo" + methodNumber

        val method = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(Void.TYPE)

        if (methodNumber > 0) {
            method.addStatement("foo" + (methodNumber - 1) + "()")
        } else if (classNumber > 0) {
            method.addStatement("new Foo" + (classNumber - 1) + "().foo" + (methodsPerClass - 1) + "()")
        }

        // adding lambda
        method.addStatement("final Runnable anything = () -> System.out.println(\"anything\")")

        return method.build()
    }

}
