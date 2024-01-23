package subclasses.privacy;

import subclasses.dbconnect.GetJdbcConnection;
import java.sql.*;
import java.util.Scanner;

import mainclass.MainClass;

public class ChangePrivacy {
    public static boolean showPassword = false;

    public static void changePrivacy() {
        Scanner scanner = new Scanner(System.in);

        try {
            if (!ChangePrivacy.showPassword) {

                System.out.println("\n---------------");
                System.out.println("Change Privacy");
                System.out.println("---------------");
                System.out.println();
            }

            String detailQuery = "select password,isAdmin,CURRENT_DATE from users where username='"
                    + MainClass.getLoggedInUser() + "'";
            Connection privacyCon = GetJdbcConnection.getConnection();
            Statement privacySt = privacyCon.createStatement();
            ResultSet detailRs = privacySt.executeQuery(detailQuery);
            detailRs.next();
            String password = detailRs.getString(1);
            boolean isAdmin = detailRs.getBoolean(2);
            String date = detailRs.getString(3);
            String user = "";
            user = (isAdmin) ? "Admin" : "Member";
            if (!ChangePrivacy.showPassword) {

                System.out.printf("\t\t\t\t +--------------+-------------------+%n");
                System.out.printf("\t\t\t\t | %-12s |  %-16s | %n", "  Username", MainClass.getLoggedInUser());
                System.out.printf("\t\t\t\t | %-12s |  %-16s | %n", "  Password", "*****");
                System.out.printf("\t\t\t\t | %-12s |  %-16s | %n", "  Date", date);
                System.out.printf("\t\t\t\t | %-12s |  %-16s | %n", "  You are", user);
                System.out.printf("\t\t\t\t +--------------+-------------------+%n");
            }
            ChangePrivacy.showPassword = false;
            System.out.println();
            System.out.println("\t\t\t1. Show Password\t\t2. Change Username\n\t\t\t3. Change Password\t\t4. Back");
            System.out.println();
            System.out.println("Enter your choice:");
            int choice = scanner.nextInt();
            switch (choice) {
                // show password
                case 1:
                    showPassword();
                    ChangePrivacy.showPassword = true;
                    ChangePrivacy.changePrivacy();
                    break;
                // change username
                case 2:
                    changeUsername();
                    break;
                // change password
                case 3:
                    changePassword();
                    break;
                // back
                case 4:
                    break;
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void showPassword() {
        try {

            System.out.println("\n---------------");
            System.out.println("Show Password");
            System.out.println("---------------");
            System.out.println();
            String detailQuery = "select password,isAdmin,CURRENT_DATE from users where username='"
                    + MainClass.getLoggedInUser() + "'";
            Connection privacyCon = GetJdbcConnection.getConnection();
            Statement privacySt = privacyCon.createStatement();
            ResultSet detailRs = privacySt.executeQuery(detailQuery);
            detailRs.next();
            String password = detailRs.getString(1);
            boolean isAdmin = detailRs.getBoolean(2);
            String date = detailRs.getString(3);
            String user = "";
            user = (isAdmin) ? "Admin" : "Member";
            if (!ChangePrivacy.showPassword) {

                System.out.printf("\t\t\t\t +--------------+-------------------+%n");
                System.out.printf("\t\t\t\t | %-12s |  %-16s | %n", "  Username", MainClass.getLoggedInUser());
                System.out.printf("\t\t\t\t | %-12s |  %-16s | %n", "  Password", password);
                System.out.printf("\t\t\t\t | %-12s |  %-16s | %n", "  Created", date);
                System.out.printf("\t\t\t\t | %-12s |  %-16s | %n", "  You are", user);
                System.out.printf("\t\t\t\t +--------------+-------------------+%n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void changeUsername() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("\n---------------");
            System.out.println("Change username");
            System.out.println("---------------");
            System.out.println();
            System.out.println("Enter new username:");
            String newUsername = scanner.next();
            System.out.println();
            System.out.println("Confirm change username? [y/n]");
            char confirm = scanner.next().charAt(0);
            // confirm add new username
            if (confirm == 'y') {
                String newUsernameQuery = "update users set username='" + newUsername + "' where username='"
                        + MainClass.getLoggedInUser() + "'";
                Connection con = GetJdbcConnection.getConnection();
                Statement st = con.createStatement();
                int r = st.executeUpdate(newUsernameQuery);
                if (r == 1) {
                    MainClass.setLoggedInUser(newUsername);
                    System.out.println("Successfully changed username to (" + newUsername + ")");
                }
            } else {
                System.out.println();
                System.out.println("Failed to update new username!!");
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    public static void changePassword() {
        Scanner scan = new Scanner(System.in);
        char retry = 'n';
        try {
            Connection con = GetJdbcConnection.getConnection();
            Statement st = con.createStatement();
            System.out.println("\n---------------");
            System.out.println("Change password");
            System.out.println("---------------");
            do {
                retry = 'n';
                System.out.println("\nEnter old password:");
                String oldPassword = scan.next();
                System.out.println("Enter new password:");
                String newPassword = scan.next();
                // get old passord to check
                String oldDbPassQuery = "select password from users where username='" + MainClass.getLoggedInUser()
                        + "'";
                ResultSet oldDbPassRs = st.executeQuery(oldDbPassQuery);
                oldDbPassRs.next();
                String oldDbPassword = oldDbPassRs.getString(1);
                // new and old password matched
                if (oldPassword.equals(oldDbPassword)) {

                    System.out.println("Confirm change password? [y/n]");
                    char confirm = scan.next().charAt(0);
                    // confirm add new username
                    if (confirm == 'y') {
                        String newPassQuery = "update users set password='" + newPassword + "' where username='"
                                + MainClass.getLoggedInUser() + "'";

                        int r = st.executeUpdate(newPassQuery);
                        if (r == 1) {

                            System.out.println("\nSuccessfully changed password!!");
                        }
                    } else {
                        System.out.println();
                        System.out.println("Failed to update new password!!");
                    }
                } else {
                    System.out.println("\nOld password did not match. Retry [y/n]");
                    retry = scan.next().charAt(0);
                }
            } while (retry == 'y');
            con.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
