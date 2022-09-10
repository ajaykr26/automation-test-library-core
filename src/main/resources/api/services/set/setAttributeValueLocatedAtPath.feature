Feature: Get attribute value in a response

  @xml
  Scenario: xml parsing
    * def attributeValue = karate.xmlPath(responseXml, xpath)

  @json
  Scenario: json parsing
    * def attributeValue = karate.jsonPath(response, jsonpath)