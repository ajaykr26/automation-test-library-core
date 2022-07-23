package library.engine.core.objectmatcher;

import info.debatty.java.stringsimilarity.JaroWinkler;
import library.common.CommonPageObject;
import library.common.TestContext;
import library.engine.api.AutoEngAPIBaseSteps;
import library.engine.core.objectmatcher.rating.FieldRating;
import library.engine.core.objectmatcher.rating.FlatFileObjectRating;
import library.engine.core.objectmatcher.rating.PageObjectRating;
import library.selenium.core.Element;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static library.common.StringHelper.removeSpecialChars;

public class ObjectFinder {
    private static final double ELEMENT_NOT_FOUND = -1.0;
    private static final double EXACT_THRESHOLD = 1;
    private static final double ABOVE_THRESHOLD = 0.85;
    private static final double BELOW_THRESHOLD = 0.7;
    private static final String WARNING_TEMPLATE = "{} {}";

    private ObjectFinder() {
    }

    protected static final Logger logger = LogManager.getLogger(ObjectFinder.class.getName());

    public static Element getMatchingElement(String objectName, String pageName) {
        PageObjectRating pageObjectMatch = null;
        FieldRating fieldMatch = null;
        CommonPageObject pageObject = null;
        Element element = null;

        pageObjectMatch = findMatchingPage(pageName);
        if (pageObjectMatch.getSimilarityScore() != ELEMENT_NOT_FOUND) {
            fieldMatch = findMatchingElement(objectName, pageObjectMatch.getPageObjectClazz());

            if (fieldMatch.getSimilarityScore() != ELEMENT_NOT_FOUND) {
                try {
                    if (pageObjectMatch.getPageObjectClazz().newInstance() instanceof CommonPageObject) {
                        pageObject = (CommonPageObject) pageObjectMatch.getPageObjectClazz().newInstance();
                    }
                    assert pageObject != null;
                    element = (Element) invokeMethod(pageObject, fieldMatch.getFieldName());
                } catch (InstantiationException | IllegalAccessException | NullPointerException exception) {
                    logger.error(exception.getMessage(), exception);
                }
            }
        }
        return element;
    }

    public static List<Element> getMatchingElements(String objectName, String pageName) {
        PageObjectRating pageObjectMatch = null;
        FieldRating fieldMatch = null;
        CommonPageObject pageObject = null;
        List<Element> elements = null;

        pageObjectMatch = findMatchingPage(pageName);
        if (pageObjectMatch.getSimilarityScore() != ELEMENT_NOT_FOUND) {
            fieldMatch = findMatchingElement(objectName, pageObjectMatch.getPageObjectClazz());

            if (fieldMatch.getSimilarityScore() != ELEMENT_NOT_FOUND) {
                try {
                    if (pageObjectMatch.getPageObjectClazz().newInstance() instanceof CommonPageObject) {
                        pageObject = (CommonPageObject) pageObjectMatch.getPageObjectClazz().newInstance();
                    }
                    assert pageObject != null;
                    elements = (List<Element>) invokeMethod(pageObject, fieldMatch.getFieldName());
                } catch (InstantiationException | IllegalAccessException | NullPointerException exception) {
                    logger.error(exception.getMessage(), exception);
                }
            }
        }
        return elements;
    }

