package subclasses;

import java.util.Scanner;

import mainclass.MainClass;
import subclasses.validation.InputNumberValidation;
import subclasses.validinputgetter.GetValidInput;

public class DisplayMenus {

    // display and ask for choice
    public static int displayMenus(String userMode) {
        boolean inputValid = false;
        Scanner scanner = new Scanner(System.in);

        int menuChoice;
        int exitNumber = 0;
        boolean loggedIn = MainClass.getLoggedIn();
        System.out.println("\t1. View books\t\t\t2. Search book");
        // for guest
        if (!loggedIn && userMode == "guest") {
            System.out.println("\t3. Log in\t\t\t4. Sign up");
            exitNumber = 5;

        }
        if (loggedIn && userMode == "member") {
            System.out.println("\t3. Borrowed books\t\t4. Return books");
            System.out.println("\t5. Change privacy\t\t6. Log out");
            exitNumber = 7;
        }
        if (loggedIn && userMode == "admin") {
            System.out.println("\t3. Add book\t\t\t4. Remove book");
            System.out.println("\t5. Change privacy\t\t6. Logout");
            System.out.printf("\t7. Add new admin\t");

            exitNumber = 8;
        }
        System.out.printf("\t" + exitNumber + ". Exit\n");

        // ask choice until valid input number

        System.out.println();
        System.out.println("Enter your choice:");
        menuChoice = GetValidInput.getValidInput(exitNumber);
        // do {

        // menuChoice = scanner.nextInt();
        // // check if input number has options to enter
        // inputValid = InputNumberValidation.isInputNumberValid(menuChoice);
        // // if input number has option
        // if (inputValid) {
        // break;
        // } else {
        // System.out.println("Enter valid choice: ");
        // }
        // } while (true);
        return menuChoice;
    }

}
