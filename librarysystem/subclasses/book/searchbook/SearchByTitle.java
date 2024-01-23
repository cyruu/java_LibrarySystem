package subclasses.book.searchbook;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import subclasses.book.viewbook.ChooseBook;
import subclasses.book.viewbook.DisplayBookIsbn;

import subclasses.calculateUserChoice.CalculateUserChoice;
import subclasses.dbconnect.GetJdbcConnection;

import subclasses.validinputgetter.GetValidInput;

public class SearchByTitle {
    public static int limit = 10;
    public static int offset = 0;
    public static int page = 1;
    public static String currentPage = "first";

    public static void searchByTitle() {
        SearchByTitle.offset = 0;
        // can be single page
        SearchByTitle.currentPage = "first";
        Scanner scanner = new Scanner(System.in);
        SearchByTitle.page = 1;
        ChooseBook.bookChoosed = false;
        try {
            boolean firstBookFound = false;
            boolean lastBookFound = false;
            boolean oneBookOnly = false;

            System.out.println("Clear input buffer [any character]:");
            char retry;
            do {
                oneBookOnly = false;
                firstBookFound = false;
                lastBookFound = false;
                retry = 'n';
                scanner.nextLine();
                System.out.println("Enter title of book: ");

                String searchTitle = scanner.nextLine();
                // find current page
                String firstLastIsbnAllBooksquery = "select min(bookIsbn) as firstBook,max(bookIsbn) as lastBook from librarybooks where bookTitle like '%"
                        + searchTitle + "%' order by bookIsbn asc";
                Connection firstLastIsbnAllBooksCon = GetJdbcConnection.getConnection();
                Statement firstLastIsbnAlBooksSt = firstLastIsbnAllBooksCon.createStatement();
                ResultSet firstLastIsbnAllBooksRs = firstLastIsbnAlBooksSt
                        .executeQuery(firstLastIsbnAllBooksquery);
                firstLastIsbnAllBooksRs.next();
                int firstBookIsbn = firstLastIsbnAllBooksRs.getInt("firstBook");
                int lastBookIsbn = firstLastIsbnAllBooksRs.getInt("lastBook");

                // get issb of all matched book
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
                int choice = 0;
                if (bookFound && !searchTitle.equals("")) {
                    ChooseBook.bookChoosed = true;
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
                    DisplayBookIsbn.displayBookOfIsbn(isbn, titleIsbnBookRs.isLast());

                    // rremainig books
                    while (titleIsbnBookRs.next()) {
                        isbn = titleIsbnBookRs.getInt(1);
                        // check first or last book
                        if (!oneBookOnly) {

                            if (isbn == firstBookIsbn) {
                                firstBookFound = true;
                            } else if (isbn == lastBookIsbn) {
                                lastBookFound = true;
                            }
                        }
                        // isbn of books found
                        DisplayBookIsbn.displayBookOfIsbn(isbn, titleIsbnBookRs.isLast());

                    }

                    System.out.println(
                            "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                    // page no
                    System.out.printf(" %70s|%d|%n", "", SearchByTitle.page);
                    // set current page
                    if (firstBookFound && lastBookFound) {
                        SearchByTitle.currentPage = "both";
                    } else {
                        SearchByTitle.currentPage = "first";
                    }
                    // paged option
                    boolean continueAsking = true;
                    do {

                        // view and get input according to current page
                        if (SearchByTitle.currentPage.equals("first")) {
                            System.out
                                    .println(
                                            "\t\t\t\t\t\t\t\t\t\t\t1. Next->\n\t\t\t\t\t2. Choose book\t\t\t\t\t3. Back");
                            System.out.println("Enter your choice: ");
                            choice = GetValidInput.getValidInput(3);
                            choice = CalculateUserChoice.noPrevMenu(choice);
                        } else if (SearchByTitle.currentPage.equals("middle")) {
                            System.out
                                    .println(
                                            "\t\t\t\t\t1. <-Prev\t\t\t\t\t\t2. Next->\n\t\t\t\t\t3. Choose book\t\t\t\t\t\t4. Back");
                            System.out.println("Enter your choice: ");
                            choice = GetValidInput.getValidInput(4);
                        } else if (SearchByTitle.currentPage.equals("last")) {
                            System.out.println("\t\t\t\t\t1. <-Prev\n\t\t\t\t\t2. Choose book\t\t\t\t\t\t3.Back");
                            System.out.println("Enter your choice: ");
                            choice = GetValidInput.getValidInput(3);
                            choice = CalculateUserChoice.noNextMenu(choice);
                        } else if (SearchByTitle.currentPage.equals("both")) {
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
                                SearchByTitle.offset -= SearchByTitle.limit;
                                SearchByTitle.page--;
                                SearchPagination.pageSearchByTitle(searchTitle);
                                break;
                            // next
                            case 2:
                                SearchByTitle.offset += SearchByTitle.limit;
                                SearchByTitle.page++;
                                SearchPagination.pageSearchByTitle(searchTitle);
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
                    System.out.println("Book with title(" + searchTitle + ") not found!!!");
                    System.out.println();
                    System.out.println("Retry? [y/n]");
                    retry = scanner.next().charAt(0);

                }
                titleIsbnBookCon.close();
            } while (retry == 'y');

        } catch (Exception e) {
            e.printStackTrace();
        } /// end of try
    }
}
