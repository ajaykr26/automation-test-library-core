Feature: Set attribute value in a response with parent


  @setAttributeValueWithParent
  Scenario: 
    * def updatedJson = arg.response

    * def arrayJsonPath = '$..' + arg.parentAttributeName + '[0].' + arg.attributeName
    * def directJsonPath = '$..' + arg.parentAttributeName + '.' + arg.attributeName
    * def valueToReplace = karate.JsonPath(updatedJson, arrayJsonPath)
    * def finalJsonPath = karate.sizeOf(valueToReplace > 0 ? arrayJsonPath : directJsonPath)

    * karate.set('updatedJson', finalJsonPath, arg.attributeValue)
    * def replacedValue = karate.JsonPath(updatedJson, finalJsonPath)