package dk.connectly;

import dk.connectly.config.ApplicationConfig;
import dk.connectly.config.HibernateConfig;
import dk.connectly.config.MongoDBConfig;
import dk.connectly.config.Routes;
import dk.connectly.model.Post;
import dk.connectly.utils.Populator;
import jakarta.persistence.EntityManagerFactory;

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

        Populator populator = new Populator(HibernateConfig.getEntityManagerFactoryConfig());

        //MongoDBConfig.dropDatabase();

        //populator.populate();

    }
}