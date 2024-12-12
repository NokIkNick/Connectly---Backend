package dk.connectly.stepdefinitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.connectly.dtos.ConnectionRequestDTO;
import dk.connectly.dtos.LoginDTO;
import dk.connectly.dtos.PostDTO;
import dk.connectly.dtos.TokenDTO;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.javalin.http.HttpStatus;
import io.cucumber.java.en.Then;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class createPosDef {
    private static String user1;
    private static LoginDTO user1Info = new LoginDTO("dude1@example.com", "userPass@123.12");
    private static TokenDTO token;

    private static PostDTO postDTO;

    private static ObjectMapper om = new ObjectMapper();


    @Given("the user is logged in")
    public void theUserIsLoggedIn() {
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

    @When("the user creates a post")
    public void theUserCreatesAPost() {
        HttpClient client = HttpClient.newHttpClient();

        try {
            postDTO = new PostDTO();
            HttpRequest.BodyPublisher bp = HttpRequest.BodyPublishers.ofString(om.writeValueAsString(postDTO));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:7070/api/post/create"))
                    .header("Authorization", "Bearer " + token.getToken())
                    .POST(bp)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(HttpStatus.CREATED, response.statusCode());

        } catch (Exception e) {
            assertTrue(false);
        }

    }


    @Then("the post is created successfully")
    public void thePostIsCreatedSuccessfully() {
        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:7070/api/post/posts"))
                    .header("Authorization", "Bearer " + token.getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(HttpStatus.OK, response.statusCode());

        } catch (Exception e) {
            assertTrue(false);
        }

    }
}
