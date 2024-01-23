package subclasses.book.addbook;

import java.sql.*;
import java.util.Scanner;

import subclasses.dbconnect.GetJdbcConnection;
import subclasses.validinputgetter.GetValidInput;
import subclasses.validinputgetter.GetValidIsbn;

public class AddBook {
    public static void addBook() {
        Scanner scanner = new Scanner(System.in);
        try {
            Connection con = GetJdbcConnection.getConnection();
            Statement st = con.createStatement();
            System.out.println();
            System.out.println("---------------");
            System.out.println("Add Book");
            System.out.println("---------------");
            // random isbn not in database
            int bookIsbn = GetValidIsbn.getValidIsbn();
            System.out.println("Enter title of book:");
            String bookTitle = scanner.nextLine();
            System.out.println("Enter published year of book:");
            int bookPublished = scanner.nextInt();
            System.out.println("Enter copies of book:");
            int bookCopies = scanner.nextInt();
            // selct genre from db
            System.out.println("Select genre of book:\n");
            String allGenreQuery = "select * from genres";
            ResultSet allGenreRs = st.executeQuery(allGenreQuery);
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
            System.out.println("\nChoose genre of book: ");
            int bookGenreId = GetValidInput.getValidInput(genreCount);
            // add bookisbn, bookTitle, bookPublished, bookCopies to librarybooks
            String addLibraryBooksQuery = "insert into librarybooks values(" + bookIsbn + ",'" + bookTitle + "',"
                    + bookPublished + "," + bookCopies + ")";
            int r1 = st.executeUpdate(addLibraryBooksQuery);
            // add genreId in bookgenres
            String addBookGenresQuery = "insert into bookgenres values(" + bookIsbn + "," + bookGenreId + ")";
            int r2 = st.executeUpdate(addBookGenresQuery);
            char anotherAuthor = 'n';
            do {
                anotherAuthor = 'n';
                // add firstName, lastName in bookauthors
                System.out.println("Enter author's first name:");
                String authorFirstName = scanner.next();
                scanner.nextLine();
                System.out.println("Enter author's last name:");
                String authorLastName = scanner.next();
                int authorId = 0;
                // check if author already in db
                String checkAuthorQuery = "select count(*) from authors where firstName='" + authorFirstName
                        + "' and lastName='" + authorLastName + "'";
                ResultSet checkAuthorRs = st.executeQuery(checkAuthorQuery);
                checkAuthorRs.next();
                int checkAuthor = checkAuthorRs.getInt(1);
                // auhtor exists then get author Id
                if (checkAuthor == 1) {
                    // get authorId
                    String authorIdQuery = "select authorId from authors where firstName='" + authorFirstName
                            + "' and lastName='" + authorLastName + "'";
                    ResultSet authorIdRs = st.executeQuery(authorIdQuery);
                    authorIdRs.next();
                    int requiredAuthorId = authorIdRs.getInt(1);
                    authorId = requiredAuthorId;
                }
                // author not register, confirm register?
                else {
                    System.out.println("Register new author (" + authorFirstName + " " + authorLastName + ")? [y/n]");
                    char confirm = scanner.next().charAt(0);
                    // register author to databse authors
                    if (confirm == 'y') {
                        String registerAuthorQuery = "insert into authors(firstName,lastName) values ('"
                                + authorFirstName
                                + "','" + authorLastName + "')";
                        int row = st.executeUpdate(registerAuthorQuery);
                        if (row == 1) {
                            System.out.println("Successfully registered new author!!");
                            System.out.println();
                            // get authorId
                            String authorIdQuery = "select authorId from authors where firstName='" + authorFirstName
                                    + "' and lastName='" + authorLastName + "'";
                            ResultSet authorIdRs = st.executeQuery(authorIdQuery);
                            authorIdRs.next();
                            int requiredAuthorId = authorIdRs.getInt(1);
                            authorId = requiredAuthorId;
                        }
                    }
                    // confirm='n'
                    else {
                        System.out.println("\nFailed to register new author!!");
                    }
                    // add author to book authors
                    if (authorId != 0) {
                        String bookAuthorQuery = "insert into bookauthors values (" + bookIsbn + "," + authorId + ")";
                        int row = st.executeUpdate(bookAuthorQuery);
                        // System.out.println("Successfully added new book (" + bookTitle + ")!!");
                        System.out.println("Have next author? [y/n]");
                        anotherAuthor = scanner.next().charAt(0);
                    }

                }
            } while (anotherAuthor == 'y');
            System.out.println("successfully added new book!");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }
}
