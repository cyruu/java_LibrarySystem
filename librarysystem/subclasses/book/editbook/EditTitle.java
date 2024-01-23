package subclasses.book.editbook;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import subclasses.book.searchbook.SearchByIsbn;
import subclasses.dbconnect.GetJdbcConnection;

public class EditTitle {
    public static void editTitle() {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("--------------");
        System.out.println("Edit title");
        System.out.println("--------------");
        System.out.println();
        System.out.println("Enter new title of book:");
        String newTitle = scanner.nextLine();
        try {
            // get old title of book using SearchByIsbn.selectedBookIsbn
            String OldBookQuery = "Select bookTitle from librarybooks where bookIsbn=" + SearchByIsbn.selectedBookIsbn;
            Connection editBookCon = GetJdbcConnection.getConnection();
            Statement oldBookTitleSt = editBookCon.createStatement();
            ResultSet oldBookTitleRs = oldBookTitleSt.executeQuery(OldBookQuery);
            oldBookTitleRs.next();
            String oldBookName = oldBookTitleRs.getString(1);
            System.out.println();
            System.out.println("Confirm change title: \"" + oldBookName + "\" to (" + newTitle + ") [y/n]");
            char confirmChange = scanner.next().charAt(0);
            System.out.println();
            if (confirmChange == 'y') {
                // change
                String updateTitleQuery = "update librarybooks set bookTitle='" + newTitle + "' where bookIsbn ="
                        + SearchByIsbn.selectedBookIsbn;
                Statement updateTitleSt = editBookCon.createStatement();
                int row = updateTitleSt.executeUpdate(updateTitleQuery);
                if (row == 1) {
                    System.out.println("Book title updated to (" + newTitle + ")");
                }
            } else {
                // not changed
                System.out.println("Failed to change title!!");
            }
            editBookCon.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
