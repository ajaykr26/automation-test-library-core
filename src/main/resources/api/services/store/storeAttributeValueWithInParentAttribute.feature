Feature: Set attribute value in a response with parent


  @json
  Scenario: json parsing
    * def arrayJsonPath = '$..' + parentAttributeName + '[0].' + attributeName
    * def directJsonPath = '$..' + parentAttributeName + '.' + attributeName
    * def finalJsonPath = karate.sizeOf(arrayJsonPath > 0 ? arrayJsonPath : directJsonPath)
    * def attributeValue = karate.jsonPath(response, finalJsonPath)

  @xml
  Scenario: xml parsing
#    * def xpath = '//' + parentAttributeName + '/' + attributeName
#    * def attributeValue = karate.xmlPath(responseXml, xpath)

    * def listOfXpath = '//' + parentAttributeName + '//' + attributeName
    * def xpath = '//' + parentAttributeName + '/' + attributeName
    * def finalXpath = karate.sizeOf(listOfXpath > 0 ? listOfXpath : xpath)
    * def attributeValue = karate.xmlPath(response, finalXpath)