Feature: Get Posts by Visibility

  Scenario: Get all Friends Posts
    Given the user is logged in
    When the user has friends connections
    Then the user can see all friends posts
