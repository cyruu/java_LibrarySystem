package subclasses.book.searchbook;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import subclasses.book.viewbook.ChooseBook;
import subclasses.book.viewbook.DisplayBookIsbn;
import subclasses.calculateUserChoice.CalculateUserChoice;
import subclasses.dbconnect.GetJdbcConnection;
import subclasses.validinputgetter.GetValidInput;

public class SearchByAuthor {
    public static int limit = 10;
    public static int offset = 0;
    public static String currentPage = "first";
    public static int page = 1;

    public static void searchByAuthor() {
        SearchByAuthor.currentPage = "first";
        Scanner scanner = new Scanner(System.in);
        SearchByAuthor.page = 1;
        SearchByAuthor.offset = 0;

        // total book fetched for offset
        int bookCount = 0;
        char retry = 'n';
        ChooseBook.bookChoosed = false;
        try {
            boolean firstBookFound = false;
            boolean lastBookFound = false;
            boolean oneBookOnly = false;
            System.out.println("Clear input buffer [any character]:");

            do {
                oneBookOnly = false;
                firstBookFound = false;
                lastBookFound = false;
                retry = 'n';
                scanner.nextLine();
                System.out.println("Enter author of book: ");
                String searchAuthor = scanner.nextLine();
                // find current page
                String firstLastIsbnAllBooksquery = "select min(ba.bookIsbn) as firstBook,max(ba.bookIsbn) as lastBook from bookauthors ba inner join authors a on ba.authorId=a.authorId where a.firstName like '%"
                        + searchAuthor + "%' or a.lastName like '%" + searchAuthor + "%' order by ba.bookIsbn asc";
                Connection firstLastIsbnAllBooksCon = GetJdbcConnection.getConnection();
                Statement firstLastIsbnAlBooksSt = firstLastIsbnAllBooksCon.createStatement();
                ResultSet firstLastIsbnAllBooksRs = firstLastIsbnAlBooksSt
                        .executeQuery(firstLastIsbnAllBooksquery);
                firstLastIsbnAllBooksRs.next();
                int firstBookIsbn = firstLastIsbnAllBooksRs.getInt("firstBook");
                int lastBookIsbn = firstLastIsbnAllBooksRs.getInt("lastBook");
                // get issb of all matched book
                // get book detail andgenre of title book
                String authorIsbnQuery = "select distinct(ba.bookIsbn) from bookauthors ba inner join authors a on ba.authorId=a.authorId where a.firstName like '%"
                        + searchAuthor + "%' or a.lastName like '%" + searchAuthor
                        + "%' order by ba.bookIsbn asc limit "
                        + SearchByAuthor.limit + " offset "
                        + SearchByAuthor.offset;

                // String searchIsbnTitleQuery = "select
                // l.bookIsbn,l.bookTitle,l.bookPublished,l.bookCopies,g.genreName from
                // librarybooks l inner join bookgenres bg on l.bookIsbn=bg.bookIsbn inner join
                // genres g on bg.genreId=g.genreId where l.bookTitle like '%"
                // + searchTitle + "%'";
                Connection authorIsbnCon = GetJdbcConnection.getConnection();
                Statement authorIsbnSt = authorIsbnCon.createStatement();
                ResultSet authorIsbnRs = authorIsbnSt.executeQuery(authorIsbnQuery);
                // fetch one to check
                boolean bookFound = authorIsbnRs.next();
                int choice = 0;
                if (bookFound && !searchAuthor.equals("")) {
                    ChooseBook.bookChoosed = false;
                    System.out.println();
                    System.out.printf("%60sYou searched (" + searchAuthor + ")%n", "");
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
                    int isbn = authorIsbnRs.getInt(1);

                    // check if ine item only
                    if (isbn == firstBookIsbn && isbn == lastBookIsbn) {
                        firstBookFound = true;
                        lastBookFound = true;
                        oneBookOnly = true;
                    }
                    // check first or last book
                    else {

                        if (isbn == firstBookIsbn) {
                            firstBookFound = true;
                        } else if (isbn == lastBookIsbn) {
                            lastBookFound = true;
                        }
                    }
                    DisplayBookIsbn.displayBookOfIsbn(isbn, authorIsbnRs.isLast());

                    // rremainig books
                    while (authorIsbnRs.next()) {

                        isbn = authorIsbnRs.getInt(1);

                        // check first or last book
                        if (!oneBookOnly) {

                            if (isbn == firstBookIsbn) {
                                firstBookFound = true;
                            } else if (isbn == lastBookIsbn) {
                                lastBookFound = true;
                            }
                        }

                        // isbn of books found
                        DisplayBookIsbn.displayBookOfIsbn(isbn, authorIsbnRs.isLast());

                    }
                    System.out.println(
                            "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                    // page no
                    System.out.printf(" %70s|%d|%n", "", SearchByAuthor.page);
                    // set current page
                    if (firstBookFound && lastBookFound) {
                        SearchByAuthor.currentPage = "both";
                    } else {
                        SearchByAuthor.currentPage = "first";
                    }

                    // paged option
                    boolean continueAsking = true;
                    do {

                        // view and get input according to current page
                        if (SearchByAuthor.currentPage.equals("first")) {
                            System.out
                                    .println(
                                            "\t\t\t\t\t\t\t\t\t\t\t1. Next->\n\t\t\t\t\t2. Choose book\t\t\t\t\t3. Back");
                            System.out.println("Enter your choice: ");
                            choice = GetValidInput.getValidInput(3);
                            choice = CalculateUserChoice.noPrevMenu(choice);
                        } else if (SearchByAuthor.currentPage.equals("middle")) {
                            System.out
                                    .println(
                                            "\t\t\t\t\t1. <-Prev\t\t\t\t\t\t2. Next->\n\t\t\t\t\t3. Choose book\t\t\t\t\t\t4. Back");
                            System.out.println("Enter your choice: ");
                            choice = GetValidInput.getValidInput(4);
                        } else if (SearchByAuthor.currentPage.equals("last")) {
                            System.out.println("\t\t\t\t\t1. <-Prev\n\t\t\t\t\t2. Choose book\t\t\t\t\t\t3.Back");
                            System.out.println("Enter your choice: ");
                            choice = GetValidInput.getValidInput(3);
                            choice = CalculateUserChoice.noNextMenu(choice);
                        } else if (SearchByAuthor.currentPage.equals("both")) {
                            System.out.println("\t\t\t\t\t1. Choose book\t\t\t\t\t2. Back");
                            System.out.println("Enter your choice: ");
                            choice = GetValidInput.getValidInput(2);
                            switch (choice) {
                                case 1:
                                    choice = 3;
                                    break;
                                case 2:
                                    choice = 4;
                                    break;
                            }
                        }
                        switch (choice) {
                            // previous
                            case 1:
                                SearchByAuthor.offset -= SearchByAuthor.limit;
                                SearchByAuthor.page--;
                                SearchPagination.pageSearchByAuthor(searchAuthor);
                                break;
                            // next
                            case 2:
                                SearchByAuthor.offset += SearchByAuthor.limit;
                                SearchByAuthor.page++;
                                SearchPagination.pageSearchByAuthor(searchAuthor);
                                break;
                            // choose book
                            case 3:
                                System.out.println();
                                System.out.println("\t--------------");
                                System.out.println("\tSelect book");
                                System.out.println("\t--------------\n");
                                SearchByIsbn.searchByIsbn();
                                continueAsking = false;
                                break;
                            // back
                            case 4:
                                continueAsking = false;

                                break;
                        }
                    } while (continueAsking);
                } else {
                    System.out.println("Book with author(" + searchAuthor + ") not found!!!");
                    System.out.println();
                    System.out.println("Retry? [y/n]");
                    retry = scanner.next().charAt(0);

                }
                authorIsbnCon.close();
            } while (retry == 'y');

        } catch (Exception e) {
            e.printStackTrace();
        } /// end of try
    }
}
