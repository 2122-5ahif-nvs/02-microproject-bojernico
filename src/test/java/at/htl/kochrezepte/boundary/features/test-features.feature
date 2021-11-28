Feature: Test features

  Scenario: Run all tests in order
    * call read('clear-repos.feature')
    * call read('recipe-creation.feature')
    * call read('recipe-update.feature')
    * call read('menu-creation.feature')
    * call read('recipe-calories.feature')
