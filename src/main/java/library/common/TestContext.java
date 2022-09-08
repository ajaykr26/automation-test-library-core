package library.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.SoftAssertions;

import java.nio.file.Files;
import java.util.*;

public class TestContext {

    private static final List<TestContext> threads = new ArrayList<>();
    protected final Logger logger = LogManager.getLogger(this.getClass().getName());
    private Map<String, Object> testdata = null;
    private Map<String, Object> propdata = null;
    private SoftAssertions softAssert = null;
    private Set<Class<?>> setOfPO = null;
    private Set<Files> setOfFF = null;
    private Set<String> setOfAPI = null;
    private Set<String> setOfDB = null;
    private Deque<String> windowHandles;
    private String activeWindowType;
    private final long threadToEnvID;

    private TestContext(long threadID) {
        this.threadToEnvID = threadID;
    }

    public static synchronized TestContext getInstance() {
        long currentRunningThreadID = Thread.currentThread().getId();
        for (TestContext thread : threads) {
            if (thread.threadToEnvID == currentRunningThreadID) {
                return thread;
            }
        }
        TestContext temp = new TestContext(currentRunningThreadID);
        threads.add(temp);
        return temp;
    }

    public SoftAssertions softAssertions() {
        if (softAssert == null)
            softAssert = new SoftAssertions();
        return softAssert;
    }

    public void resetSoftAssert() {
        softAssert = new SoftAssertions();
    }

    public Map<String, Object> testdata() {
        if (testdata == null)
            testdata = new HashMap<>();
        return testdata;
    }

    public void resetTestdata() {
        testdata = new HashMap<>();
    }

    public Map<String, Object> propData() {
        if (propdata == null)
            propdata = new HashMap<>();
        return propdata;
    }

    public void resetPropData() {
        propdata = new HashMap<>();
    }

    public void testdataPut(String key, Object value) {
        testdata.put(key, value);
    }

    public void propDataPut(String key, Object value) {
        propdata.put(key, value);
    }

    public <T> T testdataToClass(String key, Class<T> type) {
        return type.cast(testdata.get(key));
    }

    public Object testdataGet(String key) {
        if (testdata.get(key) != null) {
            return testdata.get(key);
        } else if (testdata.get(key.toLowerCase()) != null) {
            logger.warn("exact key not found for the key '{}' please check the key name", key);
            return testdata.get(key.toLowerCase());
        }
        return null;
    }

    public Object propDataGet(String key) {
        if (propdata.get(key) != null) {
            return propdata.get(key);
        } else if (propdata.get(key.toLowerCase()) != null) {
            logger.warn("exact key not found for the key '{}' please check the key name", key);
            return propdata.get(key.toLowerCase());
        }
        return null;
    }

    public Set<Class<?>> setOfPO() {
        if (setOfPO == null)
            setOfPO = new HashSet<>();
        return setOfPO;
    }

    public Set<Files> setOfFF() {
        if (setOfFF == null)
            setOfFF = new HashSet<>();
        return setOfFF;
    }

    public Set<String> setOfAPI() {
        if (setOfAPI == null)
            setOfAPI = new HashSet<>();
        return setOfAPI;
    }

    public Set<String> setOfDB() {
        if (setOfDB == null)
            setOfDB = new HashSet<>();
        return setOfDB;
    }

    public Deque<String> windowHandles() {
        if (windowHandles == null)
            windowHandles = new ArrayDeque<>();
        return windowHandles;
    }

    public void pushWindowHandles(String windowHandleName) {
        windowHandles().addLast(windowHandleName);
    }

    public String popWindowHandle() {
        return windowHandles().removeLast();
    }

    public Integer getWindowHandlesCount() {
        return windowHandles().size();
    }

    public Boolean windowHandleExist(String windowHandleName) {
        return windowHandles().contains(windowHandleName);
    }

    public String peekLastWindowHandle() {
        return windowHandles().peekLast();
    }

    public void setActiveWindowType(String activeWindowType) {
        this.activeWindowType = activeWindowType;
    }

    public String getActiveWindowType() {
        return activeWindowType;
    }
}

