package org.example;

import io.javalin.Javalin;
import org.example.config.ApplicationConfig;
import org.example.config.Routes;
import org.example.daos.TestMemoryDao;
import org.example.dtos.TestDTO;

import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        boolean isTesting = false;
        ApplicationConfig app = ApplicationConfig.getInstance()
                .initiateServer()
                .setExceptionHandling()
                .startServer(7070)
                .setRoutes(Routes.getRoutes(isTesting))
                .checkSecurityRoles(isTesting)
                .configureCors();
    }
}