package subclasses.book.editbook;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import subclasses.book.searchbook.SearchByIsbn;
import subclasses.dbconnect.GetJdbcConnection;

public class EditCopies {
    public static void editCopies() {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("----------------");
        System.out.println("Edit copies");
        System.out.println("----------------");
        System.out.println();
        System.out.println("Enter new copies of book:");
        int newCopies;
        newCopies = scanner.nextInt();

        try {
            // get old published year of book using SearchByIsbn.selectedBookIsbn
            String oldCopiesQuery = "Select bookCopies from librarybooks where bookIsbn="
                    + SearchByIsbn.selectedBookIsbn;
            Connection editBookCon = GetJdbcConnection.getConnection();
            Statement oldCopiesSt = editBookCon.createStatement();
            ResultSet oldCopiesRs = oldCopiesSt.executeQuery(oldCopiesQuery);
            oldCopiesRs.next();
            int oldCopies = oldCopiesRs.getInt(1);
            System.out.println();
            System.out.println("Confirm change copies : \"" + oldCopies + "\" to (" +
                    newCopies + ") [y/n]");
            char confirmChange = scanner.next().charAt(0);
            System.out.println();
            if (confirmChange == 'y') {
                // change
                String updateCopiesQuery = "update librarybooks set bookCopies='" + newCopies +
                        "' where bookIsbn ="
                        + SearchByIsbn.selectedBookIsbn;
                Statement updateCopiesSt = editBookCon.createStatement();
                int row = updateCopiesSt.executeUpdate(updateCopiesQuery);
                if (row == 1) {
                    System.out.println("Book copies updated to (" + newCopies + ")");
                }
            } else {
                // not changed
                System.out.println("failed to change title");
            }
            editBookCon.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
