import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JavaGenerator {

    public static void main(String[] args) {

        // directory where generator should put all generated packages
        File mainPackage = new File(System.getProperty("user.dir") + "/src/");
        System.out.println(mainPackage.getAbsolutePath());


        // how many packages should be generated
        int packageCounter = 1;

        // how many classes should be generated in each package
        int classCounter = 2;

        // how many method should be generated all together (!!!)
        int methodCounter = 4;

        // TODO not sure if it is accurate
        int methodsPerClass = methodCounter / (classCounter * packageCounter);


        System.out.println(packageCounter + " packages, " + classCounter + " classes, " +
                methodCounter + " methods, " + methodsPerClass + " methods per class");

        for (int i = 0; i < packageCounter; i++) {
            generatePackage(i, classCounter, methodsPerClass, mainPackage);
        }
    }

    private static void generatePackage(int packageNumber, int classCounter,
                                        int methodsPerClass, File mainPackage) {
        String packageName = "package_" + packageNumber;
        File packageFolder = new File(mainPackage + "/" + packageName);
        if (packageFolder.exists()) {
            packageFolder.delete();
        }

        packageFolder.mkdir();

        for (int i = 0; i < classCounter; i++) {
            generateClass(packageName, i, methodsPerClass, mainPackage);
        }
    }

    private static void generateClass(String packageName, int classNumber,
                                      int methodsPerClass, File mainPackage) {

        String className = "Foo" + classNumber;
        StringBuilder buff = new StringBuilder();

        buff.append("package " + packageName + ";\n");
        buff.append("public class " + className + " {\n");


        for (int i = 0; i < methodsPerClass; i++) {
            if(i == 0) {
                buff.append("public void foo" + i + "(){\n}\n");
            }

            if (i > 0) {
                buff.append("public void foo" + i + "() {\n");

                if (classNumber > 0) {
                    buff.append("new Foo" + (classNumber - 1) + "().foo" + (methodsPerClass - 1) + "();\n");
                }

                buff.append("\n}\n");
            }
        }

        buff.append("\n}");

        // TODO end

        String classPath = mainPackage.getAbsolutePath() + "/" + packageName+
                "/"+ className + ".java";

        try {
            File classFile = new File(classPath);
            classFile.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(classFile));
            writer.write(buff.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
