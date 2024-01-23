package subclasses.book.borrow;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import subclasses.book.searchbook.SearchByAuthor;
import subclasses.book.viewbook.DisplayBookIsbn;
import subclasses.dbconnect.GetJdbcConnection;

public class BorrowPagination {
    public static void pageDisplayBorrowedBooks(int userId) {
        ArrayList<Integer> displayedBook = new ArrayList<>();
        try {

            // get book detail andgenre of title book
            String titleIsbnQuery = "select bookIsbn from bookborrows where userId=" + userId
                    + " order by bookIsbn asc limit "
                    + BorrowBook.limit + " offset "
                    + BorrowBook.offset;

            // String searchIsbnTitleQuery = "select
            // l.bookIsbn,l.bookTitle,l.bookPublished,l.bookCopies,g.genreName from
            // librarybooks l inner join bookgenres bg on l.bookIsbn=bg.bookIsbn inner join
            // genres g on bg.genreId=g.genreId where l.bookTitle like '%"
            // + searchTitle + "%'";
            Connection titleIsbnBookCon = GetJdbcConnection.getConnection();
            Statement titleIsbnBookSt = titleIsbnBookCon.createStatement();
            ResultSet titleIsbnBookRs = titleIsbnBookSt.executeQuery(titleIsbnQuery);
            // fetch one to check
            boolean bookFound = titleIsbnBookRs.next();
            if (bookFound) {
                System.out.println();

                System.out.println(
                        "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------------+------------------+");
                System.out.printf("\t | %-8s | %-32s | %-14s | %-24s | %-16s | %-16s | %-16s |%n", "  ISBN",
                        "             Title",
                        "  Published",
                        "         Authors", "     Genre", "   Borrow Date", "   Return Date");
                System.out.println(
                        "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------------+------------------+");
                // display first record
                // display all record of that isbn
                int isbn = titleIsbnBookRs.getInt(1);
                displayedBook.add(isbn);
                DisplayBookIsbn.displayBorrowBookOfIsbn(isbn, titleIsbnBookRs.isLast());

                // rremainig books
                while (titleIsbnBookRs.next()) {
                    isbn = titleIsbnBookRs.getInt(1);
                    displayedBook.add(isbn);
                    // isbn of books found
                    DisplayBookIsbn.displayBorrowBookOfIsbn(isbn, titleIsbnBookRs.isLast());

                }
                System.out.println(
                        "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------------+------------------+");
                // page no
                System.out.printf(" %75s|%d|%n", "", BorrowBook.page);
                // paged option
                // check current page
                String firstLastIsbnAllBooksquery = "select min(bookIsbn) as firstBook,max(bookIsbn) as lastBook from bookborrows where userId="
                        + userId + " order by bookIsbn asc";
                Connection firstLastIsbnAllBooksCon = GetJdbcConnection.getConnection();
                Statement firstLastIsbnAlBooksSt = firstLastIsbnAllBooksCon.createStatement();
                ResultSet firstLastIsbnAllBooksRs = firstLastIsbnAlBooksSt
                        .executeQuery(firstLastIsbnAllBooksquery);
                firstLastIsbnAllBooksRs.next();
                int firstBookIsbn = firstLastIsbnAllBooksRs.getInt("firstBook");
                int lastBookIsbn = firstLastIsbnAllBooksRs.getInt("lastBook");

                if (displayedBook.contains(firstBookIsbn)) {
                    BorrowBook.currentPage = "first";

                } else if (displayedBook.contains(lastBookIsbn)) {
                    BorrowBook.currentPage = "last";
                } else {
                    BorrowBook.currentPage = "middle";
                }

                displayedBook.clear();
                // close connection

                firstLastIsbnAllBooksCon.close();
            }
            // System.out.println(SearchByTitle.currentPage);
            titleIsbnBookCon.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
