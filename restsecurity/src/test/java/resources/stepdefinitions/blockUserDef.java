package resources.stepdefinitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.connectly.dtos.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.javalin.http.HttpStatus;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class blockUserDef {
    private static String user1;
    private static LoginDTO user1Info = new LoginDTO("dude1@example.com", "userPass@123.12");
    private static TokenDTO token;

    private static String user2;
    private static LoginDTO user2Info = new LoginDTO("dude2@example.com", "userPass@123.12");


    private static ObjectMapper om = new ObjectMapper();

    private HttpResponse<String> response;
    private HttpRequest request;

    @Given("the user is logged in")
    public void the_user_is_logged_in() {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest.BodyPublisher bp;
        try {
            bp = HttpRequest.BodyPublishers.ofString(om.writeValueAsString(user1Info));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:7070/api/auth/register"))
                    .POST(bp)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(HttpStatus.CREATED, response.statusCode());
            token = om.readValue(response.body(), TokenDTO.class);
            user1 = token.getUsername();

            assertEquals(user1, user1Info.getEmail());

        } catch (Exception e) {
            assertTrue(false);
        }


    }


    @When("the user blocks another user")
    public void theUserBlocksAnotherUser() {
        HttpClient client = HttpClient.newHttpClient();

        try {
            BlockingDTO dto = new BlockingDTO(user1Info.getEmail(), user2Info.getEmail());
            HttpRequest.BodyPublisher bp = HttpRequest.BodyPublishers.ofString(om.writeValueAsString(dto));

             request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:7070/api/block"))
                    .header("Authorization", "Bearer " + token.getToken())
                    .POST(bp)
                    .build();

             response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(HttpStatus.CREATED, response.statusCode());

        } catch (Exception e) {
            assertTrue(false);
        }
        
    }

    @Then("the blocked user is added to the user's block list")
    public void theBlockedUserIsAddedToTheUserSBlockList() {

        try {
            HttpClient client = HttpClient.newHttpClient();

             response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(HttpStatus.OK, response.statusCode());
            BlockingDTO[] blockingDTOs = om.readValue(response.body(), BlockingDTO[].class);
            assertEquals(1, blockingDTOs.length);
            assertEquals(user2Info.getEmail(), blockingDTOs[0].getBlocked_email());

        } catch (Exception e) {
            assertTrue(false);
        }

    }
}
