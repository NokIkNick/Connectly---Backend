package dk.connectly.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.connectly.controllers.ChatController;
import dk.connectly.controllers.SecurityController;
import dk.connectly.controllers.TestController;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.security.RouteRole;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {
    private static SecurityController sc;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static TestController tc;
    private static ChatController cc;

    public static EndpointGroup getRoutes(boolean isTesting){
        sc = SecurityController.getInstance(isTesting);
        cc = ChatController.getInstance(isTesting);
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
            path("/chat", () -> {
                post("/createChat", cc.createChat(), roles.ANYONE);
                get("/getChatByParticipants", cc.fetchChat(), roles.ANYONE);
                get("/getChatById/{id}", cc.fetchChatById(), roles.ANYONE);
                post("/sendMessage", cc.sendMessage(), roles.ANYONE);
                get("/getMessagesByChatId/{id}", cc.getMessagesByChatId(), roles.ANYONE);
                get("/getChatsByUser/{email}", cc.getChatsByUser(), roles.ANYONE);
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


    public enum roles implements RouteRole {
        USER,
        ADMIN,
        ANYONE
    }

}
