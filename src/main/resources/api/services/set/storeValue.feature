@ignore
Feature: Set attribute value in a response


  @setAttribute
  Scenario: 
    * def updatedJson = arg.response
    * def directJsonPath = '$.' + arg.attributeName
    * def replacedValue = karate.JsonPath(updatedJson, finalJsonPath)