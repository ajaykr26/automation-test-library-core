Feature: Set attribute value in a response

  @setAttribute
  Scenario: 
    * def jsonPath = '$[0].' + attributeName
    * karate.set('response', jsonPath, attributeValue)
    * def replacedValue = karate.jsonPath(response, jsonPath)
