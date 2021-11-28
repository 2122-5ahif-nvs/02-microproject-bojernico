Feature: Recipe update endpoint. An user of the endpoint is able to update Recipes

  Background:
    * url 'http://localhost:8081/api/'
    * def input = read('data/updated-recipe.json')

  Scenario: Update a Recipe -> works
    Given path 'recipe/update/1'
    And request input
    When method PUT
    Then status 200
    And match response == 'Successfully updated recipe with id 1'

  Scenario: Update a Recipe -> Recipe is missing
  Given path 'recipe/update/100'
    And request input
    When method PUT
    Then status 400
    Then match header reason == 'JsonObject has serious errors.'