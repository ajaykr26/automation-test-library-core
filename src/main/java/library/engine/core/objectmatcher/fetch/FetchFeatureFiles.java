package library.engine.core.objectmatcher.fetch;

import library.common.Constants;
import library.engine.core.AutoEngCoreConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FetchFeatureFiles {
    private FetchFeatureFiles() {
    }

    private static final Logger logger = LogManager.getLogger(FetchFeatureFiles.class.getName());

    public static Set<File> populateListOfFeatureFiles() {
        return recursiveListOfFiles(new File(Constants.FEATURE_PATH), new HashSet<>());
    }

    private static Set<File> recursiveListOfFiles(File directory, Set<File> listFiles) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    listFiles.addAll(recursiveListOfFiles(file, listFiles));
                } else {
                    listFiles.add(file);
                }
            }
            return listFiles;
        } else {
            return Collections.emptySet();
        }
    }
}
