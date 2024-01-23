package subclasses;

import java.util.Scanner;

import mainclass.MainClass;

public class Logout {
    public static boolean logOut() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Confirm logout? [y/n]");
        char confirmLogout;
        confirmLogout = scanner.next().charAt(0);

        if (confirmLogout == 'y') {

            MainClass.setLoggedIn(false);
            MainClass.setLoggedInUser("guest");
            MainClass.setUserMode("guest");
            System.out.println();
            System.out.println("You are logged out !");
        }
        // display message
        System.out.println();
        return true;
    }
}
