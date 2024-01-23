package databaseauto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

import subclasses.dbconnect.GetJdbcConnection;

public class AddAuthorUniqueId {
    public static void main(String[] args) {

        int c = 0;
        try {

            Connection con = GetJdbcConnection.getConnection();
            // Statement stmt = con.createStatement();
            // String query = "insert into librarybooks(bookTitle,bookPublished,bookCopies)
            // values (?,?,?)";

            String query = "insert into authors(firstName,lastName) values (?,?)";
            PreparedStatement stmt = con.prepareStatement(query);

            String[] firstName = { "Laura" };
            String[] lastName = { "Smith" };

            for (int k = 0; k < firstName.length; k++) {
                stmt.setString(1, firstName[k]);
                stmt.setString(2, lastName[k]);
                int r = stmt.executeUpdate();
                c += r;
            }

        } catch (ArrayIndexOutOfBoundsException ae) {
        } catch (Exception e) {
            // TODO: handle exception

            e.printStackTrace();
        }
        System.out.println(c + " rows affected!!");
    }
}
