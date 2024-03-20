package org.hse.brina.server;

import org.hse.brina.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private Connection connection;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started. Waiting for clients...");
            String url = "jdbc:sqlite:" + Config.getPathToJDBC();
            connection = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server(8080);
        server.start();
        //server.stop();
    }

    public void start() {
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostName());
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.startsWith("signInUser")) {
                        String[] userData = inputLine.split(" ");
                        if (userData.length == 3) {
                            String username = userData[1];
                            String password = userData[2];
                            boolean isUserExist = checkIsUserRegistered(username, password);
                            if (!isUserExist) {
                                out.println("User with this name not found");
                            } else {
                                boolean isPasswordCorrect = checkPassword(username, password);
                                if (isPasswordCorrect) {
                                    out.println("User logged in");
                                } else {
                                    out.println("Wrong password");
                                }
                            }

                        } else {
                            out.println("Invalid command format");
                        }
                    } else if (inputLine.startsWith("addUser")) {
                        String[] userData = inputLine.split(" ");
                        if (userData.length == 3) {
                            String username = userData[1];
                            String password = userData[2];
                            addUser(username, password);
                            out.println("New user added");
                        } else {
                            out.println("Invalid command format");
                        }
                    } else if (inputLine.startsWith("signUpUser")) {
                        String[] userData = inputLine.split(" ");
                        if (userData.length == 3) {
                            String username = userData[1];
                            String password = userData[2];
                            boolean isUserExist = checkIsUserRegistered(username, password);
                            if (isUserExist) {
                                out.println("User with the same name already exists");
                            } else {
                                addUser(username, password);
                                out.println("User is registered");
                            }
                        } else {
                            out.println("Invalid command format");
                        }
                    } else {
                        out.println("Unknown command");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                    out.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void addUser(String username, String password) {
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password);
                statement.executeUpdate();
                System.out.println("User added into db");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private boolean checkPassword(String username, String password) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();
                return resultSet.next();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        private boolean checkIsUserRegistered(String username, String password) {
            String sql = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                if (!resultSet.next()) {
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return true;
        }

        public void stop() {
            try {
                in.close();
                out.close();
                clientSocket.close();
                serverSocket.close();
                System.out.println("Server stopped");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}