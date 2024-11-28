package dk.connectly;

import java.util.HashMap;

import dk.connectly.config.ApplicationConfig;
import dk.connectly.config.HibernateConfig;
import dk.connectly.config.Routes;
import dk.connectly.model.Post;
import dk.connectly.utils.Populator;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    private static String currentSetting = "";
    private static String buildString = "";
    
    public static void main(String[] args) {
        HashMap<String, String> settings = new HashMap<>();
        Runnable addToSettings = () -> {
            settings.put(currentSetting, buildString);
            buildString = "";
            currentSetting = "";
        };

        for (String arg : args) {
            if(arg.charAt(0) == '-'){
                if(currentSetting != ""){
                    addToSettings.run();
                }
                currentSetting = arg;
            } else if(arg.charAt(0) == '"') {
                buildString = arg.substring(1);
            } else if(buildString != ""){
                if(arg.charAt(arg.length()-1) == '"'){
                    buildString += arg.substring(0, arg.length() - 1);
                    addToSettings.run();
                } else {
                    buildString += arg;
                }
            } else {
                buildString = arg;
                addToSettings.run();
            }
        }

        // the last setting needs to be added if it doesn't have a corresponding text. example -t for testing
        if(currentSetting != "" & buildString == "") {
            addToSettings.run();
        }

        boolean isTesting = settings.containsKey("t") ? true : false;
        @SuppressWarnings("unused")
        ApplicationConfig app = ApplicationConfig.getInstance()
                .initiateServer()
                .setExceptionHandling()
                .startServer(7070)
                .setRoutes(Routes.setRoutes(isTesting))
                .checkSecurityRoles(isTesting)
                .configureCors();

        Populator populator = new Populator(HibernateConfig.getEntityManagerFactoryConfig());
        //populator.populate();

    }
}