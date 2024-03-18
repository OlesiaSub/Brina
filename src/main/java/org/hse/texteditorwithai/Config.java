package org.hse.texteditorwithai;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.*;
import java.util.Properties;
import java.util.Scanner;

/**
 * Config предоставляет значения ширины и высоты по умолчанию для приложения, сохраняет путь до библиотеки sqlite-jdbc у пользователя
 */
public class Config {
    private static final Properties properties = new Properties();

    static {
        try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(inputStream);
            String filePath = "src/main/resources/config.txt";
            String scannedPathToJDBC = "";
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    scannedPathToJDBC = line;
                }
            } catch (IOException e) {
                System.out.println("Error while reading the file: it might be empty");
            }
            if(scannedPathToJDBC.isEmpty()){
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter the path to the Database: ");
                scannedPathToJDBC = scanner.nextLine();
                try (FileWriter writer = new FileWriter(filePath)) {
                    writer.write(scannedPathToJDBC);
                    File f = new File(filePath);
                    System.out.println(f.getAbsolutePath());
                } catch (IOException e) {
                    System.out.println("Error with config txt: "+e.getMessage());
                }
            }
            properties.setProperty("pathToJDBC", scannedPathToJDBC);
            FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/config.properties");
            properties.store(fileOutputStream, null);
            fileOutputStream.close();
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

    public static String getPathToJDBC() {
        return properties.getProperty("pathToJDBC");
    }
}

