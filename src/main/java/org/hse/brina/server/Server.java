package org.hse.brina.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hse.brina.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.Map;
import java.util.HashMap;

public class Server {
    private static final Logger logger = LogManager.getLogger();
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private Connection connection;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            logger.info("Server started. Waiting for clients...");
            String url = "jdbc:sqlite:" + Config.getPathToDB();
            connection = DriverManager.getConnection(url);
            logger.info("Connection to SQLite has been established.");
        } catch (IOException | SQLException e) {
            logger.error(e.getMessage());
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
                logger.info("Client connected: " + clientSocket.getInetAddress().getHostName());
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
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
                    } else if (inputLine.startsWith("getDocuments")) {
                        String[] userData = inputLine.split(" ");
                        if (userData.length == 2) {
                            String username = userData[1];
                            Map<String, String> userDocuments = getUserDocuments(username);
                            StringBuilder stringBuilder = new StringBuilder();
                            for (Map.Entry<String, String> entry : userDocuments.entrySet()) {
                                stringBuilder.append(entry.getKey()).append(" ").append(entry.getValue()).append(" ");
                            }
                            out.println(stringBuilder.toString().trim());
                        } else {
                            out.println("Invalid command format");
                        }
                    } else if (inputLine.startsWith("saveDocument")) {
                        String[] userData = inputLine.split(" ");
                        if (userData.length == 3) {
                            String fileName = userData[1];
                            String username = userData[2];
                            addDocument(fileName, username);
                            out.println("Document saved");
                        } else {
                            out.println("Invalid command format");
                        }
                    } else {
                        out.println("Unknown command");
                    }
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            } finally {
                try {
                    in.close();
                    out.close();
                    clientSocket.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }

        private void addUser(String username, String password) {
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password);
                statement.executeUpdate();
                logger.info("User added into db");
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }

        private void addDocument(String filename, String username) {
            String sql = "INSERT INTO user_documents (username, filename, file_path, access, lock) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, filename);
                statement.setString(3, Config.getProjectPath().substring(0, 25) + "documents/" + filename);
                statement.setString(4, "w");
                statement.setInt(5, 0);
                statement.executeUpdate();
                logger.info("User added into db");
            } catch (SQLException e) {
                logger.error(e.getMessage());
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
                logger.error(e.getMessage());
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
                logger.error(e.getMessage());
            }
            return true;
        }

        private Map<String, String> getUserDocuments(String username) {
            Map<String, String> documentsMap = new HashMap<>();
            String sql = "SELECT filename, file_path FROM user_documents WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String filename = resultSet.getString("filename");
                    String filePath = resultSet.getString("file_path");
                    documentsMap.put(filename, filePath);
                }
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
            return documentsMap;
        }


        public void stop() {
            try {
                in.close();
                out.close();
                clientSocket.close();
                serverSocket.close();
                logger.info("Server stopped");
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }


    }
}