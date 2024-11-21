package dk.connectly.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.connectly.controllers.PostController;
import dk.connectly.controllers.SecurityController;
import dk.connectly.controllers.TestController;
import dk.connectly.dtos.PostDTO;
import dk.connectly.model.User;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.security.RouteRole;

import java.util.List;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {
    private static SecurityController sc;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static TestController tc;
    private static PostController pc;


    public static EndpointGroup getRoutes(boolean isTesting){
        sc = SecurityController.getInstance(isTesting);
        return () -> {
            path("/", () -> {
                get("/", ctx -> ctx.json(objectMapper.createObjectNode().put("Message", "Connected Successfully")), roles.ANYONE);
            });
            path("/auth", () -> {
                post("/login", sc.login(), roles.ANYONE);
                post("/register", sc.register(), roles.ANYONE);
            });
            path("/protected", () -> {
                before(sc.authenticate());
                get("/user_demo", ctx-> ctx.json(objectMapper.createObjectNode()), roles.USER);
                get("/admin_demo", ctx-> ctx.json(objectMapper.createObjectNode()), roles.ADMIN);
            });
        };
    }

    public static EndpointGroup getTestRoutes(){
        tc = TestController.getInstance();
        return ()-> {
            get("/getAll", tc.getAllTest());
            post("/create",tc.createTest());
        };
    }

    public static EndpointGroup getPostRoutes() {
        pc = PostController.getInstance();
        return () -> {
            path("/post", () -> {
                post("/create", pc.createPost(), roles.ANYONE);
                get("/posts", pc.getPostsByVisibility(), roles.ANYONE);
            });
        };
    }




    public enum roles implements RouteRole {
        USER,
        ADMIN,
        ANYONE
    }

}
