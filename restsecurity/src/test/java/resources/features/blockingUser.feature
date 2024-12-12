Feature: Block use

    Scenario: Block a user
        Given the user is logged in
        When the user blocks another user
        Then the blocked user is added to the user's block list