package pims;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/pims" ;
    private static final String USER = "root";
    private static final String PASSWORD = System.getenv("PIMS_DB_PASSWORD");

    // METHOD TO CREATE DBS CONNECTION TO PIMS
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }
}
