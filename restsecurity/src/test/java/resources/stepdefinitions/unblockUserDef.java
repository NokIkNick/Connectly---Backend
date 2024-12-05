package resources.stepdefinitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.connectly.dtos.LoginDTO;
import dk.connectly.dtos.TokenDTO;
import dk.connectly.model.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

public class unblockUserDef {
    private static User user1;
    private static User user2;

    @Given("the user has blocked another user")
    public void theUserHasBlockedAnotherUser() {
        user1 = new User("dude1@example.com", "userPass@123.12");
        user2 = new User("dude2@example.com", "userPass@123.12");

        user1.setBlockedUsers(List.of(user2));

        assertEquals(1, user1.getBlockedUsers().size());
    }

    @When("the user unblocks the blocked user")
    public void theUserUnblocksTheBlockedUser() {
        user1.unblockUser(user2);
    }

    @Then("the blocked user is removed from the user's block list")
    public void theBlockedUserIsRemovedFromTheUserSBlockList() {
        assertEquals(0, user1.getBlockedUsers().size());
    }
}
