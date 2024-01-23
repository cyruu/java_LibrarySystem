package subclasses.book.editbook;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import subclasses.book.searchbook.SearchByIsbn;
import subclasses.dbconnect.GetJdbcConnection;
import subclasses.validinputgetter.GetValidInput;

public class EditGenre {
    public static void editGenre() {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("--------------");
        System.out.println("Edit genre");
        System.out.println("--------------");
        System.out.println();
        System.out.println("Select new genre of book:");

        try {
            // display genre list
            String allGenreQuery = "select * from genres";

            Connection editBookCon = GetJdbcConnection.getConnection();
            // Connection searchGenreCon = GetJdbcConnection.getConnection();
            Statement allGenreSt = editBookCon.createStatement();
            ResultSet allGenreRs = allGenreSt.executeQuery(allGenreQuery);
            int genreCount = 0;
            while (allGenreRs.next()) {
                genreCount++;
                int genreId = allGenreRs.getInt(1);
                String genreName = allGenreRs.getString(2);
                System.out.printf("\t" + genreId + ". " + genreName + "\t");
                if (genreCount % 3 == 0) {
                    System.out.println();
                }
            }
            System.out.println("\n");
            System.out.println("Choose genre of book: ");

            int newGenreId = GetValidInput.getValidInput(genreCount);
            // get old title of book using SearchByIsbn.selectedBookIsbn
            String OldGenreQuery = "select g.genreName from bookgenres bg inner join genres g on bg.genreId=g.genreId where bg.bookIsbn="
                    + SearchByIsbn.selectedBookIsbn;
            Statement oldGenreSt = editBookCon.createStatement();
            ResultSet oldGenreRs = oldGenreSt.executeQuery(OldGenreQuery);
            oldGenreRs.next();
            String oldGenreName = oldGenreRs.getString(1);
            // new genre name
            String newGenreQuery = "select genreName from genres where genreId=" + newGenreId;
            Statement newGenreSt = editBookCon.createStatement();
            ResultSet newGenreRs = newGenreSt.executeQuery(newGenreQuery);
            newGenreRs.next();
            String newGenreName = newGenreRs.getString(1);

            System.out.println();
            System.out.println("Confirm change genre: \"" + oldGenreName + "\" to (" + newGenreName + ") [y/n]");
            char confirmChange = scanner.next().charAt(0);
            System.out.println();
            if (confirmChange == 'y') {
                // change
                String updateGenreQuery = "update bookgenres set genreId='" + newGenreId + "' where bookIsbn ="
                        + SearchByIsbn.selectedBookIsbn;
                Statement updateTitleSt = editBookCon.createStatement();
                int row = updateTitleSt.executeUpdate(updateGenreQuery);
                if (row == 1) {
                    System.out.println("Book genre updated to (" + newGenreName + ")");
                }
            } else {
                // not changed
                System.out.println("Failed to change genre!!");
            }
            editBookCon.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
