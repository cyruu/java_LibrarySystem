package subclasses.book.searchbook;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import subclasses.book.viewbook.DisplayBookIsbn;
import subclasses.dbconnect.GetJdbcConnection;

public class SearchPagination {
    public static void pageSearchByTitle(String searchTitle) {
        ArrayList<Integer> displayedBook = new ArrayList<>();
        try {

            // get book detail andgenre of title book
            String titleIsbnQuery = "select bookIsbn from librarybooks where bookTitle like '%"
                    + searchTitle + "%' order by bookIsbn asc limit "
                    + SearchByTitle.limit + " offset "
                    + SearchByTitle.offset;

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
            if (bookFound && !searchTitle.equals("")) {
                System.out.println();
                System.out.printf("%64sYou searched (" + searchTitle + ")%n", "");
                System.out.println(
                        "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                System.out.printf("\t | %-8s | %-32s | %-14s | %-24s | %-16s | %-10s |%n", "  ISBN",
                        "             Title",
                        "  Published",
                        "         Authors", "     Genre", "  copies");
                System.out.println(
                        "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                // display first record
                // display all record of that isbn
                int isbn = titleIsbnBookRs.getInt(1);
                displayedBook.add(isbn);
                DisplayBookIsbn.displayBookOfIsbn(isbn, titleIsbnBookRs.isLast());

                // rremainig books
                while (titleIsbnBookRs.next()) {
                    isbn = titleIsbnBookRs.getInt(1);
                    displayedBook.add(isbn);
                    // isbn of books found
                    DisplayBookIsbn.displayBookOfIsbn(isbn, titleIsbnBookRs.isLast());

                }
                System.out.println(
                        "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                // page no
                System.out.printf(" %70s|%d|%n", "", SearchByTitle.page);
                // paged option
                // check current page
                String firstLastIsbnAllBooksquery = "select min(bookIsbn) as firstBook,max(bookIsbn) as lastBook from librarybooks where bookTitle like '%"
                        + searchTitle + "%' order by bookIsbn asc";
                Connection firstLastIsbnAllBooksCon = GetJdbcConnection.getConnection();
                Statement firstLastIsbnAlBooksSt = firstLastIsbnAllBooksCon.createStatement();
                ResultSet firstLastIsbnAllBooksRs = firstLastIsbnAlBooksSt
                        .executeQuery(firstLastIsbnAllBooksquery);
                firstLastIsbnAllBooksRs.next();
                int firstBookIsbn = firstLastIsbnAllBooksRs.getInt("firstBook");
                int lastBookIsbn = firstLastIsbnAllBooksRs.getInt("lastBook");

                if (displayedBook.contains(firstBookIsbn)) {
                    SearchByTitle.currentPage = "first";

                } else if (displayedBook.contains(lastBookIsbn)) {
                    SearchByTitle.currentPage = "last";
                } else {
                    SearchByTitle.currentPage = "middle";
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

    // search by genre
    public static void pageSearchByGenre(int genreChoice, String genreName) {
        ArrayList<Integer> displayedBook = new ArrayList<>();
        try {

            // get book detail andgenre of title book
            String titleIsbnQuery = "select bookIsbn from bookgenres where genreId=" + genreChoice
                    + " order by bookIsbn asc limit "
                    + SearchByGenre.limit + " offset "
                    + SearchByGenre.offset;

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
                System.out.printf("%64sYou searched (" + genreName + ")%n", "");
                System.out.println(
                        "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                System.out.printf("\t | %-8s | %-32s | %-14s | %-24s | %-16s | %-10s |%n", "  ISBN",
                        "             Title",
                        "  Published",
                        "         Authors", "     Genre", "  copies");
                System.out.println(
                        "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                // display first record
                // display all record of that isbn
                int isbn = titleIsbnBookRs.getInt(1);
                displayedBook.add(isbn);
                DisplayBookIsbn.displayBookOfIsbn(isbn, titleIsbnBookRs.isLast());

                // rremainig books
                while (titleIsbnBookRs.next()) {
                    isbn = titleIsbnBookRs.getInt(1);
                    displayedBook.add(isbn);
                    // isbn of books found
                    DisplayBookIsbn.displayBookOfIsbn(isbn, titleIsbnBookRs.isLast());

                }
                System.out.println(
                        "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                // page no
                System.out.printf(" %70s|%d|%n", "", SearchByGenre.page);
                // paged option
                // check current page
                String firstLastIsbnAllBooksquery = "select min(bookIsbn) as firstBook,max(bookIsbn) as lastBook from bookgenres where genreId="
                        + genreChoice + " order by bookIsbn asc";
                Connection firstLastIsbnAllBooksCon = GetJdbcConnection.getConnection();
                Statement firstLastIsbnAlBooksSt = firstLastIsbnAllBooksCon.createStatement();
                ResultSet firstLastIsbnAllBooksRs = firstLastIsbnAlBooksSt
                        .executeQuery(firstLastIsbnAllBooksquery);
                firstLastIsbnAllBooksRs.next();
                int firstBookIsbn = firstLastIsbnAllBooksRs.getInt("firstBook");
                int lastBookIsbn = firstLastIsbnAllBooksRs.getInt("lastBook");

                if (displayedBook.contains(firstBookIsbn)) {
                    SearchByGenre.currentPage = "first";

                } else if (displayedBook.contains(lastBookIsbn)) {
                    SearchByGenre.currentPage = "last";
                } else {
                    SearchByGenre.currentPage = "middle";
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

    // search by published year
    public static void pageSearchByPublishedYear(int searchPublishedYear) {
        ArrayList<Integer> displayedBook = new ArrayList<>();
        try {

            // get book detail andgenre of title book
            String titleIsbnQuery = "select bookIsbn from librarybooks where bookPublished="
                    + searchPublishedYear
                    + " order by bookIsbn asc limit "
                    + SearchByPublishedYear.limit + " offset "
                    + SearchByPublishedYear.offset;

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
                System.out.printf("%64sYou searched (" + searchPublishedYear + ")%n", "");
                System.out.println(
                        "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                System.out.printf("\t | %-8s | %-32s | %-14s | %-24s | %-16s | %-10s |%n", "  ISBN",
                        "             Title",
                        "  Published",
                        "         Authors", "     Genre", "  copies");
                System.out.println(
                        "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                // display first record
                // display all record of that isbn
                int isbn = titleIsbnBookRs.getInt(1);
                displayedBook.add(isbn);
                DisplayBookIsbn.displayBookOfIsbn(isbn, titleIsbnBookRs.isLast());

                // rremainig books
                while (titleIsbnBookRs.next()) {
                    isbn = titleIsbnBookRs.getInt(1);
                    displayedBook.add(isbn);
                    // isbn of books found
                    DisplayBookIsbn.displayBookOfIsbn(isbn, titleIsbnBookRs.isLast());

                }
                System.out.println(
                        "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                // page no
                System.out.printf(" %70s|%d|%n", "", SearchByPublishedYear.page);
                // paged option
                // check current page
                String firstLastIsbnAllBooksquery = "select min(bookIsbn) as firstBook,max(bookIsbn) as lastBook from librarybooks where bookPublished="
                        + searchPublishedYear + " order by bookIsbn asc";
                Connection firstLastIsbnAllBooksCon = GetJdbcConnection.getConnection();
                Statement firstLastIsbnAlBooksSt = firstLastIsbnAllBooksCon.createStatement();
                ResultSet firstLastIsbnAllBooksRs = firstLastIsbnAlBooksSt
                        .executeQuery(firstLastIsbnAllBooksquery);
                firstLastIsbnAllBooksRs.next();
                int firstBookIsbn = firstLastIsbnAllBooksRs.getInt("firstBook");
                int lastBookIsbn = firstLastIsbnAllBooksRs.getInt("lastBook");

                if (displayedBook.contains(firstBookIsbn)) {
                    SearchByPublishedYear.currentPage = "first";

                } else if (displayedBook.contains(lastBookIsbn)) {
                    SearchByPublishedYear.currentPage = "last";
                } else {
                    SearchByPublishedYear.currentPage = "middle";
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

    // search by author name
    public static void pageSearchByAuthor(String searchAuthor) {
        ArrayList<Integer> displayedBook = new ArrayList<>();
        try {

            // get book detail andgenre of title book
            String titleIsbnQuery = "select distinct(ba.bookIsbn) from bookauthors ba inner join authors a on ba.authorId=a.authorId where a.firstName like '%"
                    + searchAuthor + "%' or a.lastName like '%" + searchAuthor + "%' order by ba.bookIsbn asc limit "
                    + SearchByAuthor.limit + " offset "
                    + SearchByAuthor.offset;

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
                System.out.printf("%64sYou searched (" + searchAuthor + ")%n", "");
                System.out.println(
                        "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                System.out.printf("\t | %-8s | %-32s | %-14s | %-24s | %-16s | %-10s |%n", "  ISBN",
                        "             Title",
                        "  Published",
                        "         Authors", "     Genre", "  copies");
                System.out.println(
                        "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                // display first record
                // display all record of that isbn
                int isbn = titleIsbnBookRs.getInt(1);
                displayedBook.add(isbn);
                DisplayBookIsbn.displayBookOfIsbn(isbn, titleIsbnBookRs.isLast());

                // rremainig books
                while (titleIsbnBookRs.next()) {
                    isbn = titleIsbnBookRs.getInt(1);
                    displayedBook.add(isbn);
                    // isbn of books found
                    DisplayBookIsbn.displayBookOfIsbn(isbn, titleIsbnBookRs.isLast());

                }
                System.out.println(
                        "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                // page no
                System.out.printf(" %70s|%d|%n", "", SearchByAuthor.page);
                // paged option
                // check current page
                String firstLastIsbnAllBooksquery = "select min(ba.bookIsbn) as firstBook,max(ba.bookIsbn) as lastBook from bookauthors ba inner join authors a on ba.authorId=a.authorId where a.firstName like '%"
                        + searchAuthor + "%' or a.lastName like '%" + searchAuthor + "%' order by ba.bookIsbn asc";
                Connection firstLastIsbnAllBooksCon = GetJdbcConnection.getConnection();
                Statement firstLastIsbnAlBooksSt = firstLastIsbnAllBooksCon.createStatement();
                ResultSet firstLastIsbnAllBooksRs = firstLastIsbnAlBooksSt
                        .executeQuery(firstLastIsbnAllBooksquery);
                firstLastIsbnAllBooksRs.next();
                int firstBookIsbn = firstLastIsbnAllBooksRs.getInt("firstBook");
                int lastBookIsbn = firstLastIsbnAllBooksRs.getInt("lastBook");

                if (displayedBook.contains(firstBookIsbn)) {
                    SearchByAuthor.currentPage = "first";

                } else if (displayedBook.contains(lastBookIsbn)) {
                    SearchByAuthor.currentPage = "last";
                } else {
                    SearchByAuthor.currentPage = "middle";
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