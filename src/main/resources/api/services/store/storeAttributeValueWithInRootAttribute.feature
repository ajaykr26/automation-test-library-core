Feature: Get attribute value in a response

  @getAttribute
  Scenario: 
    * def jsonPath = '$[0].' + attributeName
    * def attributeValueToStore = karate.jsonPath(response, jsonPath)