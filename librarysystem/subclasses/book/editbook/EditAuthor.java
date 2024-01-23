package subclasses.book.editbook;

import java.io.ObjectInputStream.GetField;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import subclasses.book.searchbook.SearchByIsbn;
import subclasses.dbconnect.GetJdbcConnection;

public class EditAuthor {
    public static void editAuthor() {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("---------------");
        System.out.println("Edit author");
        System.out.println("---------------");
        System.out.println("\t1. Add author\t\t2.Remove author\t\t3. Back");
        System.out.println();
        System.out.println("Enter your choice:");
        int choice = scanner.nextInt();
        switch (choice) {
            // add author
            case 1:
                addAuthor();
                break;
            // remove author
            case 2:
                removeAuthor();
                break;
            // break
            case 3:
                break;
        }

    }

    // add author
    public static void addAuthor() {
        Scanner scanner = new Scanner(System.in);
        // clear buffer?
        System.out.println();
        System.out.println("---------------");
        System.out.println("Add author");
        System.out.println("---------------");
        System.out.println("Enter first name: ");
        String firstName = scanner.next();
        System.out.println("Enter last name: ");
        String lastName = scanner.next();
        try {

            // check if this author with same first and last name already exists in authors
            // database
            String authorCheckQuery = "select count(*) from authors where firstName='" + firstName + "'and lastName ='"
                    + lastName + "'";
            Connection editBookCon = GetJdbcConnection.getConnection();
            Statement auhorCheckSt = editBookCon.createStatement();
            ResultSet auhorCheckRs = auhorCheckSt.executeQuery(authorCheckQuery);
            auhorCheckRs.next();
            int authorCheckCount = auhorCheckRs.getInt(1);
            System.out.println();
            // author already exists in database authors
            if (authorCheckCount > 0) {
                // System.out.println("Author (" + firstName + " " + lastName + ") already
                // registred!");
                System.out.println("Confirm add author (" + firstName + " " + lastName + ") [y/n]");
                char confirmAddAuthor = scanner.next().charAt(0);
                // confirm add author
                if (confirmAddAuthor == 'y') {
                    boolean alreadyAuthor = false;
                    // check if this author is already author of the book selected
                    String alreadyAuthorQuery = "select ba.bookIsbn,ba.authorId from bookauthors ba inner join authors a on ba.authorId = a.authorId where a.firstName = '"
                            + firstName + "' and a.lastName = '" + lastName + "'";
                    Statement alreadyAuthorSt = editBookCon.createStatement();
                    ResultSet alreadyAuthorRs = alreadyAuthorSt.executeQuery(alreadyAuthorQuery);
                    int authorId = 0;
                    while (alreadyAuthorRs.next()) {
                        int authorOfIsbn = alreadyAuthorRs.getInt(1);
                        authorId = alreadyAuthorRs.getInt(2);
                        if (authorOfIsbn == SearchByIsbn.selectedBookIsbn) {
                            alreadyAuthor = true;
                            break;
                        }
                    }
                    // already author of this book
                    if (alreadyAuthor) {
                        System.out.println("\nFailed to add!!\n(" + firstName + " " + lastName
                                + ") is already author of this book!!");
                    }
                    // author exists in database and not author of this book
                    else {
                        String addAuthorQuery = "insert into bookauthors values(" + SearchByIsbn.selectedBookIsbn + ","
                                + authorId + ")";
                        Statement addAuthorSt = editBookCon.createStatement();
                        int row = addAuthorSt.executeUpdate(addAuthorQuery);
                        if (row == 1) {
                            System.out.println("Succefully added author (" + firstName + " " + lastName + ")");
                        }
                    }
                }
            }
            // if author name not in database (new author)
            else {
                System.out.println("New author (" + firstName + " " + lastName + ")! Confirm add? [y/n]");
                char confirmNewAuthor = scanner.next().charAt(0);
                // confirm add new author to authors database
                if (confirmNewAuthor == 'y') {
                    String addNewAuthorQuery = "insert into authors (firstName,lastName) values ('" + firstName + "','"
                            + lastName + "')";
                    Statement addNewAuthorSt = editBookCon.createStatement();
                    int row = addNewAuthorSt.executeUpdate(addNewAuthorQuery);
                    // // added to authors database
                    // if (row == 1) {
                    // System.out.println("Succefully added author (" + firstName + " " + lastName +
                    // ") to database");
                    // }
                    // get author id
                    String newAuthorIdQuery = "select authorId from authors where firstName='" + firstName
                            + "' and lastName='" + lastName + "'";
                    Statement newAuthorIdSt = editBookCon.createStatement();
                    ResultSet newAuthorIdRs = newAuthorIdSt.executeQuery(newAuthorIdQuery);
                    newAuthorIdRs.next();
                    int newAuthorId = newAuthorIdRs.getInt(1);
                    // add to bookauthors
                    String addAuthorQuery = "insert into bookauthors values(" + SearchByIsbn.selectedBookIsbn + ","
                            + newAuthorId + ")";
                    Statement addAuthorSt = editBookCon.createStatement();
                    int r = addAuthorSt.executeUpdate(addAuthorQuery);
                    if (r == 1) {
                        System.out.println("Succefully added author (" + firstName + " " + lastName + ")");
                    }
                }

            }
            editBookCon.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    // remove author
    public static void removeAuthor() {
        Scanner scanner = new Scanner(System.in);
        // clear buffer?
        System.out.println();
        System.out.println("---------------");
        System.out.println("Remove author");
        System.out.println("---------------");

        try {
            // count number of authors of this book
            String countAuthorsQuery = "select count(*) from bookauthors where bookisbn="
                    + SearchByIsbn.selectedBookIsbn;
            Connection editBookCon = GetJdbcConnection.getConnection();
            Statement countAuthorSt = editBookCon.createStatement();
            ResultSet countAuthorRs = countAuthorSt.executeQuery(countAuthorsQuery);
            countAuthorRs.next();
            int countAuthors = countAuthorRs.getInt(1);
            // ONLY one auhtor of this book cant remove
            if (countAuthors == 1) {
                System.out.println("Can't remove!! This book has only one author!");
            }
            // if this book has more than one authors
            else if (countAuthors > 1) {

                System.out.println("Enter first name: ");
                String firstName = scanner.next();
                System.out.println("Enter last name: ");
                String lastName = scanner.next();
                System.out.println();
                // if not existed author then enter valid author name
                String authorCheckQuery = "select count(*) from authors where firstName='" + firstName
                        + "' and lastName='"
                        + lastName + "'";
                Statement authorCheckSt = editBookCon.createStatement();
                ResultSet authorCheckRs = authorCheckSt.executeQuery(authorCheckQuery);
                authorCheckRs.next();
                int authorCheck = authorCheckRs.getInt(1);
                // author not registered in database
                if (authorCheck == 0) {
                    System.out.println("Failed! Author (" + firstName + " " + lastName + ") not found!!");
                    scanner.next().charAt(0);
                } else {

                    // get author id
                    String newAuthorIdQuery = "select authorId from authors where firstName='" + firstName
                            + "' and lastName='" + lastName + "'";
                    Statement newAuthorIdSt = editBookCon.createStatement();
                    ResultSet newAuthorIdRs = newAuthorIdSt.executeQuery(newAuthorIdQuery);
                    newAuthorIdRs.next();
                    int newAuthorId = newAuthorIdRs.getInt(1);
                    // check if this is author of book
                    String checkAuthorQuery = "select count(*) from bookauthors where bookIsbn="
                            + SearchByIsbn.selectedBookIsbn + " and authorId=" + newAuthorId;
                    Statement checkAuthorSt = editBookCon.createStatement();
                    ResultSet checkAuthorRs = checkAuthorSt.executeQuery(checkAuthorQuery);
                    checkAuthorRs.next();
                    int authorFound = checkAuthorRs.getInt(1);
                    // if author found then delete
                    if (authorFound == 1) {
                        System.out.println("Confirm remove author (" + firstName + " " + lastName + ")!! [y/n]");
                        char confirmRemoveAuthor = scanner.next().charAt(0);
                        // delete the author from this book
                        if (confirmRemoveAuthor == 'y') {
                            String removeAuthorQuery = "delete from bookauthors where bookIsbn="
                                    + SearchByIsbn.selectedBookIsbn + " and authorId=" + newAuthorId;
                            Statement removeAuthorSt = editBookCon.createStatement();
                            int r = removeAuthorSt.executeUpdate(removeAuthorQuery);
                            if (r == 1) {
                                System.out.println("Author (" + firstName + " " + lastName + ") removed!!");
                            }
                        } else {
                            System.out.println("Failed to remove author!!");
                        }

                    }
                    // this not the author of this book
                    else {
                        System.out.println("Failed! (" + firstName + " " + lastName + ") is not author of this book!");
                    }
                }

            }
            editBookCon.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
