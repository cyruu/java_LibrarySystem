package subclasses.loginmodes;

import java.util.Scanner;

import mainclass.MainClass;
import subclasses.validation.UserValidation;

public class AdminLogin {
    public static boolean adminLoginValidation() {
        String inputUsername;
        String inputPassword;
        boolean adminValid = false;
        Scanner scanner = new Scanner(System.in);
        char retry;
        String loginAs = "admin";
        boolean loginSuccess = false;
        System.out.println();
        System.out.println("Clear input buffer [any character]:");
        do {
            scanner.nextLine();
            System.out.println();
            System.out.println("--------------------");
            System.out.println("Admin Verification ");
            System.out.println("--------------------");
            System.out.println("Enter username: ");
            inputUsername = scanner.nextLine();
            System.out.println("Enter password:");

            inputPassword = scanner.nextLine();
            adminValid = UserValidation.isUserValid(inputUsername, inputPassword, loginAs);
            if (adminValid) {
                // login as admin
                MainClass.setUserMode("admin");
                MainClass.setLoggedInUser(inputUsername);
                MainClass.setLoggedIn(true);
                loginSuccess = true;
                break;
                // incorrect username or password
            } else {
                System.out.println("Retry? [y/n]: ");
                retry = scanner.next().charAt(0);
            }
        } while (!adminValid && retry == 'y');
        // return if login was complete success
        return loginSuccess;
    }
}
