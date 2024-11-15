package dk.connectly.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import dk.connectly.controllers.ConnectionController;
import dk.connectly.controllers.ConnectionRequestController;
import dk.connectly.controllers.SecurityController;
import dk.connectly.controllers.TestController;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.security.RouteRole;
import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {
    private static SecurityController sc;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static TestController tc;
    private static ConnectionRequestController crc;
    private static ConnectionController cc;

    public static EndpointGroup getRoutes(boolean isTesting){
        sc = SecurityController.getInstance(isTesting);
        tc = TestController.getInstance(isTesting);
        crc = ConnectionRequestController.getInstance(isTesting);
        cc = ConnectionController.getInstance(isTesting);
        return () -> {
            path("/", () -> {
                get("/", ctx -> ctx.json(objectMapper.createObjectNode().put("Message", "Connected Successfully")), roles.ANYONE);
            });
            path("/auth", () -> {
                post("/login", sc.login(), roles.ANYONE);
                post("/register", sc.register(), roles.ANYONE);
            });
            path("/connection", () -> {
                post("/request/new", crc.setupRequest(), roles.ANYONE); // To Do change back to roles.USER once authenticate can correctly identify USER
                put("/request/confirm", cc.acceptRequest(), roles.ANYONE);
            });
            path("/protected", () -> {
                before(sc.authenticate());
                get("/user_demo", ctx-> ctx.json(objectMapper.createObjectNode()), roles.USER);
                get("/admin_demo", ctx-> ctx.json(objectMapper.createObjectNode()), roles.ADMIN);
            });
        };
    }

    public static EndpointGroup getTestRoutes(){
        tc = TestController.getInstance(true);
        return ()-> {
            get("/getAll", tc.getAllTest());
            post("/create",tc.createTest());
        };
    }


    public enum roles implements RouteRole {
        USER,
        ADMIN,
        ANYONE
    }

}
