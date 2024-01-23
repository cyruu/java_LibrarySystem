package subclasses.book.editbook;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import subclasses.book.searchbook.SearchByIsbn;
import subclasses.dbconnect.GetJdbcConnection;

public class EditPublishedYear {
    public static void editYear() {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("-------------------");
        System.out.println("Edit published year");
        System.out.println("-------------------");
        System.out.println();
        System.out.println("Enter new published year of book:");
        int newPublishedYear;
        // get valid year
        newPublishedYear = scanner.nextInt();
        while (!(newPublishedYear > 999 && newPublishedYear < 2024)) {
            System.out.println("Enter valid published year: ");
            newPublishedYear = scanner.nextInt();
        }

        try {
            // get old published year of book using SearchByIsbn.selectedBookIsbn
            String oldPublishedYear = "Select bookPublished from librarybooks where bookIsbn="
                    + SearchByIsbn.selectedBookIsbn;
            Connection editBookCon = GetJdbcConnection.getConnection();
            Statement oldPubYearSt = editBookCon.createStatement();
            ResultSet oldPubYearRs = oldPubYearSt.executeQuery(oldPublishedYear);
            oldPubYearRs.next();
            String oldPubYear = oldPubYearRs.getString(1);
            System.out.println();
            System.out.println("Confirm change published year : \"" + oldPubYear + "\" to (" +
                    newPublishedYear + ") [y/n]");
            char confirmChange = scanner.next().charAt(0);
            System.out.println();
            if (confirmChange == 'y') {
                // change
                String updatePubYearQuery = "update librarybooks set bookPublished='" + newPublishedYear +
                        "' where bookIsbn ="
                        + SearchByIsbn.selectedBookIsbn;
                Statement updatePubYearSt = editBookCon.createStatement();
                int row = updatePubYearSt.executeUpdate(updatePubYearQuery);
                if (row == 1) {
                    System.out.println("Book title updated to (" + newPublishedYear + ")");
                }
            } else {
                // not changed
                System.out.println("failed to change published year!!");
            }
            editBookCon.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
