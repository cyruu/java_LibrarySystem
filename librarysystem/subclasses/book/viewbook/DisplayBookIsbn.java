package subclasses.book.viewbook;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import mainclass.MainClass;
import subclasses.dbconnect.GetJdbcConnection;

public class DisplayBookIsbn {

        public static void displayBookOfIsbn(int isbn, boolean isBookLast) {
                try {
                        boolean remauthor = false;
                        // book info
                        String isbnBookQuery = "select l.bookTitle,l.bookPublished,l.bookCopies,g.genreName from librarybooks l inner join bookgenres bg on l.bookIsbn=bg.bookIsbn inner join genres g on bg.genreId=g.genreId where l.bookIsbn="
                                        + isbn;
                        Connection isbnBookCon = GetJdbcConnection.getConnection();
                        Statement isbnBookSt = isbnBookCon.createStatement();
                        ResultSet isbnBookRs = isbnBookSt.executeQuery(isbnBookQuery);
                        isbnBookRs.next();
                        // get author of isbn
                        String isbnAuthorQuery = "select a.firstName,a.lastName from bookauthors ba inner join authors a on ba.authorId=a.authorId where bookIsbn="
                                        + isbn;

                        // Connection isbnAuthorCon = GetJdbcConnection.getConnection();
                        Statement isbnAuthorSt = isbnBookCon.createStatement();
                        ResultSet isbnAuthorRs = isbnAuthorSt.executeQuery(isbnAuthorQuery);
                        isbnAuthorRs.next();
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
                                remauthor = true;
                                String remAuthorFirstName = remAuthorRs.getString(1);
                                String remAuthorLastName = remAuthorRs.getString(2);
                                System.out.printf(
                                                "\t | %-8s | %-32s | %-14s | %-12s%-12s | %-16s | %-10s |%n",
                                                "",
                                                "",
                                                "",
                                                " " + remAuthorFirstName, remAuthorLastName, "", "");

                        }
                        if (remauthor && !isBookLast) {
                                System.out.println(
                                                "\t |----------|----------------------------------|----------------|--------------------------|------------------|------------|");
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                        // TODO: handle exception
                }
        }

        // display borrowed books according to isbn
        public static void displayBorrowBookOfIsbn(int isbn, boolean isBookLast) {
                try {
                        boolean remauthor = false;
                        // book info
                        String isbnBookQuery = "select l.bookTitle,l.bookPublished,g.genreName from librarybooks l inner join bookgenres bg on l.bookIsbn=bg.bookIsbn inner join genres g on bg.genreId=g.genreId where l.bookIsbn="
                                        + isbn;
                        Connection isbnBookCon = GetJdbcConnection.getConnection();
                        Statement isbnBookSt = isbnBookCon.createStatement();
                        ResultSet isbnBookRs = isbnBookSt.executeQuery(isbnBookQuery);
                        isbnBookRs.next();
                        // get userId
                        String userQuery = "select userId from users where username='" + MainClass.getLoggedInUser()
                                        + "'";
                        Statement userSt = isbnBookCon.createStatement();
                        ResultSet userRs = userSt.executeQuery(userQuery);
                        userRs.next();
                        int userId = userRs.getInt(1);
                        // get author of isbn
                        String isbnAuthorQuery = "select a.firstName,a.lastName from bookauthors ba inner join authors a on ba.authorId=a.authorId where bookIsbn="
                                        + isbn;

                        // Connection isbnAuthorCon = GetJdbcConnection.getConnection();
                        Statement isbnAuthorSt = isbnBookCon.createStatement();
                        ResultSet isbnAuthorRs = isbnAuthorSt.executeQuery(isbnAuthorQuery);
                        isbnAuthorRs.next();
                        String bookTitle = isbnBookRs.getString(1);
                        String bookPublished = isbnBookRs.getString(2);

                        String bookGenre = isbnBookRs.getString(3);
                        String authorFirstName = isbnAuthorRs.getString(1);
                        String authorLastName = isbnAuthorRs.getString(2);
                        // get borrow and reutirn date of the book
                        String datequery = "SELECT cast(borrowDate AS DATE), cast(returnDate AS DATE) from bookborrows where userId="
                                        + userId + " and bookIsbn=" + isbn;
                        ResultSet dateRs = isbnAuthorSt.executeQuery(datequery);
                        dateRs.next();
                        String borrowDate = dateRs.getString(1);
                        String returnDate = dateRs.getString(2);
                        // display
                        System.out.printf("\t | %-8s | %-32s | %-14s | %-12s%-12s | %-16s | %-16s | %-16s |%n",
                                        "  " + isbn,
                                        " " + bookTitle,
                                        "    " + bookPublished,
                                        " " + authorFirstName, authorLastName, " " + bookGenre, "  " + borrowDate,
                                        "  " + returnDate);
                        // has many authors
                        String remAuthorQuery = "select a.firstName,a.lastName from bookAuthors ba inner join authors a on ba.authorId = a.authorId where ba.bookIsbn="
                                        + isbn;
                        Statement remAuthorSt = isbnBookCon.createStatement();
                        ResultSet remAuthorRs = remAuthorSt.executeQuery(remAuthorQuery);
                        remAuthorRs.next();

                        while (remAuthorRs.next()) {
                                remauthor = true;
                                String remAuthorFirstName = remAuthorRs.getString(1);
                                String remAuthorLastName = remAuthorRs.getString(2);
                                System.out.printf(
                                                "\t | %-8s | %-32s | %-14s | %-12s%-12s | %-16s | %-16s | %-16s |%n",
                                                "",
                                                "",
                                                "",
                                                " " + remAuthorFirstName, remAuthorLastName, "", "", "", "");

                        }
                        if (remauthor && !isBookLast) {
                                System.out.println(
                                                "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------------+------------------+");
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                        // TODO: handle exception
                }

        }
}
