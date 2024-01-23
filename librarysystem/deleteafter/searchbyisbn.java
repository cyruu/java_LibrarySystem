package subclasses.book.searchbook;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import subclasses.dbconnect.GetJdbcConnection;

public class SearchByIsbn {
    public static void searchByIsbn() {
        Scanner scannner = new Scanner(System.in);

        char retry = 'n';
        try {

            do {
                System.out.println("Enter ISBN of book: ");
                int isbn = scannner.nextInt();
                // get book detail andgenre of isbn
                String isbnBookQuery = "select l.bookTitle,l.bookPublished,l.bookCopies,g.genreName from librarybooks l inner join bookgenres bg on l.bookIsbn=bg.bookIsbn inner join genres g on bg.genreId=g.genreId where l.bookIsbn="
                        + isbn;
                Connection isbnBookCon = GetJdbcConnection.getConnection();
                Statement isbnBookSt = isbnBookCon.createStatement();
                ResultSet isbnBookRs = isbnBookSt.executeQuery(isbnBookQuery);
                boolean bookFound = isbnBookRs.next();
                // get author of isbn
                String isbnAuthorQuery = "select a.firstName,a.lastName from bookauthors ba inner join authors a on ba.authorId=a.authorId where bookIsbn="
                        + isbn;

                // Connection isbnAuthorCon = GetJdbcConnection.getConnection();
                Statement isbnAuthorSt = isbnBookCon.createStatement();
                ResultSet isbnAuthorRs = isbnAuthorSt.executeQuery(isbnAuthorQuery);
                isbnAuthorRs.next();
                if (bookFound) {
                    System.out.println(
                            "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                    System.out.printf("\t | %-8s | %-32s | %-14s | %-24s | %-16s | %-10s |%n", "  ISBN",
                            "             Title",
                            "  Published",
                            "         Authors", "     Genre", "  copies");
                    System.out.println(
                            "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                    String bookTitle = isbnBookRs.getString(1);
                    String bookPublished = isbnBookRs.getString(2);
                    int bookCopies = isbnBookRs.getInt(3);
                    String bookGenre = isbnBookRs.getString(4);
                    String authorFirstName = isbnAuthorRs.getString(1);
                    String authorLastName = isbnAuthorRs.getString(2);
                    // display
                    System.out.printf("\t | %-8s | %-32s | %-14s | %-12s%-12s | %-16s | %-10s |%n",
                            "  " + isbn,
                            " " + bookTitle,
                            "    " + bookPublished,
                            " " + authorFirstName, authorLastName, " " + bookGenre,
                            "    " + bookCopies);
                    // has many authors
                    String remAuthorQuery = "select a.firstName,a.lastName from bookAuthors ba inner join authors a on ba.authorId = a.authorId where ba.bookIsbn="
                            + isbn;
                    Statement remAuthorSt = isbnBookCon.createStatement();
                    ResultSet remAuthorRs = remAuthorSt.executeQuery(remAuthorQuery);
                    remAuthorRs.next();
                    while (remAuthorRs.next()) {
                        String remAuthorFirstName = remAuthorRs.getString(1);
                        String remAuthorLastName = remAuthorRs.getString(2);
                        System.out.printf(
                                "\t | %-8s | %-32s | %-14s | %-12s%-12s | %-16s | %-10s |%n",
                                "",
                                "",
                                "",
                                " " + remAuthorFirstName, remAuthorLastName, "", "");
                    }

                    System.out.println(
                            "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                } else {
                    System.out.println("Book with ISBN(" + isbn + ") not found!!!");
                    System.out.println();
                    System.out.println("Retry? [y/n]");
                    retry = scannner.next().charAt(0);

                }
                isbnBookCon.close();
            } while (retry == 'y');

        } catch (Exception e) {
            e.printStackTrace();
        } /// end of try

    }
}
