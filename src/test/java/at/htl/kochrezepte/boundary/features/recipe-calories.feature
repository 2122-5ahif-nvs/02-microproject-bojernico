Feature: Get Calories for Recipe Endpoint. An user of the endpoint is able to get the calories.

  Background:
    * url 'http://localhost:8081/api/'
    * def input = read('data/input.json')

  Scenario: Get Calories for a Recipe -> works
    Given path 'recipe/get-calories-by-id/1'
    When method GET
    Then status 200
    And match response == '4335 kcal'

  Scenario: Get Calories for a Recipe -> Recipe missing
    Given path 'recipe/get-calories-by-id/100'
    When method GET
    Then status 400
    Then match header reason == "Recipe with identifier 100 doesn't exist"