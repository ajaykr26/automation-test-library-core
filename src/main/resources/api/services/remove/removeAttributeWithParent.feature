@ignore
Feature: Remove attribute in a response


  @removeAttribute
  Scenario:
    * def updatedJson = arg.response
    * def directJsonPath = '$.' + arg.attributeName
    * karate.remove('updatedJson', directJsonPath)