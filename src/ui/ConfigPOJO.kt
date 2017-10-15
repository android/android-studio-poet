package ui

import com.google.gson.Gson

class ConfigPOJO {

    // directory where generator should put all generated packages
    var root: String? = null

    // how many modules
    var numModules: String? = null

    // how many method should be generated all together (!!!)
    var allMethods: String? = null

    // how many java methods should be generated all together
    var javaMethodCount: String? = null

    // how many java packages should be generated
    var javaPackageCount: String? = null

    // how many kotlin packages should be generated
    var kotlinPackageCount: String? = null

    // how many classes should be generated in each Java package
    var javaClassCount: String? = null

    // how many classes should be generated in each Kotlin package
    var kotlinClassCount: String? = null

    // TODO call validate
    val javaMethodsPerClass: Int
        get() = Integer.parseInt(javaMethodCount!!) / (Integer.parseInt(javaClassCount!!) * Integer.parseInt(javaPackageCount!!))

    private val allKotlinMethods: Int
        get() = Integer.parseInt(allMethods!!) - Integer.parseInt(javaMethodCount!!)

    val kotlinMethodsPerClass: Int
        get() = allKotlinMethods / (Integer.parseInt(kotlinClassCount!!) * Integer.parseInt(kotlinPackageCount!!))

    override fun toString(): String {
        return ("ClassPojo [javaMethodCount = " + javaMethodCount +
                ", root = " + root + ", numModules = " + numModules
                + ", allMethods = " + allMethods +
                ", kotlinPackageCount = " + kotlinPackageCount + ", javaClassCount = " +
                javaClassCount + ", kotlinClassCount = " + kotlinClassCount +
                ", javaPackageCount = " + javaPackageCount + "]")
    }

    fun validate(): Boolean {
        // TODO check the types of all members be strings
        return true
    }

    fun toJson(): String {
        val gson = Gson()

        return gson.toJson(this)
    }
}
