package dk.connectly.controllers;

import dk.connectly.config.ApplicationConfig;
import dk.connectly.config.HibernateConfig;
import dk.connectly.config.Routes;
import dk.connectly.daos.ChatServiceDAO;
import dk.connectly.exceptions.ApiException;
import dk.connectly.model.Role;
import dk.connectly.model.User;
import io.javalin.http.HttpStatus;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
class ChatControllerTest {

    private static EntityManagerFactory emf;
    private static User userUser;
    private static Role USER;
    private static Object userToken;

    /* WORKING ON THE TESTS CURRENTLY NOT WORKING.
    @BeforeAll
    static void setUpAll(){
        emf = HibernateConfig.getEntityManagerFactoryConfigForTesting();

        RestAssured.baseURI = "http://localhost:7070/api";
        ApplicationConfig applicationConfig = ApplicationConfig.getInstance();
        applicationConfig.initiateServer()
                .startServer(7070)
                .setExceptionHandling()
                .setRoutes(Routes.getRoutes(true))
                .checkSecurityRoles(true);

        USER = new Role("USER");

        userUser = new User("userPassword", "user@email.dk");

        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(USER);
            userUser.addRole(USER);
            em.persist(userUser);
            em.getTransaction().commit();
        }
        userToken = getToken(userUser.getEmail(), "userPassword");
        System.out.println(userToken);

    }

    public static Object getToken(String email, String password)
    {
        return login(email, password);
    }

    private static Object login(String email, String password)
    {
        String json = String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password);
        System.out.println(json);

        var token = given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("http://localhost:7070/api/auth/login")
                .then()
                .extract()
                .response()
                .body()
                .path("token");

        return "Bearer " + token;
    }

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
        ChatServiceDAO.getInstance(true).dropDatabase();
    }

    @Test
    void createChat() {
        String userEmail1 = "testgutmail@coolmail.dk";
        String userEmail2 = "testdamemail4@cooleremail.dk";
        int chatId = 1;

        String json  = "{\"participants\": [\n" +
                "  \"testgutmail@coolmail.dk\",\n" +
                "  \"testdamemail4@cooleremail.dk\"\n" +
                "]}";

        RestAssured.given()
                .header("Authorization", userToken)
                .contentType("application/json")
                .body(json)
                .when()
                .post("http://localhost:7070/api/chat/createChat")
                .then().log().all()
                .statusCode(200)
                .body("_id", notNullValue())
                .body("_id", equalTo(chatId))
                .body("participants", notNullValue())
                .body("participants[0]", equalTo(userEmail1))
                .body("participants[1]", equalTo(userEmail2));
    }

    @Test
    void fetchChat() throws ApiException {
        String userEmail1 = "testgutmail@coolmail.dk";
        String userEmail2 = "testdamemail4@cooleremail.dk";

        ChatServiceDAO serviceDAO = ChatServiceDAO.getInstance(true);
        Document created = serviceDAO.createChat(userEmail1,userEmail2);
        System.out.println(created.toJson());
        //serviceDAO.displayDatabaseName();


        String json  = "{\n" +
                "  \"participants\": [\n" +
                "    \"testgutmail@coolmail.dk\",\n" +
                "    \"testdamemail4@cooleremail.dk\"\n" +
                "  ]\n" +
                "\n" +
                "}";

        RestAssured.given()
                .header("Authorization", userToken)
                .contentType("application/json")
                .body(json)
                .when()
                .post("http://localhost:7070/api/chat/getChatByParticipants")
                .then().log().all()
                .statusCode(200)
                .body("_id", notNullValue())
                .body("_id", equalTo(created.get("id_")))
                .body("participants", notNullValue())
                .body("participants[0]", equalTo(userEmail1))
                .body("participants[1]", equalTo(userEmail2));
    }

    @Test
    void fetchChatById() {

    }

    @Test
    void sendMessage() {
    }

    @Test
    void getMessagesByChatId() {
    }

    @Test
    void getChatsByUser() {
    }
    */
}