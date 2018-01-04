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

import com.google.androidstudiopoet.utils.joinLines

annotation class Fancy

@Fancy
object BuildGradle {

    fun print(dependencies: String, useKotlin: Boolean, generateTests: Boolean, extraLines: List<String>? = null) = "" +
            "apply plugin: 'java-library'\n" +
            (if (useKotlin)
                "apply plugin: 'kotlin'\n"
            else
                "") +
            "\n" +
            "dependencies {\n" +
            "    implementation fileTree(dir: 'libs', include: ['*.jar'])\n" +
            (if (useKotlin)
                "    compile \"org.jetbrains.kotlin:kotlin-stdlib-jre8:\$kotlin_version\"\n"
            else
                "") +
            (if (generateTests)
                "    testCompile \"junit:junit:4.12\"\n"
            else
                "") +
            dependencies +
            "}\n" +
            "\n" +
            "sourceCompatibility = \"1.8\"\n" +
            "targetCompatibility = \"1.8\"\n" +
            "buildscript {\n" +
            "    repositories {\n" +
            "        mavenCentral()\n" +
            "    }\n" +
            "    dependencies {\n" +
            (if (useKotlin)
                "        classpath \"org.jetbrains.kotlin:kotlin-gradle-plugin:\$kotlin_version\"\n"
            else
                "") +
            "    }\n" +
            "}\n" +
            "repositories {\n" +
            "    mavenCentral()\n" +
            "}\n" +
            (if (useKotlin)
                "compileKotlin {\n" +
                "    kotlinOptions {\n" +
                "        jvmTarget = \"1.8\"\n" +
                "    }\n" +
                "}\n" +
                "compileTestKotlin {\n" +
                "    kotlinOptions {\n" +
                "        jvmTarget = \"1.8\"\n" +
                "    }\n" +
                (if (generateTests)
                    "    dependencies {\n" +
                    "        testCompile \"junit:junit:4.12\"\n" +
                    "    }\n"
                else
                    "") +
                "}\n"
            else
                "" ) +
            extraLines.joinLines()
}
