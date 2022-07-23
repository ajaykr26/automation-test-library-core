package library.selenium.exec;

import library.selenium.core.PageObject;
import library.selenium.exec.driver.factory.DriverContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BasePO extends PageObject {

    protected Logger logger = LogManager.getLogger(this.getClass().getName());

    public BasePO() {
        super(DriverContext.getInstance().getDriver());
    }

}
