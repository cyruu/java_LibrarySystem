package subclasses.dbconnect;

import java.sql.*;

public class GetJdbcConnection {
    public static Connection getConnection() {
        Connection con = null;
        try {
            String urldb = "jdbc:mysql://localhost:3307/librarysystem";
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(urldb, "root", "root");

            // Statement st = con.createStatement();
        } catch (Exception e) {
            System.out.println(e);
        }
        return con;
    }
}
