package dk.connectly.stepdefinitions;

import com.fasterxml.jackson.databind.ObjectMapper;

import dk.connectly.config.ApplicationConfig;
import dk.connectly.config.Routes;
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

import io.cucumber.java.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class loggedInAs_Def {
  private static String user1;
  private static LoginDTO user1Info = new LoginDTO("dude3@example.com", "userPass@123.12");
  public static TokenDTO token;

  private static ObjectMapper om = new ObjectMapper();

  public static ApplicationConfig app;
  
  
  @BeforeAll
  public static void setupUser() {
    if(token != null) return;

    app = ApplicationConfig.getInstance()
                .initiateServer()
                .setExceptionHandling()
                .startServer(7070)
                .setRoutes(Routes.getRoutes(true))
                .checkSecurityRoles(true)
                .configureCors();

    System.out.println("setupUserRun");

    HttpClient client = HttpClient.newHttpClient();

    HttpRequest.BodyPublisher bp;
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
      assertTrue(false);
    }
  }

  @Given("the user is logged in")
  public void the_user_is_logged_in() {
    setupUser();

    HttpClient client = HttpClient.newHttpClient();

    HttpRequest.BodyPublisher bp;
    try {
      bp = HttpRequest.BodyPublishers.ofString(om.writeValueAsString(user1Info));

      HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create("http://localhost:7070/api/auth/login"))
              .POST(bp)
              .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      assertEquals(HttpStatus.OK.getCode(), response.statusCode());
      token = om.readValue(response.body(), TokenDTO.class);
      user1 = token.getUsername();

      assertEquals(user1, user1Info.getEmail());

    } catch (Exception e) {
        assertTrue(false);
    }
  }
}
