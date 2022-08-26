@ignore
Feature: Remove attribute in a response


  @removeAttribute
  Scenario: 
    * def updatedJson = response
    * def directJsonPath = '$.' + attributeName
    * karate.remove('updatedJson', directJsonPath)