package org.hse.brina;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Config предоставляет значения ширины и высоты по умолчанию для приложения, сохраняет путь до базы данных у пользователя
 */
public class Config {
    private static final Properties properties = new Properties();
    private static final Logger logger = LogManager.getLogger();

    static {
        try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(inputStream);
            properties.setProperty("projectPath", System.getProperty("user.dir"));
            FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/config.properties");
            properties.store(fileOutputStream, null);
            fileOutputStream.close();
        } catch (Exception e) {
            logger.error("Error loading the settings file: {}", e.getMessage());
        }
    }

    public static double getDefaultWidth() {
        return Double.parseDouble(properties.getProperty("defaultWidth"));

    }

    public static double getDefaultHeight() {
        return Double.parseDouble(properties.getProperty("defaultHeight"));
    }

    public static String getPathToDB() {
        return getProjectPath() + properties.getProperty("pathToDB");
    }

    public static String getProjectPath() {
        return properties.getProperty("projectPath");
    }
}

