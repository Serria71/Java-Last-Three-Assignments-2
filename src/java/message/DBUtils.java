/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Serria71
 */
public class DBUtils {
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        String hostname = "localhost";
        String port = "3306";
        String dbname = "test";
        String username = "root";
        String password = "";
        String jdbc = String.format("jdbc:mysql://%s:%s/%s", hostname, port, dbname);
        return DriverManager.getConnection(jdbc, username, password);
    }
}
