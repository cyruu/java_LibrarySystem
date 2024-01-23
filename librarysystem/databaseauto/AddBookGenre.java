package databaseauto;

import java.sql.*;
import java.util.Scanner;

import subclasses.dbconnect.GetJdbcConnection;

public class AddBookGenre {
    public static void main(String[] args) {
        try {

            Scanner scan = new Scanner(System.in);
            Connection con = GetJdbcConnection.getConnection();
            String preparedquery = "insert into bookgenres values (?,?)";
            PreparedStatement st = con.prepareStatement(preparedquery);
            do {

                System.out.println("Enter isbn of book: ");
                int bookIsbn = scan.nextInt();
                System.out.println("Enter genre id : ");
                int genreId = scan.nextInt();
                st.setInt(1, bookIsbn);
                st.setInt(2, genreId);
                int row = st.executeUpdate();
                System.out.println(row + " rows affected!!");
            } while (true);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }
}
