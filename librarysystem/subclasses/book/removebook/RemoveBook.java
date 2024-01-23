package subclasses.book.removebook;

import java.sql.*;
import java.util.Scanner;

import subclasses.book.searchbook.SearchByIsbn;
import subclasses.book.searchbook.SearchByTitle;
import subclasses.book.viewbook.ChooseBook;
import subclasses.dbconnect.GetJdbcConnection;
import subclasses.options.OptionAfterSelect;
import subclasses.validinputgetter.GetValidInput;

public class RemoveBook {
    public static void removeBookOptions() {
        System.out.println();
        System.out.println("\t--------------");
        System.out.println("\tSelect book");
        System.out.println("\t--------------\n");
        System.out.println("\t1. Isbn\t\t\t2. Title\t\t\t3. Back");
        System.out.println();
        System.out.println("Enter your choice:");
        int selectChoice = GetValidInput.getValidInput(3);
        switch (selectChoice) {
            // select isbn
            case 1:
                SearchByIsbn.searchByIsbn();

                break;
            // title
            case 2:
                SearchByTitle.searchByTitle();
                removeBook();

                break;
            case 3:
                break;
        }
        if (ChooseBook.bookChoosed) {

            removeBook();
        }
    }

    public static void removeBook() {
        Scanner scanner = new Scanner(System.in);
        try {
            Connection removeBookCon = GetJdbcConnection.getConnection();
            Statement removeBookSt = removeBookCon.createStatement();
            // get book name query
            String getBookName = "select bookTitle from librarybooks where bookIsbn=" + SearchByIsbn.selectedBookIsbn;
            ResultSet getBookNameRs = removeBookSt.executeQuery(getBookName);
            getBookNameRs.next();
            String bookName = getBookNameRs.getString(1);

            String removeBookQuery = "delete from librarybooks where bookIsbn=" + SearchByIsbn.selectedBookIsbn;
            System.out.println();
            System.out.println("--------------");
            System.out.println("Remove book");
            System.out.println("--------------\n");
            System.out.println("Confirm remove book (" + bookName + ")? [y/n]");
            int confirm = scanner.next().charAt(0);
            // confirm delete the book
            if (confirm == 'y') {
                int r = removeBookSt.executeUpdate(removeBookQuery);
                if (r == 1) {
                    System.out.println("\nSuccessfully deleted book (" + bookName + ")");
                }
            }
            // dont delete
            else {
                System.out.println("\n Failed to delete book (" + bookName + ")!!!");
            }
            removeBookCon.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
