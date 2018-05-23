
## KAPT Reseacrh with Java/Koltin mix


* Date - 2018 - 5 - 21
* Tools Version
  * Gradle version - 4.7
  * Android Gradle Plugin (AGP) version - 3.1.2
  * KotlinVersion - 1.2.41

#### Profiling
The profiling was done with gradle-profiler. Each configuration, except `KotlinOnly`, 
was profiled 4 times. The `KotlinOnly` configuration was profiled twice, because 
the gradle-profiler does not support incremental build profiling for Kotlin.


#####Steps
1. Clean build with build scan profiler
2. Incremental build with build scan profiler
3. Clean build with `--benchmark` option (second for “KotlinOnly" configuration)
4. Incremental build with `--benchmark` option. 

* [Profiler scenarios](https://github.com/android/android-studio-poet/blob/master/configs/kapt/v2/gradle-profiler.scenarios) 
for clean and incremental builds. 
* [AS Poet configs](https://github.com/android/android-studio-poet/tree/master/configs/kapt/v2)
   * All projects have data binding, which means enabled annotation processing.
   * Each module has 200 classes with 10 methods each. Depending on configs 
   it is 100% Java, 100% Kotlin or 50-50 Java Kotlin mix.
   * The tested "variable" is a mix of Java and Kotlin code
* Branch with 
      [results](https://github.com/NikitaKozlov/android-studio-poet/tree/kapt-problem-v2-with-results/generated_projects).

####  Summary of the results
* Only Java code
   * Clean: 2m 23s ± 19s (Build scan: https://scans.gradle.com/s/tixxvw4gbpmmi)
   * Incremental: 30s ± 3s (Build scan: https://scans.gradle.com/s/icieznrsm3u6k)
* Mixed Java and Kotlin code
   * Clean: 2m 11s ± 16s  (Build scan: https://scans.gradle.com/s/42gr5zacv5uxc)
   * Incremental: 26s ± 3s (Build scan: https://scans.gradle.com/s/sx3jz5iwzkq6c)
* Only Kotlin code
   * Clean: 1m 44s ± 3s (Build scan: https://scans.gradle.com/s/qg4wpgnu774y4)