    public static FieldRating findMatchingElement(String objectName, Class<?> pageObjectClazz) {
        List<FieldRating> listOfFilteredFields;
        FieldRating topMatch = null;

        final Field[] declaredField = pageObjectClazz.getDeclaredFields();
        final Method[] declaredMethods = pageObjectClazz.getDeclaredMethods();

        listOfFilteredFields = Arrays.stream(declaredField)
                .map(filed -> new FieldRating(filed.getName(),
                        getSimilarityScore(filed.getName(), removeSpecialChars(objectName))))
                .filter(filed -> filed.getSimilarityScore() >= BELOW_THRESHOLD)
                .sorted(Comparator.comparing(FieldRating::getSimilarityScore).reversed())
                .collect(Collectors.toList());
        if (!listOfFilteredFields.isEmpty()) {
            topMatch = listOfFilteredFields.get(0);
            if (topMatch.getSimilarityScore() >= EXACT_THRESHOLD) {
                topMatch.setSearchResultMessage(String.format("Element: found exact match for the element: \"%s\"", objectName));
                logger.debug(topMatch.getSearchResultMessage());
            } else if (topMatch.getSimilarityScore() >= ABOVE_THRESHOLD) {
                topMatch.setSearchResultMessage(String.format("Element: couldn't found exact element match for: \"%s\". using similar match \"%s\"", objectName, topMatch.getFieldName()));
                logger.debug(topMatch.getSearchResultMessage());
            } else if (topMatch.getSimilarityScore() >= BELOW_THRESHOLD) {
                String searchResultMessage = String.format("Element: couldn't found similar match for the : \"%s\" " +
                        "element in the provided page object class \"%s\"" +
                        "here are the few element similar to the provided element name: ", objectName, objectName, getPageObjectName(pageObjectClazz.toString()), System.lineSeparator());

                String fieldList = getSortedFieldListAsString(listOfFilteredFields.stream().map(FieldRating::getFieldName)
                        .collect(Collectors.toList()));

                topMatch.setSimilarityScore(ELEMENT_NOT_FOUND);
                topMatch.setSearchResultMessage(searchResultMessage);
                topMatch.setFiledList(fieldList);
                logger.warn(WARNING_TEMPLATE, searchResultMessage, fieldList);
            }
        } else {
            String searchResultMessage = String.format("Element: couldn't found similar match for the : \"%s\" " +
                    "element in the provided page object class \"%s\"" +
                    "here are the few element similar to the provided element name: ", objectName, objectName, getPageObjectName(pageObjectClazz.toString()), System.lineSeparator());

            String fieldList = getSortedFieldListAsString(listOfFilteredFields.stream().map(FieldRating::getFieldName)
                    .collect(Collectors.toList()));

            topMatch = new FieldRating(null, ELEMENT_NOT_FOUND, searchResultMessage, fieldList);
            logger.error(WARNING_TEMPLATE, searchResultMessage, fieldList);
        }
        return topMatch;
    }

    public static By getMatchingObject(String objectName, String pageName) {
        PageObjectRating pageObjectMatch = null;
        FieldRating fieldMatch = null;
        CommonPageObject pageObject = null;
        By object = null;

        pageObjectMatch = findMatchingPage(pageName);
        if (pageObjectMatch.getSimilarityScore() != ELEMENT_NOT_FOUND) {
            fieldMatch = findMatchingObject(objectName, pageObjectMatch.getPageObjectClazz());

            if (fieldMatch.getSimilarityScore() != ELEMENT_NOT_FOUND) {
                try {
                    if (pageObjectMatch.getPageObjectClazz().newInstance() instanceof CommonPageObject) {
                        pageObject = (CommonPageObject) pageObjectMatch.getPageObjectClazz().newInstance();
                    }
                    assert pageObject != null;
                    object = (By) invokeMethod(pageObject, fieldMatch.getFieldName());
                } catch (InstantiationException | IllegalAccessException | NullPointerException exception) {
                    logger.error(exception.getMessage(), exception);
                }
            }
        }
        return object;
    }

    public static FieldRating findMatchingObject(String objectName, Class<?> pageObjectClazz) {
        List<FieldRating> listOfFilteredFields;
        FieldRating topMatch = null;

        final Method[] declaredMethods = pageObjectClazz.getDeclaredMethods();

        listOfFilteredFields = Arrays.stream(declaredMethods)
                .map(filed -> new FieldRating(filed.getName(),
                        getSimilarityScore(filed.getName(), removeSpecialChars(objectName))))
                .filter(filed -> filed.getSimilarityScore() >= BELOW_THRESHOLD)
                .sorted(Comparator.comparing(FieldRating::getSimilarityScore).reversed())
                .collect(Collectors.toList());
        if (!listOfFilteredFields.isEmpty()) {
            topMatch = listOfFilteredFields.get(0);
            if (topMatch.getSimilarityScore() >= EXACT_THRESHOLD) {
                topMatch.setSearchResultMessage(String.format("Element: found exact match for the element: \"%s\"", objectName));
                logger.debug(topMatch.getSearchResultMessage());
            } else if (topMatch.getSimilarityScore() >= ABOVE_THRESHOLD) {
                topMatch.setSearchResultMessage(String.format("Element: couldn't found exact element match for: \"%s\". using similar match \"%s\"", objectName, topMatch.getFieldName()));
                logger.debug(topMatch.getSearchResultMessage());
            } else if (topMatch.getSimilarityScore() >= BELOW_THRESHOLD) {
                String searchResultMessage = String.format("Element: couldn't found similar match for the : \"%s\" " +
                        "element in the provided page object class \"%s\"" +
                        "here are the few element similar to the provided element name: ", objectName, objectName, getPageObjectName(pageObjectClazz.toString()), System.lineSeparator());

                String fieldList = getSortedFieldListAsString(listOfFilteredFields.stream().map(FieldRating::getFieldName)
                        .collect(Collectors.toList()));

                topMatch.setSimilarityScore(ELEMENT_NOT_FOUND);
                topMatch.setSearchResultMessage(searchResultMessage);
                topMatch.setFiledList(fieldList);
                logger.warn(WARNING_TEMPLATE, searchResultMessage, fieldList);
            }
        } else {
            String searchResultMessage = String.format("Element: couldn't found similar match for the : \"%s\" " +
                    "element in the provided page object class \"%s\"" +
                    "here are the few element similar to the provided element name: ", objectName, objectName, getPageObjectName(pageObjectClazz.toString()), System.lineSeparator());

            String fieldList = getSortedFieldListAsString(listOfFilteredFields.stream().map(FieldRating::getFieldName)
                    .collect(Collectors.toList()));

            topMatch = new FieldRating(null, ELEMENT_NOT_FOUND, searchResultMessage, fieldList);
            logger.error(WARNING_TEMPLATE, searchResultMessage, fieldList);
        }
        return topMatch;
    }

