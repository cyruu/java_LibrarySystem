package subclasses.loginmodes;

import java.util.Scanner;

import mainclass.MainClass;
import subclasses.validation.UserValidation;

public class MemberLogin {
    public static boolean memberLoginValidation() {
        boolean loginSuccess = false;
        String inputUsername;
        String inputPassword;
        boolean memberValid = false;
        Scanner scanner = new Scanner(System.in);
        char retry;
        String loginAs = "member";
        System.out.println();
        System.out.println("Clear input buffer [any character]:");
        do {
            scanner.nextLine();
            System.out.println();
            System.out.println("-----------------------");
            System.out.println("Member Verification ");
            System.out.println("-----------------------");
            System.out.println("Enter username: ");
            inputUsername = scanner.nextLine();
            System.out.println("Enter password:");

            inputPassword = scanner.nextLine();
            memberValid = UserValidation.isUserValid(inputUsername, inputPassword, loginAs);
            if (memberValid) {
                // login as admin
                MainClass.setUserMode("member");
                MainClass.setLoggedInUser(inputUsername);
                MainClass.setLoggedIn(true);
                loginSuccess = true;
                break;
            } else {
                System.out.println("Retry? [y/n]: ");
                retry = scanner.next().charAt(0);
            }

        } while (!memberValid && retry == 'y');
        return loginSuccess;
    }
}
