# automation-framework-bdd
This is a sample project to demonstrate how to work with Selenium and cucumber for Java.
It includes:
1. web automation
2. mobile automation
3. pdf automation
4. api automation

## technology 
1. java 8 and above.
2. maven
3. testng
4. selenium
5. Appium
6. karate
7. allure reporting
8. extent reporting

## Features:
1. cross-browser testing like Chrome-MicrosoftEdge, Chrome-Firefox
1. cross-platform testing like API-Web, API-Mobile, Web-Mobile
2. save report for each run
3. attach screenshot in report for every step
5. save screenshot and add to word doc
6. credential encryption using bat file
7. can be run from bat file without IDE
8. separate log for each run
9. anyone can create feature file no technical skill required.

## Data Setup
1. JSON file name should be same as the Feature Name.
2. data set name should be same as Scenario name in the JSON file. 
3. if you are using excel sheet for test data, worksheet name should be same as the Feature Name.
4. data set name should be same as Scenario name.

## Initial setups
2. application url should be mentioned in *Env*.properties file kept in the resources/config/environments folder.
3. user details should be mentioned in *Env*-SecureText.properties file kept in the resources/config/environments folder.

## vm arguments
-ea
-Dcukes.environment=UAT
-Dcukes.techstack=LOCAL_CHROME
-Dorg.apache.logging.log4j.level=DEBUG
-Dcukes.selenium.defaultFindRetries=2

##techstack
GRID_CHROME
GRID_IEXPLORE
GRID_FIREFOX
GRID_MSEDGE
LOCAL_CHROME
LOCAL_IEXPLORE
LOCAL_FIREFOX
LOCAL_MSEDGE
LOCAL_APPIUM
REMOTE_BROWSERSTACKS
HTMLUNIT_CHROME
HTMLUNIT_FIREFOX

## capabilities
"platformName": "android",
"device": "Google Pixel 3",
"os_version": "9.0"

## Writing feature file
1. use "@(testdata)" format to read data from properties file.
2. use "#(testdata)" to read data from JSON.



