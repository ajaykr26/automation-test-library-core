Feature: Call an api with tag

  Scenario: Call an api with tag
    * def featurePathToCallWithTag = featurePathToCall + tagName
    * call read(featurePathToCallWithTag)
