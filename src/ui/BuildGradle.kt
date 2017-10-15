package ui

object BuildGradle {

    val TEXT = "" +
            "apply plugin: 'java-library'\n" +
            "apply plugin: 'kotlin'\n" +
            "\n" +
            "dependencies {\n" +
            "    implementation fileTree(dir: 'libs', include: ['*.jar'])\n" +
            "    compile \"org.jetbrains.kotlin:kotlin-stdlib-jre8:\$kotlin_version\"\n" +
            "}\n" +
            "\n" +
            "sourceCompatibility = \"1.7\"\n" +
            "targetCompatibility = \"1.7\"\n" +
            "buildscript {\n" +
            "    ext.kotlin_version = '1.1.51'\n" +
            "    repositories {\n" +
            "        mavenCentral()\n" +
            "    }\n" +
            "    dependencies {\n" +
            "        classpath \"org.jetbrains.kotlin:kotlin-gradle-plugin:\$kotlin_version\"\n" +
            "    }\n" +
            "}\n" +
            "repositories {\n" +
            "    mavenCentral()\n" +
            "}\n" +
            "compileKotlin {\n" +
            "    kotlinOptions {\n" +
            "        jvmTarget = \"1.8\"\n" +
            "    }\n" +
            "}\n" +
            "compileTestKotlin {\n" +
            "    kotlinOptions {\n" +
            "        jvmTarget = \"1.8\"\n" +
            "    }\n" +
            "}\n"

}
