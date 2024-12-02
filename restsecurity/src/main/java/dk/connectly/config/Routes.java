package dk.connectly.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import dk.connectly.controllers.ConnectionController;
import dk.connectly.controllers.ConnectionRequestController;
import dk.connectly.controllers.SecurityController;
import dk.connectly.controllers.TestController;
import dk.connectly.daos.ConnectionDAO;
import dk.connectly.dtos.ConnectionDTO;
import dk.connectly.model.Connection;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.security.RouteRole;
import static io.javalin.apibuilder.ApiBuilder.*;

import java.util.List;

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
            before(sc.authenticate());
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
                get("/get/connections", cc.getAllIAmConnectedTo(), roles.ANYONE);
                get("/get/connectionsOf", cc.getAllConnectionsTo(), roles.ANYONE);
                get("/test", (ctx) -> {
                    var cdao = ConnectionDAO.getInstance(false);
                    List<Connection> conns = cdao.getAll();
                    ctx.json(conns.stream().map(x-> {
                        return new ConnectionDTO(x);
                    }).toList());
                }, roles.ANYONE);
            });
            path("/protected", () -> {
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
