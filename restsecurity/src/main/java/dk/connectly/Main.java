package dk.connectly;

import dk.connectly.config.ApplicationConfig;
import dk.connectly.config.HibernateConfig;
import dk.connectly.config.Routes;
import dk.connectly.daos.ChatServiceDAO;
import dk.connectly.utils.Populator;

public class Main {
    public static void main(String[] args) {
        boolean isTesting = true;
        ApplicationConfig app = ApplicationConfig.getInstance()
                .initiateServer()
                .setExceptionHandling()
                .startServer(7070)
                .setRoutes(Routes.getRoutes(isTesting))
                .checkSecurityRoles(isTesting)
                .configureCors();

        //Populator populator = new Populator(HibernateConfig.getEntityManagerFactoryConfig());


        //ChatServiceDAO.getInstance(isTesting).establishTestConnection();
        //populator.populate();

    }
}