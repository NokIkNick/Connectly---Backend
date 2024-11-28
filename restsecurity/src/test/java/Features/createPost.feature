Feature: Create post
  As a user,
  I want to create a post and select the correct tag(s),
  So that my desired connections can see my post in their feed.


Scenario: Create a post
  Given the user has written a post with this content: "Content of the post"

  When the user clicks Upload Post,

  Then the post will be visible in the given feed.
