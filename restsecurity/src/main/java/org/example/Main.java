package org.example;

import org.example.config.ApplicationConfig;
import org.example.config.Routes;

public class Main {
    public static void main(String[] args) {
        boolean isTesting = false;
        @SuppressWarnings("unused")
        ApplicationConfig app = ApplicationConfig.getInstance()
                .initiateServer()
                .setExceptionHandling()
                .startServer(7070)
                .setRoutes(Routes.getRoutes(isTesting))
                .checkSecurityRoles(isTesting)
                .configureCors();
    }
}