
## Comparison between different build tools versions

Date - 7th July 2018
  
### Setup
  
Profiling is done with gradle-profiler. Profiling scenarios could be found [here](https://github.com/android/android-studio-poet/blob/master/configs/build-tool-versions/gradle-profiler.scenarios). 
Commands that were used to profile can be found [here](https://github.com/android/android-studio-poet/blob/master/configs/build-tool-versions/gradle-profiler-commands.txt).

Please note, that gradle-profiler was modified to support incremental Kotlin profiling.

### Variants
All variants have 2 modules with 2500 Kotlin classes and 2500 Java classes 10 methods each. All variant has 
`minSdkVersion=21`.
The only difference is version of Gradle(G), Android Gradle Plugin(AGP), Kotlin(K).
The following combinations were profiled:
  * G:4.3.1, AGP 3.0.1, Kotlin 1.1.60
  * G:4.5, AGP 3.0.1, Kotlin 1.2.20
  * G:4.6, AGP 3.1.0, Kotlin 1.2.30
  * G:4.6, AGP 3.1.0, Kotlin 1.2.41
  * G:4.6, AGP 3.1.2, Kotlin 1.2.41
  * G:4.7, AGP 3.1.2, Kotlin 1.2.41
  * G:4.8, AGP 3.1.2, Kotlin 1.2.41

### Summary of the results

|             | Clean build | Incremental build |
|-------------|-------------| ----------------- |
|G:4.3.1 AGP 3.0.1 Kotlin 1.1.60 | 1m 57s [buildscan](https://scans.gradle.com/s/bvdlmqauv4vfi) | 1m 22s [buildscan](https://scans.gradle.com/s/tsesvbvtmhqzi) |
|G:4.5, AGP 3.0.1, Kotlin 1.2.20 | 1m 17s [buildscan](https://scans.gradle.com/s/ek2vtboirtgpc) | 1m 19s [buildscan](https://scans.gradle.com/s/pghes2tsukg7s) |
|G:4.6, AGP 3.1.0, Kotlin 1.2.30 | 1m 31s [buildscan](https://scans.gradle.com/s/momm25atli64q) | 1m 1s [buildscan](https://scans.gradle.com/s/uxmkl46swvefu) |
|G:4.6, AGP 3.1.0, Kotlin 1.2.41 | 1m 31s [buildscan](https://scans.gradle.com/s/unrlpbrwzaa3g) | 53s [buildscan](https://scans.gradle.com/s/f2lxmcbwglth4) |
|G:4.6, AGP 3.1.2, Kotlin 1.2.41 | 1m 7s [buildscan](https://scans.gradle.com/s/h4gqgwb4al4mu)| 56s [buildscan](https://scans.gradle.com/s/a2ufhihk2mmpi) |
|G:4.7, AGP 3.1.2, Kotlin 1.2.41 | 1m 10s [buildscan](https://scans.gradle.com/s/ulf32prjxbkny)| 55s [buildscan](https://scans.gradle.com/s/zvbhbskzpd6be) |
|G:4.8, AGP 3.1.2, Kotlin 1.2.41 | 1m 12s [buildscan](https://scans.gradle.com/s/2jbjvjhapt7pa)| 1m 11s [buildscan](https://scans.gradle.com/s/nmrtqbgq3yya6) |
  
Notes:
  * Time for incremental build is constantly decrease for all the versions, but Gradle 4.8.
  * Clean build for AGP 3.1.2 is significantly faster then AGP 3.1.0.  