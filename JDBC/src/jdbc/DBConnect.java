package jdbc;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnect {

    public Connection getInstance() throws ClassNotFoundException {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");

            // "jdbc:mysql://localhost:3306/sonoo","root","root"
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/derbyclient", "root", "root");
            System.out.println("got the conneciton ");

        } catch (SQLException ex) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

}
