package org.example;

import java.sql.*;

public class DatabaseHandler {
    final String DB_URL = "jdbc:mysql://localhost/cities";
    final String USER = "root";
    final String PASSWORD = "";
    Connection conn;
    Statement stmt = null;

    DatabaseHandler() {

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    String selectAll() {
        String result = "[";
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from cities");
            while(rs.next())
                result += "{\"id\":" + rs.getInt(1) +
                        ", \"name\":" + rs.getString(2) +
                        ", \"temp\":" + rs.getDouble(3) +
                        " }, ";
        }catch(Exception e){
            System.out.println(e);
        }
        result += "]";
        System.out.println(result);
        return result;
    }

    String sort_by_temp() {
        String result = "[";
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from cities ORDER BY temp");
            while(rs.next())
                result += "{\"id\":" + rs.getInt(1) +
                        ", \"name\":" + rs.getString(2) +
                        ", \"temp\":" + rs.getDouble(3) +
                        " }, ";
        }catch(Exception e){
            System.out.println(e);
        }
        result += "]";
        System.out.println(result);
        return result;
    }
}
