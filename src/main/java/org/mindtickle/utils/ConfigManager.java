package org.mindtickle.utils;
import java.io.IOException;
import java.util.Properties;
public class ConfigManager {
    public static ConfigManager manager;
    public static final Properties properties = new Properties();
    private ConfigManager() throws IOException {
        properties.load(ConfigManager.class.getClassLoader().getResourceAsStream("config.properties"));}
    public static ConfigManager getInstance() throws IOException {
        if(manager == null){
            synchronized (ConfigManager.class)
            {manager= new ConfigManager();}
        }return manager;
    }

    public String getString(String key){
        return System.getProperty(key,properties.getProperty(key));
    }

}

