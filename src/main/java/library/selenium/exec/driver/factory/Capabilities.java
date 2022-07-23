package library.selenium.exec.driver.factory;


import library.common.Constants;
import library.common.Property;
import library.common.TestContext;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Capabilities {

    private final DesiredCapabilities dc;

    public Capabilities() {
        Map<String, String> map = DriverContext.getInstance().getTechStack();

        dc = new DesiredCapabilities();

        if (TestContext.getInstance().testdata().containsKey("fw.testDescription")) {
            dc.setCapability("name", TestContext.getInstance().testdataGet("fw.testDescription"));
        }
        if (TestContext.getInstance().testdata().containsKey("fw.projectName")) {
            dc.setCapability("project", TestContext.getInstance().testdataGet("fw.projectName"));
        }
        if (TestContext.getInstance().testdata().containsKey("fw.buildNumber")) {
            dc.setCapability("build", TestContext.getInstance().testdataGet("fw.buildNumber"));
        }

        for (Map.Entry<String, String> pair : map.entrySet()) {
            if (!pair.getKey().equalsIgnoreCase("serverName")) {
                dc.setCapability(pair.getKey(), pair.getValue());
            }
        }

        PropertiesConfiguration props = Property.getProperties(Constants.RUNTIME_PROP_FILE);
        if (Boolean.parseBoolean(props != null ? props.getString("useProxy") : null)) {
            Proxy proxy = new Proxy();
            if (props.getString("httpProxyURL") != null && props.getString("sslProxyURL") != null) {
                proxy.setHttpProxy(props.getString("httpProxyURL")).setSslProxy("sslProxyURL");
                dc.setCapability(CapabilityType.PROXY, proxy);
            } else {
                throw new IllegalArgumentException("seProxy set to true, but the value not provided for httpProxyURL or sslProxyURL in runtime.properties file");
            }
        }
    }

    public DesiredCapabilities getDesiredCapabilities() {
        return dc;
    }

}