package com.learning.atm_project.db;

import java.sql.*;

public class Connector {
    private String JDBC_DRIVER;
    private String DB_URL;
    private String DB_USER;
    private String DB_PASSWORD;

    private Connection connection;
    private Statement statement;

    public Connector(String driver, String url, String user, String password) {
        JDBC_DRIVER = driver;
        DB_URL = url;
        DB_USER = user;
        DB_PASSWORD = password;
    }

    ResultSet executeQuery(String query) throws SQLException {
        ResultSet resultSet = null;

        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            statement = connection.createStatement();

            resultSet = statement.executeQuery(query);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException ignored) {}
            try {
                if (connection != null) connection.close();
            } catch (SQLException ignored) {}
        }

        return resultSet;
    }
}
