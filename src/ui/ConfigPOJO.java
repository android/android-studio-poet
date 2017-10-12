package ui;

import com.google.gson.Gson;

public class ConfigPOJO {

    // directory where generator should put all generated packages
    private String root;

    // how many method should be generated all together (!!!)
    private String allMethods;

    // how many java methods should be generated all together
    private String javaMethodCount;

    // how many java packages should be generated
    private String javaPackageCount;

    // how many kotlin packages should be generated
    private String kotlinPackageCount;

    // how many classes should be generated in each Java package
    private String javaClassCount;

    // how many classes should be generated in each Kotlin package
    private String kotlinClassCount;

    public String getJavaMethodCount() {
        return javaMethodCount;
    }

    public void setJavaMethodCount(String javaMethodCount) {
        this.javaMethodCount = javaMethodCount;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getAllMethods() {
        return allMethods;
    }

    public void setAllMethods(String allMethods) {
        this.allMethods = allMethods;
    }

    public String getKotlinPackageCount() {
        return kotlinPackageCount;
    }

    public void setKotlinPackageCount(String kotlinPackageCount) {
        this.kotlinPackageCount = kotlinPackageCount;
    }

    public String getJavaClassCount() {
        return javaClassCount;
    }

    public void setJavaClassCount(String javaClassCount) {
        this.javaClassCount = javaClassCount;
    }

    public String getKotlinClassCount() {
        return kotlinClassCount;
    }

    public void setKotlinClassCount(String kotlinClassCount) {
        this.kotlinClassCount = kotlinClassCount;
    }

    public String getJavaPackageCount() {
        return javaPackageCount;
    }

    public void setJavaPackageCount(String javaPackageCount) {
        this.javaPackageCount = javaPackageCount;
    }

    @Override
    public String toString() {
        return "ClassPojo [javaMethodCount = " + javaMethodCount +
                ", root = " + root + ", allMethods = " + allMethods +
                ", kotlinPackageCount = " + kotlinPackageCount + ", javaClassCount = " +
                javaClassCount + ", kotlinClassCount = " + kotlinClassCount +
                ", javaPackageCount = " + javaPackageCount + "]";
    }

    public boolean validate() {
        // TODO check the types of all members be strings
        return true;
    }

    public int getJavaMethodsPerClass() {
        // TODO call validate
        int javaMethodsPerClass = Integer.parseInt(javaMethodCount) / (Integer.parseInt(javaClassCount)
                * Integer.parseInt(javaPackageCount));

        return javaMethodsPerClass;
    }

    public int getAllKotlinMethods() {

        return Integer.parseInt(allMethods) - Integer.parseInt(javaMethodCount);
    }

    public int getKotlinMethodsPerClass() {
        return getAllKotlinMethods() /
                (Integer.parseInt(kotlinClassCount) * Integer.parseInt(kotlinPackageCount));
    }

    public String toJson() {
        Gson gson = new Gson();
        String json = gson.toJson(this);

        return json;
    }
}
