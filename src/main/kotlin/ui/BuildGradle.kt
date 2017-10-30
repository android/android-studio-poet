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

annotation class Fancy

@Fancy
object BuildGradle {

    fun print(dependencies: String) = "" +
            "apply plugin: 'java-library'\n" +
            "apply plugin: 'kotlin'\n" +
            "\n" +
            "dependencies {\n" +
            "    implementation fileTree(dir: 'libs', include: ['*.jar'])\n" +
            "    compile \"org.jetbrains.kotlin:kotlin-stdlib-jre8:\$kotlin_version\"\n" +
            dependencies +
            "}\n" +
            "\n" +
            "sourceCompatibility = \"1.8\"\n" +
            "targetCompatibility = \"1.8\"\n" +
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
