package subclasses.book.viewbook;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import subclasses.dbconnect.GetJdbcConnection;

public class BookPagination {

        public static void viewPagedBooks() {

                // get all records of books from librarybooks,authors and genres
                try {

                        String allRecordQuery = "select bookISbn,bookTitle,bookPublished,bookCopies from librarybooks order by bookIsbn asc limit "
                                        + ViewBook.limitValue + " offset "
                                        + ViewBook.offset;
                        Connection allRecordsCon = GetJdbcConnection.getConnection();
                        Statement allRecordsSt = allRecordsCon.createStatement();
                        ResultSet allRecordsRs = allRecordsSt.executeQuery(allRecordQuery);
                        System.out.println(
                                        "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                        System.out.printf("\t | %-8s | %-32s | %-14s | %-24s | %-16s | %-10s |%n", "  ISBN",
                                        "             Title",
                                        "  Published",
                                        "         Authors", "     Genre", "  copies");
                        System.out.println(
                                        "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                        boolean currentBookChecked = false;
                        // for displaying 10 books
                        int bookCount = 0;
                        while (allRecordsRs.next()) {
                                int rowIsbn = allRecordsRs.getInt(1);
                                String rowBookTitle = allRecordsRs.getString(2);
                                int rowBookPublished = allRecordsRs.getInt(3);
                                int rowBookCopies = allRecordsRs.getInt(4);

                                // add this isbn to fetched isbn arraylist
                                String getGenreQuery = "select g.genreName from bookgenres bg inner join genres g on bg.genreId = g.genreId where bg.bookIsbn="
                                                + rowIsbn;
                                Connection getGenreCon = GetJdbcConnection.getConnection();
                                Statement getGenreStatement = getGenreCon.createStatement();
                                ResultSet getGenreRs = getGenreStatement.executeQuery(getGenreQuery);
                                getGenreRs.next();
                                String rowGenreName = getGenreRs.getString(1);
                                // add unique isbn book to the arraylist

                                // increase book count after displaying
                                bookCount++;
                                // authors
                                String getAuthorQuery = "select a.firstName,a.lastName from bookauthors ba inner join authors a on ba.authorId=a.authorId where ba.bookIsbn="
                                                + rowIsbn;
                                Connection getAuthorsCon = GetJdbcConnection.getConnection();
                                Statement getAuthorSt = getAuthorsCon.createStatement();
                                ResultSet getAuthorRs = getAuthorSt.executeQuery(getAuthorQuery);
                                getAuthorRs.next();
                                String rowAutorFirstName = getAuthorRs.getString(1);
                                String rowAutorLastName = getAuthorRs.getString(2);
                                // diplay all information
                                System.out.printf("\t | %-8s | %-32s | %-14s | %-12s%-12s | %-16s | %-10s |%n",
                                                "  " + rowIsbn,
                                                " " + rowBookTitle,
                                                "    " + rowBookPublished,
                                                " " + rowAutorFirstName, rowAutorLastName, " " + rowGenreName,
                                                "    " + rowBookCopies);

                                // if book has many authors then only display authors name
                                String countAuthorQuery = "select count(*) as count from bookauthors ba inner join authors a on ba.authorId=a.authorId where ba.bookIsbn="
                                                + rowIsbn;
                                Connection countAuthorCon = GetJdbcConnection.getConnection();
                                Statement countAuthorStatement = countAuthorCon.createStatement();
                                ResultSet countAuthorRs = countAuthorStatement.executeQuery(countAuthorQuery);
                                countAuthorRs.next();
                                int authorcount = countAuthorRs.getInt("count");

                                // for fetch name of authors where there are more than one authors
                                if (authorcount > 1) {
                                        String remainingAuthorQuery = "select a.firstName,a.lastName from librarybooks l inner join bookauthors ba on l.bookIsbn=ba.bookIsbn inner join authors a on ba.authorId=a.authorId where l.bookIsbn="
                                                        + rowIsbn;
                                        Connection remainingAuthorsCon = GetJdbcConnection.getConnection();
                                        Statement remainingAuthorSt = remainingAuthorsCon.createStatement();
                                        ResultSet remainingAuthorRs = remainingAuthorSt
                                                        .executeQuery(remainingAuthorQuery);
                                        remainingAuthorRs.next();
                                        while (remainingAuthorRs.next()) {
                                                String authorFirstName = remainingAuthorRs.getString(1);
                                                String authorLastName = remainingAuthorRs.getString(2);
                                                System.out.printf(
                                                                "\t | %-8s | %-32s | %-14s | %-12s%-12s | %-16s | %-10s |%n",
                                                                "",
                                                                "",
                                                                "",
                                                                " " + authorFirstName, authorLastName, "", "");
                                        }
                                        if (!(bookCount == ViewBook.limitValue)) {
                                                System.out.println(
                                                                "\t |----------|----------------------------------|----------------|--------------------------|------------------|------------|");
                                        }
                                        remainingAuthorsCon.close();
                                }

                                // if 10 books displayed then quit loop
                                // if (bookCount == 10) {
                                // break;
                                // }
                                // check current page
                                String firstLastIsbnAllBooksquery = "select min(bookIsbn) as firstBook,max(bookIsbn) as lastBook from librarybooks order by bookIsbn asc";
                                Connection firstLastIsbnAllBooksCon = GetJdbcConnection.getConnection();
                                Statement firstLastIsbnAlBooksSt = firstLastIsbnAllBooksCon.createStatement();
                                ResultSet firstLastIsbnAllBooksRs = firstLastIsbnAlBooksSt
                                                .executeQuery(firstLastIsbnAllBooksquery);
                                firstLastIsbnAllBooksRs.next();
                                int firstBookIsbn = firstLastIsbnAllBooksRs.getInt("firstBook");
                                int lastBookIsbn = firstLastIsbnAllBooksRs.getInt("lastBook");

                                if (!currentBookChecked && (rowIsbn == firstBookIsbn)) {
                                        ViewBook.currentPage = "first";
                                        currentBookChecked = true;
                                } else if (!currentBookChecked && (rowIsbn == lastBookIsbn)) {
                                        ViewBook.currentPage = "last";
                                        currentBookChecked = true;
                                } else if (!currentBookChecked) {
                                        ViewBook.currentPage = "middle";
                                }
                                // close connection
                                getGenreCon.close();
                                getAuthorsCon.close();
                                countAuthorCon.close();
                                firstLastIsbnAllBooksCon.close();
                        } // end of outer while loop
                        System.out.println(
                                        "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                        // page no
                        System.out.printf(" %70s|%d|%n", "", ViewBook.pageNo);
                        // close connections
                        allRecordsCon.close();

                } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                } // end of try catch
                  // for (int i = 0; i < fetchedIsbn.size(); i++) {
                  // System.out.println(fetchedIsbn.get(i));
                  // }
        }

}
