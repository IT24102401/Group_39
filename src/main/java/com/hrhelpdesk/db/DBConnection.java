package com.hrhelpdesk.db;

import jakarta.servlet.ServletContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=HRHelpDesk;encrypt=false";
    private static final String USER = "sa";
    private static final String PASS = "amila2004A9";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("JDBC Driver not found", e);
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static Connection getConnection(ServletContext context) throws SQLException {

        return getConnection();
    }
}