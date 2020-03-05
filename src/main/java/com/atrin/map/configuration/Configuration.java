package com.atrin.map.configuration;

import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by atrinhojjat on 9/20/15.
 */
public class Configuration {

    // TODO  Read .Properties Configuration File

    private Properties properties  = new Properties();
    public static final String DEFAULT_FILE = "/com/atrin/map/configuration/config.properties";

    public static final Configuration config = new Configuration();

    public Configuration(){
        try {
            properties.load(this.getClass().getResourceAsStream(DEFAULT_FILE));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public Configuration(String file){
        try {
            properties.load(this.getClass().getResourceAsStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    public String get(String name){
        return properties.getProperty(name);
    }

    public Integer getAsInteger(String name){
        return Integer.parseInt(properties.getProperty(name));
    }

    public Double getAsDouble(String name){
        return Double.parseDouble(properties.getProperty(name));
    }

    public Boolean check(String name){
        return properties.containsKey(name);
    }

}