    private static String getPageObjectName(String pageObjectFullName) {
        List<String> name = Arrays.asList(pageObjectFullName.split("\\."));
        if (!name.isEmpty()) {
            return name.get(name.size() - 1);

        } else {
            return null;
        }
    }

    private static String getSortedFieldListAsString(List<String> sortedFiledList) {
        StringBuilder fieldList = new StringBuilder();
        sortedFiledList.sort(String.CASE_INSENSITIVE_ORDER);
        sortedFiledList.forEach(field -> {
            fieldList.append(field);
            fieldList.append(System.lineSeparator());
        });
        return fieldList.toString();
    }

    public static PageObjectRating findMatchingPage(String pageName) {
        Set<Class<?>> pageObjects = TestContext.getInstance().setOfPO();
        List<PageObjectRating> listOfFilteredPO;
        PageObjectRating topMatch = null;

        listOfFilteredPO = pageObjects.stream()
                .map(clazz -> new PageObjectRating(getClassName(clazz),
                        getSimilarityScore(getClassName(clazz), removeSpecialChars(pageName)), clazz))
                .filter(po -> po.getSimilarityScore() >= BELOW_THRESHOLD)
                .sorted(Comparator.comparing(PageObjectRating::getSimilarityScore).reversed())
                .collect(Collectors.toList());
        if (!listOfFilteredPO.isEmpty()) {
            topMatch = listOfFilteredPO.get(0);
            if (topMatch.getSimilarityScore() >= EXACT_THRESHOLD) {
                topMatch.setSearchResultMessage(String.format("Page Object: found exact match on the page: \"%s\"", topMatch.getPageObjectName()));
                logger.debug(topMatch.getSearchResultMessage());
            } else if (topMatch.getSimilarityScore() >= ABOVE_THRESHOLD) {
                topMatch.setSearchResultMessage(String.format("Page Object: could not found exact match for: \"%s\". using similar match \"%s\"", pageName, topMatch.getPageObjectName()));
                logger.debug(topMatch.getSearchResultMessage());
            } else if (topMatch.getSimilarityScore() >= BELOW_THRESHOLD) {
                String searchResultMessage = String.format("Page Object: could not found similar match for: \"%s\". potential pages for a similar match are: ", pageName);
                StringBuilder pageObjectList = new StringBuilder();
                listOfFilteredPO.forEach(po -> {
                    pageObjectList.append(po.getPageObjectName());
                    pageObjectList.append(System.lineSeparator());
                });
                topMatch.setSimilarityScore(ELEMENT_NOT_FOUND);
                topMatch.setSearchResultMessage(searchResultMessage);
                topMatch.setPageObjectList(pageObjectList.toString());
                logger.warn(WARNING_TEMPLATE, searchResultMessage, pageObjectList);
            }
        } else {
            String searchResultMessage = "Page Object: matching page name of \"" + pageName + "\" not found in available list of page objects. available page objests: " + System.lineSeparator();
            StringBuilder pageObjList = new StringBuilder();
            pageObjects.forEach(po -> {
                pageObjList.append(po.getName());
                pageObjList.append(System.lineSeparator());
            });
            topMatch = new PageObjectRating(null, ELEMENT_NOT_FOUND, null, searchResultMessage, pageObjList.toString());
            logger.error(WARNING_TEMPLATE, searchResultMessage, pageObjList);
        }
        return topMatch;
    }

    private static String getClassName(Class<?> clazz) {
        return clazz.getName().substring(clazz.getName().lastIndexOf('.') + 1);
    }

    private static double getSimilarityScore(String str1, String str2) {
        JaroWinkler jw = new JaroWinkler();
        return jw.similarity(str1.toLowerCase(), str2.toLowerCase());
    }

