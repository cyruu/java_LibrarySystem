package subclasses.book.searchbook;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import mainclass.MainClass;
import subclasses.book.borrow.ReturnBook;
import subclasses.book.editbook.EditBook;
import subclasses.book.removebook.RemoveBook;
import subclasses.book.viewbook.ChooseBook;
import subclasses.book.viewbook.DisplayBookIsbn;
import subclasses.dbconnect.GetJdbcConnection;
import subclasses.options.OptionAfterSelect;
import subclasses.validinputgetter.GetValidInput;

public class SearchByIsbn {
        public static int selectedBookIsbn = 0;

        public static void searchByIsbn() {
                Scanner scannner = new Scanner(System.in);
                ChooseBook.bookChoosed = false;
                char retry = 'n';
                try {

                        do {
                                retry = 'n';
                                ChooseBook.bookChoosed = false;
                                System.out.println("Enter ISBN of book: ");
                                int isbn = scannner.nextInt();
                                String isbnBookQuery = "select bookIsbn from librarybooks where bookIsbn="
                                                + isbn;
                                Connection isbnBookCon = GetJdbcConnection.getConnection();
                                Statement isbnBookSt = isbnBookCon.createStatement();
                                ResultSet isbnBookRs = isbnBookSt.executeQuery(isbnBookQuery);
                                boolean bookFound = isbnBookRs.next();

                                if (bookFound) {
                                        ChooseBook.bookChoosed = true;
                                        SearchByIsbn.selectedBookIsbn = isbn;
                                        System.out.println();
                                        System.out.printf("%64sYou searched (" + isbn + ")%n", "");
                                        System.out.println(
                                                        "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                                        System.out.printf("\t | %-8s | %-32s | %-14s | %-24s | %-16s | %-10s |%n",
                                                        "  ISBN",
                                                        "             Title",
                                                        "  Published",
                                                        "         Authors", "     Genre", "  copies");
                                        System.out.println(
                                                        "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                                        DisplayBookIsbn.displayBookOfIsbn(isbn, isbnBookRs.isLast());

                                        System.out.println(
                                                        "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                                        // option after displaying records
                                        OptionAfterSelect.optionAfterSelect();
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

        public static void searchBorrowByIsbn() {
                Scanner scannner = new Scanner(System.in);

                char retry = 'n';
                try {

                        do {
                                retry = 'n';
                                Connection isbnBookCon = GetJdbcConnection.getConnection();
                                Statement isbnBookSt = isbnBookCon.createStatement();
                                ChooseBook.bookChoosed = false;
                                System.out.println("Enter ISBN of book: ");
                                int isbn = scannner.nextInt();

                                // get user id
                                String userQuery = "select userId from users where username='"
                                                + MainClass.getLoggedInUser() + "'";
                                ResultSet userRs = isbnBookSt.executeQuery(userQuery);
                                userRs.next();
                                int userId = userRs.getInt(1);
                                String isbnBookQuery = "select bookIsbn from bookborrows where bookIsbn="
                                                + isbn + " and userId=" + userId;
                                ResultSet isbnBookRs = isbnBookSt.executeQuery(isbnBookQuery);
                                boolean bookFound = isbnBookRs.next();
                                if (bookFound) {

                                        ChooseBook.bookChoosed = true;
                                        SearchByIsbn.selectedBookIsbn = isbn;
                                        System.out.println();

                                        System.out.println(
                                                        "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------------+------------------+");
                                        System.out.printf(
                                                        "\t | %-8s | %-32s | %-14s | %-24s | %-16s | %-16s | %-16s |%n",
                                                        "  ISBN",
                                                        "             Title",
                                                        "  Published",
                                                        "         Authors", "     Genre", "   Borrow Date",
                                                        "   Return Date");
                                        System.out.println(
                                                        "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------------+------------------+");

                                        DisplayBookIsbn.displayBorrowBookOfIsbn(isbn, isbnBookRs.isLast());

                                        System.out.println(
                                                        "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------------+------------------+");
                                        // option after displaying records
                                        System.out.println("\n\t\t\t1. Return\t\t\t2. Back");
                                        System.out.println();
                                        System.out.println("Enter your choice:");
                                        int returnChoice = GetValidInput.getValidInput(3);
                                        switch (returnChoice) {
                                                // return
                                                case 1:

                                                        ReturnBook.returnBook();
                                                        break;
                                                // back
                                                case 2:

                                                        break;

                                        }
                                } else {

                                        System.out.println("You have not borrowed this book !!!");
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
