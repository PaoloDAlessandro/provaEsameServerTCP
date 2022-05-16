package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    Database() {
        String DB_URL = "jdbc:mysql://localhost/";
        final String USER = "root";
        final String PASSWORD = "";
        String sql;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Statement stmt = null;

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            sql = "CREATE DATABASE cities";
            stmt.executeUpdate(sql);
            System.out.println("Database created successfully...");
        } catch (SQLException e) {
            System.out.println("Database already exists");
        }

        DB_URL = "jdbc:mysql://localhost/cities";

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            sql = "CREATE TABLE `prova` (`id` int(11) NOT NULL, `name` varchar(200) NOT NULL, `temp` double NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
            stmt.executeQuery(sql);
            System.out.println("Database created successfully...");

        } catch (SQLException e) {
            System.out.println("Table already exists");
        }
    }
}
