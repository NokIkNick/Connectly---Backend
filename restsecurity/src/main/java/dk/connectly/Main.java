package dk.connectly;

import dk.connectly.config.ApplicationConfig;
import dk.connectly.config.HibernateConfig;
import dk.connectly.config.Routes;
import dk.connectly.model.Post;
import dk.connectly.utils.Populator;
import jakarta.persistence.EntityManagerFactory;

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

        Populator populator = new Populator(HibernateConfig.getEntityManagerFactoryConfig());
        //populator.populate();

    }
}