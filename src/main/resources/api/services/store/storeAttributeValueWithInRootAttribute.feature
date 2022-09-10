Feature: Get attribute value in a response

  @json
  Scenario: json parsing
    * def jsonpath = '$.' + attributeName
    * def attributeValue = karate.jsonPath(response, jsonpath)


  @xml
  Scenario: xml parsing
    * def xpath = '//' + attributeName
    * def attributeValue = karate.xmlPath(responseXml, xpath)
