# java-generator

Following great post <LINK>, referenced with author's permission
  
![alt text](https://github.com/borisf/java-generator/blob/master/img/generator.png)  
  
A tool to generate big Java and Kotlin based modules for analyzing big Android apps, and looking for ways to improve build times.

## Download & Run
To run, grab the [latest JAR](https://github.com/borisf/classyshark-bytecode-viewer/releases)
and run `java -jar ClassySharkBytecodeViewer.jar`. Optionally you can add a class file to open.

## Why
Instantaneously understand and assess any Kotlin code.
## How
The most accurate and measurable way is to look at Kotlin generated executable (.class) files, the same files that both JVM and Android DX tool see. 

## What
From every kotlin-compiler generated class file you will see 3 tabs:

1. Equivalent Java code
2. Equivalent Java bytecode
3. Hex view
 
The (mind) flow will be as follows:
1. From Kotlin code to Java code
2. From Java code to Java bytecode (class format)
3. From Java bytecode to hex view (raw binary format)
 
Here is the tricky part, instead of doing source to source translation from Kotlin to Java, it is 
better (faster and accurate) to decompile Kotlin generated class file right into Java.
 
To support the above the we use the following 2 libraries:
* [Procyon](https://bitbucket.org/mstrobel/procyon/wiki/Java%20Decompiler)- an open source Java decompiler.
* [ASM](http://asm.ow2.org/) - the best Java bytecode reading library (used both by Kotlin and Android Studio).



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
