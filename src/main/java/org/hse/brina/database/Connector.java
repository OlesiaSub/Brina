package org.hse.brina.database;
import org.hse.brina.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Connector {
    public static void connect() {
        Connection connector = null;
        try {
            String url = "jdbc:sqlite:" + Config.getPathToJDBC();
            connector = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "username TEXT NOT NULL," +
                    "password TEXT NOT NULL" +
                    ")";
            Statement statement = connector.createStatement();
            statement.executeUpdate(sql);
            System.out.println("Table has been created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connector != null) {
                    connector.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        //ExecuteCommands.main(args);
        connect();
    }
}