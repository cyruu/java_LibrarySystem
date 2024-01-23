package databaseauto;

import java.sql.*;
import java.util.ArrayList;

import subclasses.dbconnect.GetJdbcConnection;
import java.sql.*;

public class AddUniqueIsbn {

    public static void main(String[] args) {
        int offsetValue = 0;
        int c = 0;
        try {

            Connection con = GetJdbcConnection.getConnection();
            // Statement stmt = con.createStatement();
            // String query = "insert into librarybooks(bookTitle,bookPublished,bookCopies)
            // values (?,?,?)";

            String query = "insert into librarybooks values (?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(query);
            // stmt to insert in tempindex table
            String preparedQuery = "insert into tempindex values (?)";
            PreparedStatement pst = con.prepareStatement(preparedQuery);
            String[] bookList = { "Pride and Prejudice", "The Great Expectations", };
            int[] publishedList = { 1813, 1861 };
            int isbn;
            ArrayList<Integer> databseIsbn = new ArrayList<>();
            Statement st = con.createStatement();
            boolean continueFinding = true;
            // for all books in bookList array
            for (int k = 0; k < bookList.length; k++) {

                // get isbn not repeatand less than 500
                do {

                    // clear arraylist
                    databseIsbn.clear();
                    ResultSet rs = st.executeQuery("select * from tempindex");
                    // fetch all existed index and add to array
                    while (rs.next()) {

                        databseIsbn.add(rs.getInt(1));
                    }

                    // generate random number less than 500
                    isbn = (int) Math.floor(Math.random() * 398);
                    isbn += 102;
                    int databaseOneIsbn;
                    int i = 0;
                    for (i = 0; i < databseIsbn.size(); i++) {
                        databaseOneIsbn = databseIsbn.get(i);
                        // existed isbn
                        if (databaseOneIsbn == isbn) {
                            continueFinding = true;
                            break;
                        }
                    }

                    // not found
                    if (i == databseIsbn.size())

                    {
                        continueFinding = false;
                        // add index to db

                    }
                } while (continueFinding);

                // got isbn
                stmt.setInt(1, isbn);
                stmt.setString(2, bookList[k]);
                stmt.setInt(3, publishedList[k]);
                // get random copies less than 5
                int copies;
                copies = (int) Math.floor(Math.random() * 3);
                copies += 2;
                stmt.setInt(4, copies);

                int rows = stmt.executeUpdate();
                c += rows;
                // add isbn to tempindex table

                pst.setInt(1, isbn);
                pst.executeUpdate();

            }

        } catch (ArrayIndexOutOfBoundsException ae) {
        } catch (Exception e) {
            // TODO: handle exception

            e.printStackTrace();
        }
        System.out.println(c + " rows affected!!");
    }

}
