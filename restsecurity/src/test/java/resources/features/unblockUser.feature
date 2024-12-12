Feature: Unblock use

  Scenario: Block a user
    Given the user has blocked another user
    When the user unblocks the blocked user
    Then the blocked user is removed from the user's block list
