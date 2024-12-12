package dk.connectly.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.javalin.http.HttpStatus;
import io.cucumber.java.en.Then;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

import dk.connectly.config.ApplicationConfig;
import dk.connectly.config.Routes;
import dk.connectly.dtos.ConnectionRequestDTO;
import dk.connectly.dtos.LoginDTO;
import dk.connectly.dtos.NewConnectionDTO;
import dk.connectly.dtos.TokenDTO;
import dk.connectly.dtos.UserDTO;
import dk.connectly.utils.ConnectionType;

public class RequestConnectionDef{

  private static String user1;
  private static LoginDTO user1Info = new LoginDTO("dude1@example.com", "userPass@123.12");
  private static TokenDTO token;

  private static String user2;
  private static LoginDTO user2Info = new LoginDTO("dude2@example.com", "userPass@123.12");

  private static ConnectionRequestDTO dto;

  private static ObjectMapper om = new ObjectMapper();

  ApplicationConfig app = ApplicationConfig.getInstance()
                .initiateServer()
                .setExceptionHandling()
                .startServer(7070)
                .setRoutes(Routes.getRoutes(true))
                .checkSecurityRoles(true)
                .configureCors();

  @Given("I am logged in as a user")
  public void loggedin() {
    HttpClient client = HttpClient.newHttpClient();
    System.out.println("logging in");
    

    BodyPublisher bp;
    try {
      bp = HttpRequest.BodyPublishers.ofString(om.writeValueAsString(user1Info));

      HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:7070/api/auth/register"))
        .POST(bp)
        .build();

      
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      assertEquals(HttpStatus.CREATED.getCode(), response.statusCode());
      token = om.readValue(response.body(), TokenDTO.class);
      user1 = token.getUsername();

      assertEquals(user1, user1Info.getEmail());

    } catch (Exception e) {
      System.out.println(e);
      assertTrue(false);
    }
  }

  @Given("I have another user")
  public void IHaveAnotherUser() {
    HttpClient client = HttpClient.newHttpClient();

    BodyPublisher bp;
    try {
      bp = HttpRequest.BodyPublishers.ofString(om.writeValueAsString(user2Info));

      HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:7070/api/auth/register"))
        .POST(bp)
        .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      assertEquals(HttpStatus.CREATED.getCode(), response.statusCode());
      user2 = om.readValue(response.body(), TokenDTO.class).getUsername();
      assertEquals(user2, user2Info.getEmail());

    } catch (Exception e) {
      assertTrue(false);
    }
  }

  @When("I add them")
  public void I_add_them(){
    HttpClient client = HttpClient.newHttpClient();

    UserDTO UserDTO2 = new UserDTO(user2, Set.of());

    NewConnectionDTO crdto = new NewConnectionDTO(UserDTO2, Set.of(ConnectionType.WORK));

    BodyPublisher bp;
    try {
      bp = HttpRequest.BodyPublishers.ofString(om.writeValueAsString(crdto));

      HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:7070/api/connection/request/new"))
        .header("Authorization", "BEARER " + token.getToken())
        .POST(bp)
        .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      assertEquals(HttpStatus.CREATED.getCode(), response.statusCode());
      dto = om.readValue(response.body(), ConnectionRequestDTO.class);

    } catch (Exception e) {
      assertTrue(false);
    }
  }

  @Then("A Request is made")
  public void aRequestIsMade() {
    assertEquals(user2, dto.getReceiver().getEmail());
  }
}
