Feature: Clear repositories

  Background:
    * url 'http://localhost:8081/api/'

  Scenario: Clean recipe repository
    Given path 'recipe/clear'

  Scenario: Clean ingredient repository
    Given path 'ingredient/clear'

  Scenario: Clean menu repository
    Given path 'menu/clear'