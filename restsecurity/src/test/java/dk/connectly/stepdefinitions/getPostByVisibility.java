package dk.connectly.stepdefinitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.connectly.dtos.*;
import dk.connectly.utils.ConnectionType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.javalin.http.HttpStatus;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class getPostByVisibility {

    private static String user1;
    private static LoginDTO user1Info = new LoginDTO("dude1@example.com", "userPass@123.12");
    private static TokenDTO token;

    private static String user2;
    private static LoginDTO user2Info = new LoginDTO("dude2@example.com", "userPass@123.12");

    private static ConnectionRequestDTO dto;


    private static PostDTO postDTO;

    private static ObjectMapper om = new ObjectMapper();

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

    @When("the user has friends connections")
    public void the_user_has_friends_connections() {
        HttpClient client = HttpClient.newHttpClient();

        UserDTO UserDTO2 = new UserDTO(user2, Set.of());

        NewConnectionDTO crdto = new NewConnectionDTO(UserDTO2, Set.of(ConnectionType.FRIEND));

        HttpRequest.BodyPublisher bp;
        try {
            bp = HttpRequest.BodyPublishers.ofString(om.writeValueAsString(crdto));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:7070/api/auth/register"))
                    .header("Authroization", om.writeValueAsString(token))
                    .POST(bp)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(HttpStatus.CREATED, response.statusCode());
            dto = om.readValue(response.body(), ConnectionRequestDTO.class);

        } catch (Exception e) {
            assertTrue(false);
        }



    }


    @Then("the user can see all friends posts")
    public void the_user_can_see_all_friends_posts() {
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


