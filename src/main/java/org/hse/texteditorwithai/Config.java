/**
 * Config предоставляет значения ширины и высоты по умолчанию для приложения.
 */
package org.hse.texteditorwithai;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties properties = new Properties();

    static {
        try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(inputStream);
        } catch (Exception e) {
            System.err.println("Error loading the settings file: " + e.getMessage());
        }
    }

    public static double getDefaultWidth() {
        return Double.parseDouble(properties.getProperty("defaultWidth"));
    }

    public static double getDefaultHeight() {
        return Double.parseDouble(properties.getProperty("defaultHeight"));
    }
}

