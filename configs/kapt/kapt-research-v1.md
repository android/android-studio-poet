How kapt affects the performance
Legend:
	KotlinOnly - no java code
	NoKotlin - no kotlin code
	Kotlin - mix java and kotlin code
	DataBinding - data binding enabled
	NoDataBinding - data binding disabled

Kotlin_DataBinding:
 * Full: https://scans.gradle.com/s/a4gqljyjxzvqm (6m)
 * Incremental: https://scans.gradle.com/s/a4gqljyjxzvqm (4m 18s)
 
Kotlin_NoDataBinding:
 * Full: https://scans.gradle.com/s/kdzpjmiky5yze (3m 21s)
 * Incremental: https://gradle.com/s/5dl6qskwekapy (3m 9ss)
 
NoKotlin_DataBinding:
 * Full: https://scans.gradle.com/s/5evcrapewcyqm (7m 58s)
 * Incremental: https://scans.gradle.com/s/2q4uqgikwyfzq (5m 56s)
 
NoKotlin_NoDataBinding:
 * Full: https://scans.gradle.com/s/7hcmjgrqcir7a (7m 17s)
 * Incremental: https://scans.gradle.com/s/imcnynoavdgay (3m 51s)
 
KotlinOnly_DataBinding:
 * Full: https://scans.gradle.com/s/iwseu3574d3ga (4m 40s)
 * Incremental: https://scans.gradle.com/s/d72wi7k6p6tci (3m 59s)
 
KotlinOnly_NoDataBinding:
 * Full: https://scans.gradle.com/s/jbjjqpgsuv5ei (1m 51s)
 * Incremental: https://scans.gradle.com/s/lm4sivh5n5lbu (43s)


Hypothesyses:
 1. Kapt slows down the build significantly (KotlinOnly_DataBinding vs KotlinOnly_NoDataBinding)
 2. Annotaion Processing for Java slows down the build but less then kapt. (NoKotlin_DataBinding vs NoKotlin_NoDataBinding)
 3. Generated Java code is more complex to process then Kotlin, probably because of lambdas. This conclusion is made because desugaring task takes very long to complete.
