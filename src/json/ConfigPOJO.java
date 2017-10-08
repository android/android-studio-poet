package json;

public class ConfigPOJO {
    private String javaMethodCount;

    private String mainPackage;

    private String allMethods;

    private String kotlinPackageCount;

    private String javaClassCount;

    private String kotlinClassCount;

    private String javaPackageCount;

    public String getJavaMethodCount ()
    {
        return javaMethodCount;
    }

    public void setJavaMethodCount (String javaMethodCount)
    {
        this.javaMethodCount = javaMethodCount;
    }

    public String getMainPackage ()
    {
        return mainPackage;
    }

    public void setMainPackage (String mainPackage)
    {
        this.mainPackage = mainPackage;
    }

    public String getAllMethods ()
    {
        return allMethods;
    }

    public void setAllMethods (String allMethods)
    {
        this.allMethods = allMethods;
    }

    public String getKotlinPackageCount ()
    {
        return kotlinPackageCount;
    }

    public void setKotlinPackageCount (String kotlinPackageCount)
    {
        this.kotlinPackageCount = kotlinPackageCount;
    }

    public String getJavaClassCount ()
    {
        return javaClassCount;
    }

    public void setJavaClassCount (String javaClassCount)
    {
        this.javaClassCount = javaClassCount;
    }

    public String getKotlinClassCount ()
    {
        return kotlinClassCount;
    }

    public void setKotlinClassCount (String kotlinClassCount)
    {
        this.kotlinClassCount = kotlinClassCount;
    }

    public String getJavaPackageCount ()
    {
        return javaPackageCount;
    }

    public void setJavaPackageCount (String javaPackageCount)
    {
        this.javaPackageCount = javaPackageCount;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [javaMethodCount = "+javaMethodCount+", mainPackage = "+mainPackage+", allMethods = "+allMethods+", kotlinPackageCount = "+kotlinPackageCount+", javaClassCount = "+javaClassCount+", kotlinClassCount = "+kotlinClassCount+", javaPackageCount = "+javaPackageCount+"]";
    }





}
