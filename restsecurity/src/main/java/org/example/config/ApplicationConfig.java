package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import io.javalin.plugin.bundled.RouteOverviewPlugin;
import io.javalin.security.AccessManager;
import io.javalin.security.RouteRole;
import org.example.controllers.SecurityController;
import org.example.dtos.UserDTO;
import org.example.exceptions.ApiException;
import org.example.utils.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class ApplicationConfig {

    private static ObjectMapper jsonMapper = new ObjectMapper();
    private static ApplicationConfig appConfig;

    private static Javalin app;

    private ApplicationConfig(){}

    public static ApplicationConfig getInstance(){
        if(appConfig == null){
            appConfig = new ApplicationConfig();
        }
        return appConfig;
    }

    public ApplicationConfig initiateServer(){
        System.out.println("Working Directory = "+ System.getProperty("user.dir"));
        String separator = System.getProperty("file.separator");
        app = Javalin.create(config -> {
            config.http.defaultContentType="application/json";
            config.routing.contextPath="/api";
            config.plugins.enableDevLogging();
        });
        return appConfig;
    };


    public ApplicationConfig setRoutes(EndpointGroup routes){
        app.routes(routes);
        return appConfig;
    }

    public ApplicationConfig startServer( int port){
        app.start(port);
        return appConfig;
    };

    public ApplicationConfig setExceptionHandling(){
        app.exception(ApiException.class, (e, ctx) -> {
            ObjectNode exception = Logger.log(e.getStatus(), e.getMessage(),ctx.ip());
            ctx.json(exception).status(e.getStatus());
        });
        app.exception(NumberFormatException.class, (e, ctx) -> {
            ObjectNode exception = Logger.log(HttpStatus.forStatus(400), e.getMessage(),ctx.ip());
            ctx.json(exception).status(HttpStatus.forStatus(400));
        });
        app.exception(Exception.class, (e, ctx) -> {
            ObjectNode exception = Logger.log(HttpStatus.forStatus(500), e.getMessage(), ctx.ip());
            ctx.json(exception).status(HttpStatus.forStatus(500));
        });
        return appConfig;
    }

    public ApplicationConfig closeServer(){
        app.close();
        return appConfig;
    }

    public ApplicationConfig checkSecurityRoles(boolean isTesting) {
        // Check roles on the user (ctx.attribute("username") and compare with permittedRoles using securityController.authorize()
        app.updateConfig(config -> {

            config.accessManager((handler, ctx, permittedRoles) -> {
                // permitted roles are defined in the last arg to routes: get("/", ctx -> ctx.result("Hello World"), Role.ANYONE);

                Set<String> allowedRoles = permittedRoles.stream().map(role -> role.toString().toUpperCase()).collect(Collectors.toSet());
                if(allowedRoles.contains("ANYONE") || ctx.method().toString().equals("OPTIONS")) {
                    // Allow requests from anyone and OPTIONS requests (preflight in CORS)
                    handler.handle(ctx);
                    return;
                }

                UserDTO user = ctx.attribute("user");
                System.out.println("USER IN CHECK_SEC_ROLES: "+user);
                if(user == null) {
                    ctx.status(HttpStatus.FORBIDDEN)
                            .json(jsonMapper.createObjectNode()
                                    .put("msg", "Not authorized. No username were added from the token"));
                }
                if (authorize(user, allowedRoles)){
                    handler.handle(ctx);

                }else{
                    try {
                        throw new ApiException(HttpStatus.FORBIDDEN.getCode(), "Unauthorized with roles: "+allowedRoles);
                    } catch (ApiException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        });
        return appConfig;
    }

    public boolean authorize(UserDTO user, Set<String> allowedRoles) {
        AtomicBoolean hasAccess = new AtomicBoolean(false); // Since we update this in a lambda expression, we need to use an AtomicBoolean
        if (user != null) {
            user.getRoles().stream().forEach(role -> {
                if (allowedRoles.contains(role.toUpperCase())) {
                    hasAccess.set(true);
                }
            });
        }
        return hasAccess.get();
    }

    public ApplicationConfig configureCors() {
        app.before(ctx -> {
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type");
        });

        app.options("/*", ctx -> {
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type");
        });

        return appConfig;
    }
}