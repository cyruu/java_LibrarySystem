package mainclass;

import java.util.Scanner;

import subclasses.DisplayMenus;
import subclasses.Login;
import subclasses.Logout;
import subclasses.Signup;
import subclasses.book.addbook.AddBook;
import subclasses.book.borrow.BorrowBook;
import subclasses.book.borrow.ReturnBook;
import subclasses.book.removebook.RemoveBook;
import subclasses.book.searchbook.SearchBook;
import subclasses.book.searchbook.SearchByIsbn;
import subclasses.book.viewbook.ViewBook;
import subclasses.calculateUserChoice.CalculateUserChoice;
import subclasses.privacy.ChangePrivacy;
import subclasses.signupaccount.SignupAccount;

public class MainClass {
    // instance variables
    private static String loggedInUser = "guest";
    // member,admin,guest
    private static String userMode = "guest";
    // logged in
    private static boolean loggedIn = false;

    // contructor to set userMode as guest in deafult

    // getters
    public static String getUserMode() {
        return userMode;
    }

    public static boolean getLoggedIn() {
        return loggedIn;
    }

    public static String getLoggedInUser() {
        return loggedInUser;
    }

    // setters
    public static void setLoggedInUser(String updatedLoggedInUser) {
        loggedInUser = updatedLoggedInUser;
    }

    public static void setUserMode(String updatedUserMode) {
        userMode = updatedUserMode;
    }

    public static void setLoggedIn(boolean updatedLoggedIn) {
        loggedIn = updatedLoggedIn;
    }

    // main function
    public static void main(String[] args) {
        // MainClass object
        char exitLoop;
        boolean exited = false;
        boolean directlyViewMenus;
        boolean directToLoginMenu = false;
        int menuChoice;
        // MainClass mainClassObj = new MainClass();
        // get default user mode

        do {
            directlyViewMenus = false;

            System.out.println();
            System.out.println(
                    "+----------------------------------------------------------------------------------------------------------------------------------------+");
            System.out.printf("|%60s%-76s|%n", "", "MY LIBRARY");
            System.out.println(
                    "+----------------------------------------------------------------------------------------------------------------------------------------+");
            System.out.println();
            // login in as (userMode) message
            if (loggedIn)
                System.out.println("\tHi! " + MainClass.loggedInUser + " \t\t\t(" + MainClass.userMode + ")");
            else
                System.out.println("\tWelcome! \t\t\t(guest)");

            System.out.println();

            // check if directly enter any of the menu
            if (directToLoginMenu) {
                menuChoice = 3;
            } else {

                menuChoice = DisplayMenus.displayMenus(MainClass.userMode);
                // calculate menuChoice according to user mode
                // here is constant case number but different number in display side
                menuChoice = CalculateUserChoice.getEquivalentChoiceNumber(menuChoice);
            }

            // after directing to login set to initial state
            directToLoginMenu = false;
            // ask for choice

            // ask again if entered value is invalid???
            switch (menuChoice) {
                // 1 view book
                case 1:
                    // System.out.println("view books");
                    ViewBook.viewBooks();
                    directlyViewMenus = true;
                    break;
                // 2 search book
                case 2:
                    SearchBook.searchBook();
                    directlyViewMenus = true;
                    // System.out.println("search");
                    break;
                // Login
                case 3:
                    // always view menu directly whether login success or failed
                    // always true
                    directlyViewMenus = Login.logIn();
                    break;
                // 4signup
                case 4:
                    // after successful signup direct to login page
                    directToLoginMenu = Signup.signup();

                    directlyViewMenus = true;

                    break;
                // 5change privacy
                case 5:
                    ChangePrivacy.changePrivacy();
                    directlyViewMenus = true;

                    break;
                // 6logout
                case 6:

                    // always true
                    directlyViewMenus = Logout.logOut();
                    break;
                // 7add book
                case 7:
                    AddBook.addBook();
                    directlyViewMenus = true;

                    break;
                // 8remove book
                case 8:
                    // System.out.println("remove book");
                    RemoveBook.removeBookOptions();
                    directlyViewMenus = true;
                    break;
                // 9exit
                case 9:
                    System.out.println("exit");
                    exited = true;
                    break;
                // bdisplayborrowed books
                case 10:
                    BorrowBook.displayBorrowedBooks();
                    directlyViewMenus = true;
                    // System.out.println("display borrowed books");
                    break;
                // return book
                case 11:
                    System.out.println("\n--------------");
                    System.out.println("Return book");
                    System.out.println("--------------\n");
                    SearchByIsbn.searchBorrowByIsbn();
                    directlyViewMenus = true;
                    break;

                // add new admin
                case 12:
                    SignupAccount.addNewAdmin();
                    directlyViewMenus = true;
                    break;

            }
            // exit of exited without asking if continue
            if (exited) {
                break;
            }
            // Login.logIn();
            // System.out.println(MainClass.loggedIn);
            // System.out.println(MainClass.loggedInUser);
            // System.out.println(MainClass.userMode);

            // jump straight to display menu
            if (!directlyViewMenus) {

                System.out.println("Do you want to EXIT ? [y/n]: ");
                Scanner scanner = new Scanner(System.in);
                exitLoop = scanner.next().charAt(0);
            } else {
                exitLoop = 'n';
            }

        } while (!exited && exitLoop == 'n');
        System.out.println();
        // out of do while
        System.out.println("\tThanks for visiting !!!");
    }
}