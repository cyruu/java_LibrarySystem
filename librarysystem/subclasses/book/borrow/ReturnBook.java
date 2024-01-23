package subclasses.book.borrow;

import java.util.Scanner;

import mainclass.MainClass;

import java.sql.*;
import subclasses.book.searchbook.SearchByIsbn;
import subclasses.dbconnect.GetJdbcConnection;

public class ReturnBook {
    public static void returnBook() {
        Scanner scanner = new Scanner(System.in);
        // System.out.println("return " + SearchByIsbn.selectedBookIsbn);
        try {
            Connection returnCon = GetJdbcConnection.getConnection();
            Statement returnSt = returnCon.createStatement();
            // userid
            String userQuery = "select userId from users where username='" + MainClass.getLoggedInUser() + "'";
            ResultSet userRs = returnSt.executeQuery(userQuery);
            userRs.next();
            int userId = userRs.getInt(1);
            // book title
            String titleQuery = "select bookTitle from librarybooks where bookIsbn=" + SearchByIsbn.selectedBookIsbn;
            ResultSet titleRs = returnSt.executeQuery(titleQuery);
            titleRs.next();
            String bookTitle = titleRs.getString(1);

            System.out.println("\nConfirm return book (" + bookTitle + ")? [y/n]!!");
            char confirm = scanner.next().charAt(0);
            // confirm return book
            if (confirm == 'y') {
                // increase copies by one
                String incCopiesQuery = "update librarybooks set bookCopies=bookCopies+1 where bookIsbn="
                        + SearchByIsbn.selectedBookIsbn;
                int inc = returnSt.executeUpdate(incCopiesQuery);
                // delete record from bookborrows where bookIsbn and userId
                String delRecQuery = "delete from bookborrows where userId=" + userId + " and bookIsbn="
                        + SearchByIsbn.selectedBookIsbn;
                int del = returnSt.executeUpdate(delRecQuery);

                System.out.println("\nSuccessfully returned book (" + bookTitle + ")!!");

            } else {
                System.out.println("\nFailed to return book! (" + bookTitle + ")!");
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

}
