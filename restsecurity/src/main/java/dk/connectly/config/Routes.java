package dk.connectly.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.connectly.controllers.PostController;
import dk.connectly.controllers.SecurityController;
import dk.connectly.controllers.TestController;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.security.RouteRole;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {
    private static SecurityController sc;
    private static TestController tc;
    private static PostController pc;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static EndpointGroup setRoutes(boolean isTesting){
        return () -> {
            sc = SecurityController.getInstance(isTesting);
            tc = TestController.getInstance();
            pc = PostController.getInstance();

            get("/", ctx -> ctx.json(objectMapper.createObjectNode().put("Message", "Connected Successfully")), roles.ANYONE);
            path("/security", getSecurityRoutes());
            path("/protected",getProtectedRoutes());
            path("/public", getPublicRoutes());

        };
    }


    public static EndpointGroup getSecurityRoutes(){
        return () -> {
            path("/auth", () -> {
                post("/login", sc.login(), roles.ANYONE);
                post("/register", sc.register(), roles.ANYONE);
            });

        };
    }

    public static EndpointGroup getProtectedRoutes(){
        return () -> {
            path("/post", () -> {
                post("/create", pc.createPost(), roles.ANYONE);
                get("/posts", pc.getPostsByVisibility(), roles.ANYONE);
            });

        };
    }

    public static EndpointGroup getPublicRoutes(){
        return () -> {

            };
    }

    public static EndpointGroup getTestRoutes(){
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


