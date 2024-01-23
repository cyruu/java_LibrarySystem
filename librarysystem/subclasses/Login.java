package subclasses;

import java.util.Scanner;

import mainclass.MainClass;
import subclasses.loginmodes.AdminLogin;
import subclasses.loginmodes.MemberLogin;
import subclasses.validation.InputNumberValidation;
import subclasses.validinputgetter.GetValidInput;

public class Login {
    public static boolean logIn() {
        boolean loginSuccess = false;
        int loginChoice;
        boolean loginChoiceValid = false;
        Scanner scan = new Scanner(System.in);
        System.out.println("\t--------");
        System.out.println("\tLog in");
        System.out.println("\t--------");
        System.out.println("\t1. Admin\t\t2. Member\t\t3.Exit");
        System.out.println();
        System.out.println("Enter your choice: ");
        loginChoice = GetValidInput.getValidInput(3);

        switch (loginChoice) {

            // login as admin
            case 1:

                loginSuccess = AdminLogin.adminLoginValidation();
                // if admin was logged in successfully

                // directly display menus

                // MainClass.setUserMode("admin");
                // MainClass.setLoggedInUser("Cyrus");
                // MainClass.setLoggedIn(true);
                break;
            // login member
            case 2:

                loginSuccess = MemberLogin.memberLoginValidation();
                // MainClass.setUserMode("member");
                // MainClass.setLoggedInUser("Ram");
                // MainClass.setLoggedIn(true);
                break;
            case 3:
                break;
        }
        // display directly menus whether login success or fail
        return true;

    }

    public static boolean memberOnlyLogin() {
        boolean loginSuccess = false;
        int loginChoice;
        boolean loginChoiceValid = false;
        Scanner scan = new Scanner(System.in);
        System.out.println("\t--------");
        System.out.println("\tLog in");
        System.out.println("\t--------");
        System.out.println("\t1. Member\t\t2. Back");
        System.out.println();
        System.out.println("Enter your choice: ");
        loginChoice = GetValidInput.getValidInput(2);

        switch (loginChoice) {

            // login member
            case 1:

                loginSuccess = MemberLogin.memberLoginValidation();
                break;
            case 2:
                break;
        }
        return loginSuccess;
    }

}