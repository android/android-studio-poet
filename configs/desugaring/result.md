## If lambdas slow build down in Java? Kotlin?


Date - 7th July 2018
  
### Setup
  
Profiling is done with gradle-profiler. Profiling scenarios could be found [here](https://github.com/android/android-studio-poet/blob/master/configs/desugaring/gradle-profiler.scenarios).
Commands that were used to profile can be found [here](https://github.com/android/android-studio-poet/blob/master/configs/desugaring/gradle-profiler-commands.txt).

Please note, that gradle-profiler was modified to support incremental Kotlin profiling.

### Variants
All variants use the following tools versions:
  * Gradle version - 4.8
  * Android Gradle Plugin (AGP) version - 3.1.2
  * KotlinVersion - 1.2.41
  * Java target and source compatibility - 1.8
  
For each of the following variants two configs are created one with `minSdkVersion=19` and second one with `minSdkVersion=21`
  * Java with Lambdas. 10 000 classes with 10 methods and 1 lambda
  * Kotlin with Lambdas. 10 000 classes with 10 methods and 1 lambda
  * Java without Lambdas. 20 000 classes with 6 methods. This variant is suppose to represent case when user writes classes instead of using lambdas. Because each lambda is transformed into a separate class the amount of classes is doubled, but because lambda has less methods amount of methods per class is reduced.
  * Java without Lambdas with reduced amount of classes. 10 000 classes with 10 methods. This variant is added to check how big overhead lambdas bring.  

### Summary of the results


|             | Clean build | Incremental build |
|-------------|-------------| ----------------- |
|Java with Lambdas | 2m 50s [buildscan](https://scans.gradle.com/s/7mpfqw3jve6xw)| 1m 2s [buildscan](https://scans.gradle.com/s/bugatuedxthpi)|
|Java with Lambdas, min SDK 21 | 2m 46s [buildscan](https://scans.gradle.com/s/o65u2svlmgcmu)| 38s [buildscan](https://scans.gradle.com/s/57iaid27hk3sm) |
|Kotlin with Lambdas | 4m 7s [buildscan](https://scans.gradle.com/s/awoh2u5cvttm4) | 1m 26s [buildscan](https://scans.gradle.com/s/2g5424pq27vre) |
|Kotlin with Lambdas, min SDK 21 | 3m 53s [buildscan](https://scans.gradle.com/s/umukzli3std5c) | 35s [buildscan](https://scans.gradle.com/s/3qgzlu5hkt4gm) |
|Java without Lambdas | 2m 19s [buildscan](https://scans.gradle.com/s/xn5qduoedgwmg) | 1m 36s [buildscan](https://scans.gradle.com/s/pjig5ehivuufq) |
|Java without Lambdas, min SDK 21 | 2m 28s [buildscan](https://scans.gradle.com/s/3azcgfe43vd2u) | 1m 15s [buildscan](https://scans.gradle.com/s/wnjlm7jitv2ni) |
|Java without Lambdas with reduced amount of classes | 1m 7s [buildscan](https://scans.gradle.com/s/lizettzqhc4lu) | 43s [buildscan](https://gradle.com/s/qxjmitf5emo66) |
|Java without Lambdas with reduced amount of classes, min SDK 21 | 1m 5s [buildscan](https://scans.gradle.com/s/wrqpuqjti2ljm) | 36s [buildscan](https://scans.gradle.com/s/nntf735budeqs) |

Notes:
  * Incremental build with min SDK 21 is faster for all variants
  * Clean build for Kotlin variant is more then 50% longer then Java. But incremental build with min SDK 21 is the fastest.
  * Incremental build for "Java without Lambdas" variant took surprisingly long, but the clean build is faster then any variant with lambdas.  
  
  