    private static Object invokeMethod(CommonPageObject pageObject, String objectName) {
        Object obj = null;
        try {
            Method method = pageObject.getClass().getMethod(objectName);
            obj = method.invoke(pageObject);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            logger.error(exception.getMessage(), exception);
        }
        return obj;
    }

    public static FlatFileObjectRating getMatchingAPIFeature(String featureName) {
        Set<String> apiObjects = TestContext.getInstance().setOfAPI();
        return getMatchingFlatFile(apiObjects, featureName, "API");
    }

    public static FlatFileObjectRating getMatchingDBQuery(String queryName) {
        Set<String> dbObjects = TestContext.getInstance().setOfDB();
        return getMatchingFlatFile(dbObjects, queryName, "query");
    }

    private static FlatFileObjectRating getMatchingFlatFile(Set<String> flatFileObject, String objectName, String objectType) {
        List<FlatFileObjectRating> listOfFilteredFiles;
        FlatFileObjectRating topMatch = null;

        listOfFilteredFiles = flatFileObject.stream()
                .map(flatFileName -> new FlatFileObjectRating(flatFileName,
                        getSimilarityScore(flatFileName, objectName)))
                .filter(fName -> getSimilarityScore(fName.getFlatFileObjectName(), objectName) >= BELOW_THRESHOLD)
                .sorted(Comparator.comparing(FlatFileObjectRating::getSimilarityScore).reversed())
                .collect(Collectors.toList());
        if (!listOfFilteredFiles.isEmpty()) {
            topMatch = listOfFilteredFiles.get(0);
            if (topMatch.getSimilarityScore() >= EXACT_THRESHOLD) {
                topMatch.setSearchResultMessage(String.format("%s Object: found exact match: \"%s\"", objectType, topMatch.getFlatFileObjectName()));
                logger.debug(topMatch.getSearchResultMessage());
            } else if (topMatch.getSimilarityScore() >= ABOVE_THRESHOLD) {
                topMatch.setSearchResultMessage(String.format("%s Object: could not found exact match for: \"%s\". using similar match \"%s\"", objectType, objectName, topMatch.getFlatFileObjectName()));
                logger.debug(topMatch.getSearchResultMessage());
            } else if (topMatch.getSimilarityScore() >= BELOW_THRESHOLD) {
                String searchResultMessage = String.format("%s Object: could not found similar match for: \"%s\". potential %s object for a similar match are: ", objectType, objectName, topMatch.getFlatFileObjectName());
                StringBuilder apiObjectList = new StringBuilder();
                listOfFilteredFiles.forEach(p -> {
                    apiObjectList.append(p.getFlatFileObjectName());
                    apiObjectList.append(System.lineSeparator());
                });
                topMatch.setSimilarityScore(ELEMENT_NOT_FOUND);
                topMatch.setSearchResultMessage(searchResultMessage);
                topMatch.setFlatFileObjectList(apiObjectList.toString());
                logger.warn(WARNING_TEMPLATE, searchResultMessage, apiObjectList);
            }
        } else {
            String searchResultMessage = String.format("%s Object: matching page name of \"%s\" not found in available list of %s objects. available %s objects: ", objectType, objectName, objectType, objectType, System.lineSeparator());
            StringBuilder apiObjectList = new StringBuilder();
            flatFileObject.forEach(p -> {
                apiObjectList.append(p);
                apiObjectList.append(System.lineSeparator());
            });
            topMatch = new FlatFileObjectRating(null, ELEMENT_NOT_FOUND, searchResultMessage, apiObjectList.toString());
            logger.error(WARNING_TEMPLATE, searchResultMessage, apiObjectList);
        }
        return topMatch;
    }

    public static void invokeMethod(String methodName, String pageName) throws IllegalAccessException, InstantiationException {
        PageObjectRating matchingPage = ObjectFinder.findMatchingPage(pageName);
        CommonPageObject commonPageObject;
        AutoEngAPIBaseSteps baseAPISteps;
        if (matchingPage.getPageObjectClazz().newInstance() instanceof CommonPageObject) {
            commonPageObject = (CommonPageObject) matchingPage.getPageObjectClazz().newInstance();
            invokeMethod(commonPageObject, methodName);
        } else if (matchingPage.getPageObjectClazz().newInstance() instanceof AutoEngAPIBaseSteps) {
            baseAPISteps = (AutoEngAPIBaseSteps) matchingPage.getPageObjectClazz().newInstance();
            invokeMethod(baseAPISteps, methodName);
        }
    }

    private static void invokeMethod(AutoEngAPIBaseSteps baseAPISteps, String objectName) {
        Object obj = null;
        try {
            Method method = baseAPISteps.getClass().getMethod(objectName);
            obj = method.invoke(baseAPISteps);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            logger.error(exception.getMessage(), exception);
        }
    }
}
