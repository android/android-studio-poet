# Android Studio Poet

[![Build Status](https://travis-ci.org/android/android-studio-poet.svg?branch=master)](https://travis-ci.org/android/android-studio-poet)

![alt text](https://github.com/borisf/java-generator/blob/master/img/generator.png)  
  
Inspired by [GradleBuildExperiment](https://github.com/NikitaKozlov/GradleBuildExperiment). A tool to generate a real life Java and Kotlin Android project to analyze and improve build times.

Create a real life complexity Android project that mimics your own and observe the build times. If the build times of the generated project are way smaller than yours, it might be worth inspecting deeply your build configs and optimize.

## Feautures

* Configurable number of modules
* Configurable number of packages
* Configurable number of classes
* Configurable number of inter module dependencies
* Android resources (images, strings,activities, layouts)
* Configurable version of gradle, kotlin, the android gradle plugin

## Build & Run
1. Clone the repo
2. `./gradlew fatJar`
3. `java -jar build/libs/android-studio-poet-all-1.0-SNAPSHOT.jar`

## How
* The generated sources will include the basic functionality of method calling both in class itself and between the generated classes, both in Java and Kotlin

* The generated build.gradle files will include other modules as dependencies

* You can control Android resources (images, layouts etc')

* You can control product flavors

* You can have both source and resources inter-module dependencies

This is not an official Google product.

### License

```
Copyright 2017 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License
is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
or implied. See the License for the specific language governing permissions and limitations under
the License.
```
