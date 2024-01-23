package subclasses.book.viewbook;

import java.sql.*;

import subclasses.book.searchbook.SearchBook;
import subclasses.book.searchbook.SearchByIsbn;
import subclasses.book.searchbook.SearchByTitle;
import subclasses.calculateUserChoice.CalculateUserChoice;
import subclasses.dbconnect.GetJdbcConnection;
import subclasses.options.OptionAfterSelect;
import subclasses.validinputgetter.GetValidInput;

// select * from librarybooks l inner join bookauthors ba on l.bookIsbn = ba.bookIsbn inner join authors a on ba.authorId = a.authorId inner join bookgenres bg on l.bookIsbn = bg.bookIsbn inner join genres g on bg.genreId = g.genreId order by l.bookIsbn asc;
public class ViewBook {
    public static int offset = 0;
    public static int limitValue = 10;
    public static int pageNo = 1;
    public static String currentPage = "first";

    public static void viewBooks() {

        // initially display books after selecting view books
        ViewBook.offset = 0;
        ViewBook.pageNo = 1;
        // display first page
        BookPagination.viewPagedBooks();
        System.out.println();
        int choice = 0;
        boolean continueAsking = true;
        ChooseBook.bookChoosed = false;
        do {

            // view and get input according to current page
            if (ViewBook.currentPage.equals("first")) {
                System.out.println("\t\t\t\t\t\t\t\t\t\t\t1. Next->\n\t\t\t\t\t2. Choose book\t\t\t\t\t3. Back");
                System.out.println("Enter your choice: ");
                choice = GetValidInput.getValidInput(3);
                choice = CalculateUserChoice.noPrevMenu(choice);
            } else if (ViewBook.currentPage.equals("middle")) {
                System.out
                        .println(
                                "\t\t\t\t\t1. <-Prev\t\t\t\t\t\t2. Next->\n\t\t\t\t\t3. Choose book\t\t\t\t\t\t4. Back");
                System.out.println("Enter your choice: ");
                choice = GetValidInput.getValidInput(4);
            } else if (ViewBook.currentPage.equals("last")) {
                System.out.println("\t\t\t\t\t1. <-Prev\n\t\t\t\t\t2. Choose book\t\t\t\t\t\t3.Back");
                System.out.println("Enter your choice: ");
                choice = GetValidInput.getValidInput(3);
                choice = CalculateUserChoice.noNextMenu(choice);
            }

            switch (choice) {
                // previous
                case 1:
                    ViewBook.offset -= limitValue;
                    ViewBook.pageNo--;
                    BookPagination.viewPagedBooks();
                    break;
                // next
                case 2:
                    ViewBook.offset += limitValue;
                    ViewBook.pageNo++;
                    BookPagination.viewPagedBooks();
                    break;
                // search
                case 3:
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

                            break;
                        case 3:
                            break;
                    }
                    // if (ChooseBook.bookChoosed) {

                    // OptionAfterSelect.optionAfterSelect();
                    // ChooseBook.bookChoosed = false;
                    // }
                    continueAsking = false;
                    break;
                // back
                case 4:
                    continueAsking = false;

                    break;
            }
        } while (continueAsking);

    }

}
