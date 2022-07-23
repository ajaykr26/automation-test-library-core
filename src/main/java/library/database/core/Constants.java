package library.database.core;

import java.io.File;

public class Constants {

    private Constants() {
    }

    public static final String USERDIR = System.getProperty("user.dir");
    public static final String BASEPATH = USERDIR + "/src/test/resources/";
    public static final String CONFIGDB = "config/db/";
    public static final String ENVIRONMENTPATH = BASEPATH + "config/environments";
    public static final String GENERATEDCLASSPATH = USERDIR + "target/test-classes/";
    public static final String DBOBJECTFOLDER = "dbobjects";
    public static final String DBOBJECTPATH = GENERATEDCLASSPATH + DBOBJECTFOLDER + File.separator;
    public static final String DBRUNTIMEPATH = BASEPATH + CONFIGDB + "runtime.properties";
    public static final String DBQUERYPATH = BASEPATH + CONFIGDB + "query.properties";
    public static final String DBQUERYFILEPATH = USERDIR + "/lib/sqlfiles/";

}
