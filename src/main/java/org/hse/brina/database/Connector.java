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
            String url = "jdbc:sqlite:" + Config.getPathToDB();
            connector = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "username TEXT NOT NULL," +
                    "password TEXT NOT NULL" +
                    ")";
            Statement statement = connector.createStatement();
            statement.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS user_documents (" +
                    "username TEXT NOT NULL," +
                    "filename TEXT NOT NULL," +
                    "file_path TEXT NOT NULL," +
                    "access CHAR(1)," +
                    "lock INTEGER," +
                    "PRIMARY KEY (username, filename)" +
                    ")";
            statement.executeUpdate(sql);
            System.out.println("Tables has been created");
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
    public static void main(String[] args) {
        //ExecuteCommands.main(args);
        connect();
    }
}