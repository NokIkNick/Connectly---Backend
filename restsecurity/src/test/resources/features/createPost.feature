Feature: Create post
Scenario: Create a post
  Given the user is logged in

  When the user creates a post

  Then the post is created successfully
