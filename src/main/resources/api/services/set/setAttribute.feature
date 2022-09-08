Feature: Set attribute value in a response

  @setAttribute
  Scenario: 
    * def updatedJson = arg.response
    * def directJsonPath = '$.' + arg.attributeName
    * karate.set('updatedJson', finalJsonPath, arg.attributeValue)
    * def replacedValue = karate.JsonPath(updatedJson, finalJsonPath)