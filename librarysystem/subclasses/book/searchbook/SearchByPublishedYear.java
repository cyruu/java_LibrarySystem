package subclasses.book.searchbook;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import subclasses.book.viewbook.ChooseBook;
import subclasses.book.viewbook.DisplayBookIsbn;
import subclasses.calculateUserChoice.CalculateUserChoice;
import subclasses.dbconnect.GetJdbcConnection;
import subclasses.options.OptionAfterSelect;
import subclasses.validinputgetter.GetValidInput;

public class SearchByPublishedYear {
    public static int limit = 5;
    public static int offset = 0;
    public static int page = 1;
    public static String currentPage = "first";

    public static void searchByPublishedYear() {
        SearchByTitle.offset = 0;
        // can be single page
        SearchByTitle.currentPage = "first";
        Scanner scanner = new Scanner(System.in);
        SearchByTitle.page = 1;
        ChooseBook.bookChoosed = false;
        char retry = 'n';
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
                System.out.println("Enter published year of book: ");
                int searchPublishedYear = scanner.nextInt();
                // find current page
                String firstLastIsbnAllBooksquery = "select min(bookIsbn) as firstBook,max(bookIsbn) as lastBook from librarybooks where bookPublished="
                        + searchPublishedYear + " order by bookIsbn asc";
                Connection firstLastIsbnAllBooksCon = GetJdbcConnection.getConnection();
                Statement firstLastIsbnAlBooksSt = firstLastIsbnAllBooksCon.createStatement();
                ResultSet firstLastIsbnAllBooksRs = firstLastIsbnAlBooksSt
                        .executeQuery(firstLastIsbnAllBooksquery);
                firstLastIsbnAllBooksRs.next();
                int firstBookIsbn = firstLastIsbnAllBooksRs.getInt("firstBook");
                int lastBookIsbn = firstLastIsbnAllBooksRs.getInt("lastBook");
                // get issb of all matched book
                // get book detail andgenre of title book
                String publishedYearQuery = "select bookIsbn from librarybooks where bookPublished="
                        + searchPublishedYear
                        + " order by bookIsbn asc limit "
                        + SearchByPublishedYear.limit + " offset "
                        + SearchByPublishedYear.offset;

                // String searchIsbnTitleQuery = "select
                // l.bookIsbn,l.bookTitle,l.bookPublished,l.bookCopies,g.genreName from
                // librarybooks l inner join bookgenres bg on l.bookIsbn=bg.bookIsbn inner join
                // genres g on bg.genreId=g.genreId where l.bookTitle like '%"
                // + searchTitle + "%'";
                Connection publishedIsbnBookCon = GetJdbcConnection.getConnection();
                Statement publishedIsbnBookSt = publishedIsbnBookCon.createStatement();
                ResultSet publishedIsbnBookRs = publishedIsbnBookSt.executeQuery(publishedYearQuery);
                // fetch one to check
                boolean bookFound = publishedIsbnBookRs.next();
                int choice = 0;
                if (bookFound) {
                    ChooseBook.bookChoosed = false;
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
                    int isbn = publishedIsbnBookRs.getInt(1);
                    // check if one item only
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
                    DisplayBookIsbn.displayBookOfIsbn(isbn, publishedIsbnBookRs.isLast());

                    // rremainig books
                    while (publishedIsbnBookRs.next()) {
                        isbn = publishedIsbnBookRs.getInt(1);
                        // check first or last book
                        if (!oneBookOnly) {

                            if (isbn == firstBookIsbn) {
                                firstBookFound = true;
                            } else if (isbn == lastBookIsbn) {
                                lastBookFound = true;
                            }
                        }
                        // isbn of books found
                        DisplayBookIsbn.displayBookOfIsbn(isbn, publishedIsbnBookRs.isLast());

                    }
                    System.out.println(
                            "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                    // page no
                    System.out.printf(" %70s|%d|%n", "", SearchByTitle.page);
                    // set current page
                    if (firstBookFound && lastBookFound) {
                        SearchByPublishedYear.currentPage = "both";
                    } else {
                        SearchByPublishedYear.currentPage = "first";
                    }
                    // paged option
                    boolean continueAsking = true;
                    do {

                        // view and get input according to current page
                        if (SearchByPublishedYear.currentPage.equals("first")) {
                            System.out
                                    .println(
                                            "\t\t\t\t\t\t\t\t\t\t\t1. Next->\n\t\t\t\t\t2. Choose book\t\t\t\t\t3. Back");
                            System.out.println("Enter your choice: ");
                            choice = GetValidInput.getValidInput(3);
                            choice = CalculateUserChoice.noPrevMenu(choice);
                        } else if (SearchByPublishedYear.currentPage.equals("middle")) {
                            System.out
                                    .println(
                                            "\t\t\t\t\t1. <-Prev\t\t\t\t\t\t2. Next->\n\t\t\t\t\t3. Choose book\t\t\t\t\t\t4. Back");
                            System.out.println("Enter your choice: ");
                            choice = GetValidInput.getValidInput(4);
                        } else if (SearchByPublishedYear.currentPage.equals("last")) {
                            System.out.println("\t\t\t\t\t1. <-Prev\n\t\t\t\t\t2. Choose book\t\t\t\t\t\t3.Back");
                            System.out.println("Enter your choice: ");
                            choice = GetValidInput.getValidInput(3);
                            choice = CalculateUserChoice.noNextMenu(choice);
                        } else if (SearchByPublishedYear.currentPage.equals("both")) {
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
                                SearchByPublishedYear.offset -= SearchByPublishedYear.limit;
                                SearchByPublishedYear.page--;
                                SearchPagination.pageSearchByPublishedYear(searchPublishedYear);
                                break;
                            // next
                            case 2:
                                SearchByPublishedYear.offset += SearchByPublishedYear.limit;
                                SearchByPublishedYear.page++;
                                SearchPagination.pageSearchByPublishedYear(searchPublishedYear);
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
                    System.out.println("Book with published year(" + searchPublishedYear + ") not found!!!");
                    System.out.println();
                    System.out.println("Retry? [y/n]");
                    retry = scanner.next().charAt(0);

                }
                publishedIsbnBookCon.close();
            } while (retry == 'y');

        } catch (Exception e) {
            e.printStackTrace();
        } /// end of try
    }
}
