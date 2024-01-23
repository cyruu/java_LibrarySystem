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

public class SearchByGenre {
    public static int limit = 10;
    public static int offset = 0;
    public static int page = 1;
    public static String currentPage = "first";

    public static void searchByGenre() {
        SearchByGenre.offset = 0;
        // can be single page
        SearchByGenre.currentPage = "first";
        SearchByGenre.page = 1;
        Scanner scanner = new Scanner(System.in);
        ChooseBook.bookChoosed = false;
        char retry = 'n';
        try {
            boolean firstBookFound = false;
            boolean lastBookFound = false;
            boolean oneBookOnly = false;
            do {
                oneBookOnly = false;
                firstBookFound = false;
                lastBookFound = false;
                retry = 'n';
                System.out.println();
                System.out.println("\t----------");
                System.out.println("\tGenres");
                System.out.println("\t----------");
                System.out.println();
                // display all availabe category of the books in database
                String allGenreQuery = "select * from genres";

                Connection searchGenreCon = GetJdbcConnection.getConnection();
                Statement allGenreSt = searchGenreCon.createStatement();
                ResultSet allGenreRs = allGenreSt.executeQuery(allGenreQuery);
                int genreCount = 0;
                while (allGenreRs.next()) {
                    genreCount++;
                    int genreId = allGenreRs.getInt(1);
                    String genreName = allGenreRs.getString(2);
                    System.out.printf("\t" + genreId + ". " + genreName + "\t");
                    if (genreCount % 3 == 0) {
                        System.out.println();
                    }
                }

                System.out.println("\n");
                System.out.println("Choose genre of book: ");

                int genreChoice = GetValidInput.getValidInput(genreCount);
                // find current page
                String firstLastIsbnAllBooksquery = "select min(bookIsbn) as firstBook,max(bookIsbn) as lastBook from bookgenres where genreId="
                        + genreChoice + " order by bookIsbn asc";
                Connection firstLastIsbnAllBooksCon = GetJdbcConnection.getConnection();
                Statement firstLastIsbnAlBooksSt = firstLastIsbnAllBooksCon.createStatement();
                ResultSet firstLastIsbnAllBooksRs = firstLastIsbnAlBooksSt
                        .executeQuery(firstLastIsbnAllBooksquery);
                firstLastIsbnAllBooksRs.next();
                int firstBookIsbn = firstLastIsbnAllBooksRs.getInt("firstBook");
                int lastBookIsbn = firstLastIsbnAllBooksRs.getInt("lastBook");

                // get bookIsbn of genre selected
                String searchGenreQuery = "select bookIsbn from bookgenres where genreId=" + genreChoice
                        + " order by bookIsbn asc limit "
                        + SearchByGenre.limit + " offset "
                        + SearchByGenre.offset;
                Statement searchGenreSt = searchGenreCon.createStatement();
                ResultSet searchGenreRs = searchGenreSt.executeQuery(searchGenreQuery);
                // get genre name
                String genreNameQuery = "select genreName from genres where genreId=" + genreChoice;
                Statement genreNameSt = searchGenreCon.createStatement();
                ResultSet genreNameRs = genreNameSt.executeQuery(genreNameQuery);
                genreNameRs.next();
                String genreName = genreNameRs.getString(1);
                // fetch one to check
                boolean bookFound = searchGenreRs.next();
                int choice = 0;
                if (bookFound) {
                    ChooseBook.bookChoosed = true;
                    System.out.println();
                    System.out.printf("%64sYou searched (" + genreName + ")%n", "");
                    System.out.println(
                            "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                    System.out.printf("\t | %-8s | %-32s | %-14s | %-24s | %-16s | %-10s |%n", " ISBN",
                            " Title",
                            " Published",
                            " Authors", " Genre", " copies");
                    System.out.println(
                            "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                    // display first record
                    // display all record of that isbn
                    int isbn = searchGenreRs.getInt(1);
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
                    DisplayBookIsbn.displayBookOfIsbn(isbn, searchGenreRs.isLast());

                    // remainig books
                    while (searchGenreRs.next()) {
                        isbn = searchGenreRs.getInt(1);
                        // check first or last book
                        if (!oneBookOnly) {

                            if (isbn == firstBookIsbn) {
                                firstBookFound = true;
                            } else if (isbn == lastBookIsbn) {
                                lastBookFound = true;
                            }
                        }
                        // isbn of books found
                        DisplayBookIsbn.displayBookOfIsbn(isbn, searchGenreRs.isLast());

                    }
                    System.out.println(
                            "\t +----------+----------------------------------+----------------+--------------------------+------------------+------------+");
                    // page no
                    System.out.printf(" %70s|%d|%n", "", SearchByGenre.page);
                    // set current page
                    if (firstBookFound && lastBookFound) {
                        SearchByGenre.currentPage = "both";
                    } else {
                        SearchByGenre.currentPage = "first";
                    }
                    // paged option
                    boolean continueAsking = true;
                    do {

                        // view and get input according to current page
                        if (SearchByGenre.currentPage.equals("first")) {
                            System.out
                                    .println(
                                            "\t\t\t\t\t\t\t\t\t\t\t1. Next->\n\t\t\t\t\t2. Choose book\t\t\t\t\t3. Back");
                            System.out.println("Enter your choice: ");
                            choice = GetValidInput.getValidInput(3);
                            choice = CalculateUserChoice.noPrevMenu(choice);
                        } else if (SearchByGenre.currentPage.equals("middle")) {
                            System.out
                                    .println(
                                            "\t\t\t\t\t1. <-Prev\t\t\t\t\t\t2. Next->\n\t\t\t\t\t3. Choose book\t\t\t\t\t\t4. Back");
                            System.out.println("Enter your choice: ");
                            choice = GetValidInput.getValidInput(4);
                        } else if (SearchByGenre.currentPage.equals("last")) {
                            System.out.println("\t\t\t\t\t1. <-Prev\n\t\t\t\t\t2. Choose book\t\t\t\t\t\t3.Back");
                            System.out.println("Enter your choice: ");
                            choice = GetValidInput.getValidInput(3);
                            choice = CalculateUserChoice.noNextMenu(choice);
                        } else if (SearchByGenre.currentPage.equals("both")) {
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
                                SearchByGenre.offset -= SearchByGenre.limit;
                                SearchByGenre.page--;
                                SearchPagination.pageSearchByGenre(genreChoice, genreName);
                                break;
                            // next
                            case 2:
                                SearchByGenre.offset += SearchByGenre.limit;
                                SearchByGenre.page++;
                                SearchPagination.pageSearchByGenre(genreChoice, genreName);
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
                    System.out.println("Book with genre(" + genreChoice + ") not found!!!");
                    System.out.println();
                    System.out.println("Retry? [y/n]");
                    retry = scanner.next().charAt(0);

                }
                searchGenreCon.close();
            } while (retry == 'y');

        } catch (Exception e) {
            e.printStackTrace();
        } /// end of try
    }
}
