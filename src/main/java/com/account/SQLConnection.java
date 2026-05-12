package com.account;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import io.github.cdimascio.dotenv.Dotenv;
public class SQLConnection {

    // Step 1: Hold the one and only instance of itself
    private static SQLConnection sqlConn = null;

    // Step 2: Hold the actual database connection objects
    private Connection conn = null;
    private Statement st = null;

    // Step 3: Private constructor — no one can do "new SQLConnection()" from outside
    private SQLConnection() {
        Dotenv dotenv = Dotenv.load();
        String driver   = "com.mysql.cj.jdbc.Driver";
        String url      = dotenv.get("DB_URL");
        String dbname   = dotenv.get("DB_NAME");
        String username = dotenv.get("DB_USER");
        String pwd      = dotenv.get("DB_PASSWORD");

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url + dbname, username, pwd);
            st   = conn.createStatement();
            System.out.println("Database connected successfully!");
        } catch (Exception e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    // Step 4: Method to get the instance (synchronize : to prevent duplication connection)
    public static SQLConnection getInstance() {
        if (sqlConn == null) {
            sqlConn = new SQLConnection();
        }
        return sqlConn;
    }

    // Step 5: Give other classes access to the Statement to run queries
    public Statement getSQLConnection() {
        return st;
    }
}