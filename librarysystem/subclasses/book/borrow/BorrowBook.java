package subclasses.book.borrow;

import java.sql.*;
import java.util.Scanner;

import mainclass.MainClass;
import subclasses.book.searchbook.SearchByIsbn;

import subclasses.book.viewbook.DisplayBookIsbn;
import subclasses.calculateUserChoice.CalculateUserChoice;
import subclasses.dbconnect.GetJdbcConnection;
import subclasses.validinputgetter.GetValidInput;

public class BorrowBook {
    public static int limit = 5;
    public static int offset = 0;
    public static int page = 1;
    public static String currentPage = "first";

    public static void borrowBook() {
        Scanner scanner = new Scanner(System.in);
        try {
            Connection borrowCon = GetJdbcConnection.getConnection();
            Statement borrowSt = borrowCon.createStatement();
            // check if copies of this book available
            String copiesQuery = "select bookTitle,bookCopies from librarybooks where bookIsbn="
                    + SearchByIsbn.selectedBookIsbn;
            ResultSet copiesRs = borrowSt.executeQuery(copiesQuery);
            copiesRs.next();
            String bookTitle = copiesRs.getString(1);
            int bookCopies = copiesRs.getInt(2);
            if (bookCopies > 0) {
                System.out.println("\nConfirm borrow book (" + bookTitle + ")? [y/n]");
                char confirm = scanner.next().charAt(0);
                // confirm borrow book
                if (confirm == 'y') {
                    // get userid from logged in user
                    String userQuery = "select userId from users where username='" + MainClass.getLoggedInUser() + "'";
                    ResultSet userRs = borrowSt.executeQuery(userQuery);
                    userRs.next();
                    int userId = userRs.getInt(1);
                    // check if user borrowed this book already
                    String alreadyborrowQuery = "select count(*) from bookborrows where userId=" + userId
                            + " and bookIsbn=" + SearchByIsbn.selectedBookIsbn;
                    ResultSet alreadyborrowRs = borrowSt.executeQuery(alreadyborrowQuery);
                    alreadyborrowRs.next();
                    int alreadyborrow = alreadyborrowRs.getInt(1);
                    // not borrowed then borrow
                    if (alreadyborrow == 0) {

                        // add bookId and userId in bookborrows table in database
                        // book must be returned within 30 days
                        String borrowQuery = "insert into bookborrows(userId,bookIsbn,returnDate) values (" + userId
                                + ","
                                + SearchByIsbn.selectedBookIsbn + ",date_add(current_timestamp,interval 30 day))";
                        int borrow = borrowSt.executeUpdate(borrowQuery);
                        if (borrow == 1) {
                            String decrementCopiesQuery = "update librarybooks set bookCopies=bookCopies-1 where bookIsbn="
                                    + SearchByIsbn.selectedBookIsbn;
                            int decCopies = borrowSt.executeUpdate(decrementCopiesQuery);
                            System.out.println("\nSuccessfully borrowed book (" + bookTitle + ")");
                            // decrement the value of copies by one
                        } else {

                            System.out.println("\nFailed to borrow book (" + bookTitle + ")!!");
                        }
                    } else {
                        System.out.println("\nFailed!! You have already borrowed (" + bookTitle + ")");
                    }

                }
                // borrow failed
                else {
                    System.out.println("\nFailed to borrow book (" + bookTitle + ")!!");
                }
            }
            // book not available at the moment
            else {
                System.out.println("\nBook (" + bookTitle + ") not available at the moment!!");
            }
            borrowCon.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    // display borrowed books
    public static void displayBorrowedBooks() {
        BorrowBook.offset = 0;
        // can be single page
        BorrowBook.currentPage = "first";
        Scanner scanner = new Scanner(System.in);
        BorrowBook.page = 1;

        try {
            Connection borrowCon = GetJdbcConnection.getConnection();
            Statement borrowSt = borrowCon.createStatement();
            boolean firstBookFound = false;
            boolean lastBookFound = false;
            boolean oneBookOnly = false;
            String userQuery = "select userId from users where username='" + MainClass.getLoggedInUser() + "'";
            ResultSet userRs = borrowSt.executeQuery(userQuery);
            userRs.next();
            int userId = userRs.getInt(1);
            char retry;
            do {
                oneBookOnly = false;
                firstBookFound = false;
                lastBookFound = false;
                retry = 'n';

                // find current page
                String firstLastIsbnAllBooksquery = "select min(bookIsbn) as firstBook,max(bookIsbn) as lastBook from bookborrows where userId="
                        + userId;
                Connection firstLastIsbnAllBooksCon = GetJdbcConnection.getConnection();
                Statement firstLastIsbnAlBooksSt = firstLastIsbnAllBooksCon.createStatement();
                ResultSet firstLastIsbnAllBooksRs = firstLastIsbnAlBooksSt
                        .executeQuery(firstLastIsbnAllBooksquery);
                firstLastIsbnAllBooksRs.next();
                int firstBookIsbn = firstLastIsbnAllBooksRs.getInt("firstBook");
                int lastBookIsbn = firstLastIsbnAllBooksRs.getInt("lastBook");

                // get issb of all matched book
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
                int choice = 0;
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
                    DisplayBookIsbn.displayBorrowBookOfIsbn(isbn, titleIsbnBookRs.isLast());

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
                        DisplayBookIsbn.displayBorrowBookOfIsbn(isbn, titleIsbnBookRs.isLast());

                    }

                    System.out.println(
                            "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------------+------------------+");
                    // page no
                    System.out.printf(" %75s|%d|%n", "", BorrowBook.page);
                    // set current page
                    if (firstBookFound && lastBookFound) {
                        BorrowBook.currentPage = "both";
                    } else {
                        BorrowBook.currentPage = "first";
                    }
                    // paged option
                    boolean continueAsking = true;
                    do {

                        // view and get input according to current page
                        if (BorrowBook.currentPage.equals("first")) {
                            System.out
                                    .println(
                                            "\t\t\t\t\t\t\t\t\t\t\t1. Next->\n\t\t\t\t\t2. Choose book\t\t\t\t\t3. Back");
                            System.out.println("Enter your choice: ");
                            choice = GetValidInput.getValidInput(3);
                            choice = CalculateUserChoice.noPrevMenu(choice);
                        } else if (BorrowBook.currentPage.equals("middle")) {
                            System.out
                                    .println(
                                            "\t\t\t\t\t1. <-Prev\t\t\t\t\t\t2. Next->\n\t\t\t\t\t3. Choose book\t\t\t\t\t\t4. Back");
                            System.out.println("Enter your choice: ");
                            choice = GetValidInput.getValidInput(4);
                        } else if (BorrowBook.currentPage.equals("last")) {
                            System.out.println("\t\t\t\t\t1. <-Prev\n\t\t\t\t\t2. Choose book\t\t\t\t\t\t3.Back");
                            System.out.println("Enter your choice: ");
                            choice = GetValidInput.getValidInput(3);
                            choice = CalculateUserChoice.noNextMenu(choice);
                        } else if (BorrowBook.currentPage.equals("both")) {
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
                                BorrowBook.offset -= BorrowBook.limit;
                                BorrowBook.page--;
                                BorrowPagination.pageDisplayBorrowedBooks(userId);
                                break;
                            // next
                            case 2:
                                BorrowBook.offset += BorrowBook.limit;
                                BorrowBook.page++;
                                BorrowPagination.pageDisplayBorrowedBooks(userId);
                                break;
                            // choose book
                            case 3:
                                System.out.println();
                                System.out.println("\t--------------");
                                System.out.println("\tSelect book");
                                System.out.println("\t--------------\n");
                                SearchByIsbn.searchBorrowByIsbn();

                                continueAsking = false;
                                break;
                            // back
                            case 4:
                                continueAsking = false;

                                break;
                        }
                    } while (continueAsking);
                }
                titleIsbnBookCon.close();
            } while (retry == 'y');

        } catch (Exception e) {
            e.printStackTrace();
        } /// end of try
    }
}
