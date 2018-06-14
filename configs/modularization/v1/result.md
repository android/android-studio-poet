
## If lambdas slow build down in Java? Kotlin?

Date - 13th June 2018
  
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
  * Min SDK - 21


In total there are 10 variant. Each config has 20k files, and they are split equally among 1-10 modules, all 
library modules are independent from one another 
Each variant is profiled in 4 scenarios:
  * Clean build
  * Incremental build in app module(App Incremental)
  * Incremental build of the app module with change in library module(Lib Incremental)
  * Incremental build of the library module (Lib only Incremental)
  
All builds are repeated on 2 machines. 

1. MacBook Pro (13-inch, Early 2015) with 2.7 GHz Intel Core i5 and 16 GB 1867 MHz DDR3
2. MacBook Pro (15-inch, 2017) with 2,8 GHz Intel Core i7 and 16 GB 2133 MHz LPDDR3
    
### Summary of the results

MBP 13'

|            | Clean build | App Incremental  | Lib Incremental | Lib only Incremental |
| ---------- |-------------| ---------------- | --------------- | -------------------- |
| 1 Module   | 3m 43s [buildscan](https://scans.gradle.com/s/nb4ibhyli7qze)| 55s [buildscan](https://scans.gradle.com/s/ic44xdddhh3ku)| --- | --- |
| 2 Modules  | 2m 36s [buildscan](https://scans.gradle.com/s/tvykblvnethb4)| 39s [buildscan](https://scans.gradle.com/s/2cr2pl6xxn6te)| 2m 23s [buildscan](https://scans.gradle.com/s/hu2w5o4ai4sys)| 26s [buildscan](https://scans.gradle.com/s/33w5ad4qqizke)|
| 3 Modules  | 1m 55s [buildscan](https://scans.gradle.com/s/5646m5ry5cv7e)| 22s [buildscan](https://scans.gradle.com/s/i3eqfqxhgvqii)| 1m 40s [buildscan](https://scans.gradle.com/s/qcw3epo2n4vxy)| 18s [buildscan](https://scans.gradle.com/s/esufq6uwtib7c)|
| 4 Modules  | 1m 50s [buildscan](https://scans.gradle.com/s/uv5uhb3q7hxc2)| 19s [buildscan](https://scans.gradle.com/s/v4eqakhwoziou)| 1m 8s [buildscan](https://scans.gradle.com/s/fdymjefbi2zgo)| 12s [buildscan](https://scans.gradle.com/s/spv3qeinjfzig)|
| 5 Modules  | 1m 33s [buildscan](https://scans.gradle.com/s/7xsyxde2qqcn2)| 17s [buildscan](https://scans.gradle.com/s/dvxng553jhx5c)| 56s [buildscan](https://scans.gradle.com/s/gpeoybladrw7s)| 10s [buildscan](https://scans.gradle.com/s/g5p75p2dqw7x2)|
| 6 Modules  | 1m 30s [buildscan](https://scans.gradle.com/s/xa5fdrfgibgh4)| 16s [buildscan](https://scans.gradle.com/s/ljm5puyfyt25q)| 51s [buildscan](https://scans.gradle.com/s/r5pqyknyxuhz2)| 8s [buildscan](https://scans.gradle.com/s/arxzqbyeyai6m)|
| 7 Modules  | 1m 20s [buildscan](https://scans.gradle.com/s/lwxuixoioakae)| 14s [buildscan](https://scans.gradle.com/s/i2jqe7lt2a564)| 40s [buildscan](https://scans.gradle.com/s/iq6nr2kgxw5rq)| 7s [buildscan](https://scans.gradle.com/s/cu7yo7ucvci7a)|
| 8 Modules  | 1m 25s [buildscan](https://scans.gradle.com/s/mxpayjwvzuxja)| 12s [buildscan](https://scans.gradle.com/s/ktxbsbgrji2rq)| 35s [buildscan](https://scans.gradle.com/s/gsltm45fju7by)| 6s [buildscan](https://scans.gradle.com/s/ovpcdodndaob6)|
| 9 Modules  | 1m 19s [buildscan](https://scans.gradle.com/s/os33rtnlj5ylm)| 12s [buildscan](https://scans.gradle.com/s/cdiyaho34xpgo)| 34s [buildscan](https://scans.gradle.com/s/dqejapfprne6g)| 5s [buildscan](https://scans.gradle.com/s/l73ogwjuiqkyg)|
| 10 Modules | 1m 25s [buildscan](https://scans.gradle.com/s/tsmp6tvfxhjwk)| 12s [buildscan](https://scans.gradle.com/s/xe64bvuh5dcxs)| 30s [buildscan](https://scans.gradle.com/s/fjcflst4t2xwu)| 4s [buildscan](https://scans.gradle.com/s/zrejsy6gsxkcs)|


MBP 15'

|            | Clean build | App Incremental  | Lib Incremental | Lib only Incremental |
| ---------- |-------------| ---------------- | --------------- | -------------------- |
| 1 Module   | 35s [buildscan](https://scans.gradle.com/s/eyrkmb7owzvhk)| 20s [buildscan](https://scans.gradle.com/s/tnpuqxgfbgsy2)| --- | --- |
| 2 Modules  | 26s [buildscan](https://scans.gradle.com/s/b7dp7ybmikobo)| 11s [buildscan](https://scans.gradle.com/s/ykxavwvl2cvue)| 50s [buildscan](https://scans.gradle.com/s/4u7qklj6n37d2)| 8s [buildscan](https://scans.gradle.com/s/f2bpobptrqiiw)|
| 3 Modules  | 23s [buildscan](https://scans.gradle.com/s/6orthpr6dssmu)| 9s [buildscan](https://scans.gradle.com/s/lingerngabwmc)| 34s [buildscan](https://scans.gradle.com/s/qyeyvm37bikza)| 5s [buildscan](https://scans.gradle.com/s/ykgtiijeg733m)|
| 4 Modules  | 21s [buildscan](https://scans.gradle.com/s/tc4herdtovfsi)| 7s [buildscan](https://scans.gradle.com/s/vc4asmmaxg6uo)| 29s [buildscan](https://scans.gradle.com/s/b5bdgm53mjpqo)| 5s [buildscan](https://scans.gradle.com/s/ibjbk5yqhrm2m)|
| 5 Modules  | 23s [buildscan](https://scans.gradle.com/s/eglgqveuvflmk)| 7s [buildscan](https://scans.gradle.com/s/txno2lv6iedz2)| 23s [buildscan](https://scans.gradle.com/s/3b5oj7tyhabgm)| 4s [buildscan](https://scans.gradle.com/s/il7sgcv4bqkxa)|
| 6 Modules  | 21s [buildscan](https://scans.gradle.com/s/pjcs2eehvgx5w)| 6s [buildscan](https://scans.gradle.com/s/zo6j2g6jrkh4i)| 19s [buildscan](https://scans.gradle.com/s/z2rc4xop2yjfc)| 3s [buildscan](https://scans.gradle.com/s/5puoxm22mckho)|
| 7 Modules  | 20s [buildscan](https://scans.gradle.com/s/4mpnlsd2o66q6)| 6s [buildscan](https://scans.gradle.com/s/jjwehjhkp7kzq)| 18s [buildscan](https://scans.gradle.com/s/zmmbmccdek7f6)| 3s [buildscan](https://scans.gradle.com/s/wrnlvlzdq732i)|
| 8 Modules  | 21s [buildscan](https://scans.gradle.com/s/wxpu2flgtczwu)| 6s [buildscan](https://scans.gradle.com/s/3meb5a4qxf556)| 17s [buildscan](https://scans.gradle.com/s/4m75bn35qf7ig)| 3s [buildscan](https://scans.gradle.com/s/qimeh53f3jjig)|
| 9 Modules  | 20s [buildscan](https://scans.gradle.com/s/5hpml4fz5p3fw)| 5s [buildscan](https://scans.gradle.com/s/tf7o3cnalumpm)| 16s [buildscan](https://scans.gradle.com/s/buutwrflj2at2)| 3s [buildscan](https://scans.gradle.com/s/h5oqq6ckhvicq)|
| 10 Modules | 20s [buildscan](https://scans.gradle.com/s/m4ujz745cu7mm)| 6s [buildscan](https://scans.gradle.com/s/qnln4ahqfocxw)| 15s [buildscan](https://scans.gradle.com/s/7jq4jxywsr5oo)| 3s [buildscan](https://scans.gradle.com/s/snu3qrqashzv4)|

Notes:
  * Apparently faster laptop might be a better improvement then modularization if one doesn't work against tests
  * The clean build data is invalid. Even though `clean` and `cleanBuildCache` are called before every repetition, 
  there is another cache that speeds up the build, because tasks like `compileKotlinDebug` are marked `FROM-CACHE` in the buildscan.
