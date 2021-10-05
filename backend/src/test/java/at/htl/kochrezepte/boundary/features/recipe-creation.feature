Feature: Recipe creation endpoint. An user of the endpoint is able to create Recipes

  Background:
    * url 'http://localhost:8081/api/'
    * def input = read('data/recipes.json')

  Scenario: Create a Recipe -> works
    Given path 'recipe/create'
     And request input
     When method POST
     Then status 200
     And match response == 'Successfully added multiple recipes.'

  Scenario: Create a Recipe -> essential data missing
    Given path 'recipe/create'
    And request {}
    When method POST
    Then status 400
    Then match header reason == 'JsonObject has serious errors.'