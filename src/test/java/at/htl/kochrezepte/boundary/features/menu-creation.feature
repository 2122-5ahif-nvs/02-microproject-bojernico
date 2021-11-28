Feature: Menu creation endpoint. An user of the endpoint is able to create Menus

  Background:
    * url 'http://localhost:8081/api/'
    * def input = read('data/menu.json')

  Scenario: Create multiple menus -> works
    Given path 'menu/create'
    And request input
    When method POST
    Then status 200
    And match response == "Successfully added menu Jonas' Spezialitäten"

  Scenario: Create a menu -> fails
    Given path 'menu/create'
    And request { name: 'missing dessert', starterId: 4, entreeId : 1 }
    When method POST
    Then status 400
    Then match header reason == 'JsonObject has serious errors.'

