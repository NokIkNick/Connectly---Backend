package org.example.utils;

import org.example.exceptions.ApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {

    public static String getPropertyValue(String SECRET_KEY, String CONFIG) throws ApiException{
        try(InputStream is = Utils.class.getClassLoader().getResourceAsStream(CONFIG)) {
            Properties prop = new Properties();
            prop.load(is);
            return prop.getProperty(SECRET_KEY);

        }catch(IOException ex){
            ex.printStackTrace();
            throw new ApiException(500,"Error while getting property value");
        }
    }

}
