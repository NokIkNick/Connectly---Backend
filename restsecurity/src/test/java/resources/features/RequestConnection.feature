Feature: Connection
Scenario: Create Request
  Given I am logged in as a user
  And I have another user
  When I add them
  Then A Request is made